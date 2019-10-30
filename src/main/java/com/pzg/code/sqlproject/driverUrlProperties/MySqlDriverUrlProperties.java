package com.pzg.code.sqlproject.driverUrlProperties;


import com.pzg.code.sqlproject.vo.DBType;
import com.pzg.code.sqlproject.vo.DataSourceProperties;

public class MySqlDriverUrlProperties extends BaseDriverUrlProperties {

    public static final String DEFAULT_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DEFAULT_URL_PATTERN = "jdbc:mysql://%s:%s/%s";
    private static final String DEFAULT_URL_PARAMETER = "useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false";

    public MySqlDriverUrlProperties(DataSourceProperties dataSourceProperties) {
        this(DEFAULT_DRIVER_CLASS_NAME, DEFAULT_URL_PATTERN, DEFAULT_URL_PARAMETER, dataSourceProperties);
    }

    public MySqlDriverUrlProperties(String driverClassName, String urlPattern, DataSourceProperties dataSourceProperties) {
        this(driverClassName, urlPattern, null, dataSourceProperties);
    }

    public MySqlDriverUrlProperties(String driverClassName, String urlPattern, String urlParameter, DataSourceProperties dataSourceProperties) {
        super(driverClassName, urlPattern, urlParameter, dataSourceProperties);
    }

    @Override
    public DBType dbType() {
        return DBType.MYSQL;
    }

    @Override
    protected String formatUrl(String urlPattern, DataSourceProperties dataSourceProperties) {
        return String.format(urlPattern, dataSourceProperties.getHost(),
                dataSourceProperties.getPort(), dataSourceProperties.getDatabaseName());
    }


}
