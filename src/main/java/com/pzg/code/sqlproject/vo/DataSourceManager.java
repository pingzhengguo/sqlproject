package com.pzg.code.sqlproject.vo;

import com.alibaba.druid.pool.DruidDataSource;
import com.pzg.code.sqlproject.driverUrlProperties.DriverUrlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供DataSource的管理。
 * 对于同一个数据库的连接只保持有一个DataSource，所有的Connection都应该通过连接池来获取，以节约创建连接的时间及性能消耗。
 */
public class DataSourceManager {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

    private static Map<DataSourceProperties, DataSource> targetDataSources = new ConcurrentHashMap<>();

    /**
     * 连接时最大等待时间，单位毫秒
     */
    @Value("${hymodel.druid.maxWait}")
    private long maxWait;

    /**
     * 设置连接失败重连次数，默认为1，但实际使用的时候，如果oracle的实例名写错了，会不断重连
     */
    @Value("${hymodel.druid.connectionErrorRetryAttempts}")
    private int connectionErrorRetryAttempts;

    /**
     * true表示pool向数据库请求连接失败后标记整个pool为block并close，就算后端数据库恢复正常也不进行重连，客户端对pool的请求都拒绝掉
     */
    @Value("${hymodel.druid.breakAfterAcquireFailure}")
    private boolean breakAfterAcquireFailure;

    /**
     * 获取dbProperties对应的数据源
     *
     * @param dataSourceProperties
     * @return
     */
    public DataSource getDataSource(DataSourceProperties dataSourceProperties) {
        DataSource dataSource = targetDataSources.get(dataSourceProperties);

        if (dataSource == null) {
            dataSource = newDataSource(dataSourceProperties);
        }
        logger.debug("当前DataSourceManager#targetDataSources中包含的DataSource数量为{}，详细内容：{}",
                targetDataSources.size(), targetDataSources);
        return dataSource;
    }

    public synchronized boolean removeDataSource(DataSourceProperties dataSourceProperties) {
        DataSource dataSource = targetDataSources.get(dataSourceProperties);
        if (dataSource != null) {
            return dataSource == targetDataSources.remove(dataSourceProperties);
        }
        return false;
    }

    /**
     * 新建一个DataSource
     *
     * @param dataSourceProperties 新建DataSource所需的一些参数封装
     * @return
     */
    private DataSource newDataSource(DataSourceProperties dataSourceProperties) {
        return doCreateDataSource(dataSourceProperties);
    }

    protected synchronized DataSource doCreateDataSource(DataSourceProperties dataSourceProperties) {
        DataSource dataSource = targetDataSources.get(dataSourceProperties);
        if (dataSource == null) {
            dataSource = newDataSourceInstance(dataSourceProperties);
        }
        return dataSource;
    }

    protected synchronized DataSource newDataSourceInstance(DataSourceProperties dataSourceProperties) {
        DriverUrlProperties driverUrlProperties = dataSourceProperties.getDriverUrlProperties();
        try {
            Class.forName(driverUrlProperties.driverClassName());
        } catch (ClassNotFoundException e) {
            String errorMessage = String.format("加载数据库驱动类[%s]失败！", driverUrlProperties.driverClassName());
            logger.error(errorMessage);

            throw new IllegalArgumentException(errorMessage);
        }
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverUrlProperties.driverClassName());
        druidDataSource.setUrl(driverUrlProperties.url());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        // 检测连接是否有效的超时时间，单位秒，不起作用
        // druidDataSource.setValidationQueryTimeout(3);
        // 获取连接时最大等待时间，单位毫秒
        druidDataSource.setMaxWait(maxWait);
        // 设置连接失败重连次数，默认为1，但实际使用的时候，如果oracle的实例名写错了，会不断重连
        druidDataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        //  true表示pool向数据库请求连接失败后标记整个pool为block并close，就算后端数据库恢复正常也不进行重连，客户端对pool的请求都拒绝掉
        druidDataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
        // TODO 其他参数设置，如最大连接数等11
        targetDataSources.put(dataSourceProperties, druidDataSource);

        return druidDataSource;
    }

}
