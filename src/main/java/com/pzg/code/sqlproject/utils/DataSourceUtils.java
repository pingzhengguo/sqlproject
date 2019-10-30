package com.pzg.code.sqlproject.utils;


import com.pzg.code.sqlproject.driverUrlProperties.DriverUrlProperties;
import com.pzg.code.sqlproject.vo.DataSourceManager;
import com.pzg.code.sqlproject.vo.DataSourceProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceUtils {

    private static final DataSourceManager dataSourceManager = new DataSourceManager();

    private DataSourceUtils() {
    }

    /**
     * 获取dataSourceProperties相关的DataSource
     *
     * @param dataSourceProperties
     * @return
     */
    public static DataSource getDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceManager.getDataSource(dataSourceProperties);
    }

    /**
     * 获取dataSourceProperties相关的一个Connection
     *
     * @param dataSourceProperties
     * @return
     */
    public static Connection getConnection(DataSourceProperties dataSourceProperties) throws SQLException {
        return org.springframework.jdbc.datasource.DataSourceUtils.getConnection(getDataSource(dataSourceProperties));
    }

    /**
     * 释放{@link this#getConnection(DataSourceProperties)}获取到的Connection
     *
     * @param dataSourceProperties
     * @param connection
     */
    public static void releaseConnection(DataSourceProperties dataSourceProperties, Connection connection) {
        org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection(connection, getDataSource(dataSourceProperties));
    }

    /**
     * 验证DataSourceProperties相关配置是否是一个可用的数据源配置，由于只是用于验证数据源配置的正确性，这里并不会创建DataSource
     *
     * @param dataSourceProperties
     * @return
     * @throws Exception
     */
    public static Boolean connectionValid(DataSourceProperties dataSourceProperties) throws Exception {
        DriverUrlProperties driverUrlProperties = dataSourceProperties.getDriverUrlProperties();
        Class.forName(driverUrlProperties.driverClassName());

        try (Connection connection = DriverManager.getConnection(driverUrlProperties.url(),
                dataSourceProperties.getUsername(), dataSourceProperties.getPassword())) {
            return true;
        }
    }

}
