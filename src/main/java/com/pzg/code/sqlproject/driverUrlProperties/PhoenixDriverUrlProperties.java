package com.pzg.code.sqlproject.driverUrlProperties;


import com.pzg.code.sqlproject.vo.DBType;
import com.pzg.code.sqlproject.vo.DataSourceProperties;

public class PhoenixDriverUrlProperties extends BaseDriverUrlProperties {
//    jdbc:phoenix:master.haiyun:2181:/hbase-unsecure
    public static final String DEFAULT_DRIVER_CLASS_NAME = "org.apache.phoenix.jdbc.PhoenixDriver";
    private static final String DEFAULT_URL_PATTERN = "jdbc:phoenix:%s:%s:%s";

    public PhoenixDriverUrlProperties(DataSourceProperties dataSourceProperties) {
        this(DEFAULT_DRIVER_CLASS_NAME, DEFAULT_URL_PATTERN, null, dataSourceProperties);
    }

    protected PhoenixDriverUrlProperties(String driverClassName, String urlPattern, String urlParameter, DataSourceProperties dataSourceProperties) {
        super(driverClassName, urlPattern, urlParameter, dataSourceProperties);
    }

    @Override
    protected String formatUrl(String urlPattern, DataSourceProperties dataSourceProperties) {
        return String.format(urlPattern, dataSourceProperties.getHost(),
                dataSourceProperties.getPort(), dataSourceProperties.getDatabaseName());
    }

    @Override
    public DBType dbType() {
        return DBType.PHOENIX;
    }
}
