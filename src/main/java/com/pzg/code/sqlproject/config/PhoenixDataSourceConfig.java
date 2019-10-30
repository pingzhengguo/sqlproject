package com.pzg.code.sqlproject.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = PhoenixDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "phoenixSqlSessionFactory")
public class PhoenixDataSourceConfig {
    // 精确到 phoenix 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hiynn.gybigdata.dao.phoenix";
    static final String MAPPER_LOCATION = "classpath*:mapper/phoenix/*.xml";

    @Value("${phoenix.datasource.url}")
    private String url;

    @Value("${phoenix.datasource.username}")
    private String user;

    @Value("${phoenix.datasource.password}")
    private String password;

    @Value("${phoenix.datasource.driver-class-name}")
    private String driverClass;

    @Bean(name = "phoenixDataSource")
    public DataSource phoenixDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "phoenixTransactionManager")
    public DataSourceTransactionManager phoenixTransactionManager() {
        return new DataSourceTransactionManager(phoenixDataSource());
    }

    @Bean(name = "phoenixSqlSessionFactory")
    public SqlSessionFactory phoenixSqlSessionFactory(@Qualifier("phoenixDataSource") DataSource phoenixDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(phoenixDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(PhoenixDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}