package com.pzg.code.sqlproject.driverUrlProperties;


import com.pzg.code.sqlproject.vo.DBType;
import com.pzg.code.sqlproject.vo.DataSourceProperties;

public class OracleDriverUrlProperties extends BaseDriverUrlProperties {

    // private static final String DEFAULT_DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
    public static final String DEFAULT_DRIVER_CLASS_NAME = "oracle.jdbc.OracleDriver";
    /**
     * jdbc:oracle:thin:@//%s:%s:%s 格式只支持SID的连接
     * jdbc:oracle:thin:@//%s:%s/%s 格式支持service_name 与 单实例的SID连接
     */
    public static final String DEFAULT_SERVICE_NAME_URL_PATTERN = "jdbc:oracle:thin:@//%s:%s/%s";
    public static final String DEFAULT_SID_URL_PATTERN = "jdbc:oracle:thin:@%s:%s:%s";


    public OracleDriverUrlProperties(DataSourceProperties dataSourceProperties) {
        this(DEFAULT_DRIVER_CLASS_NAME, DEFAULT_SERVICE_NAME_URL_PATTERN, null, dataSourceProperties);
    }

    public OracleDriverUrlProperties(String driverClassName, String urlPattern, DataSourceProperties dataSourceProperties) {
        this(driverClassName, urlPattern, null, dataSourceProperties);
    }

    public OracleDriverUrlProperties(String driverClassName, String urlPattern, String urlParameter, DataSourceProperties dataSourceProperties) {
        super(driverClassName, urlPattern, urlParameter, dataSourceProperties);
    }

    @Override
    protected String formatUrl(String urlPattern, DataSourceProperties dataSourceProperties) {
        return String.format(urlPattern, dataSourceProperties.getHost(),
                dataSourceProperties.getPort(), dataSourceProperties.getDatabaseName());
    }

    @Override
    public DBType dbType() {
        return DBType.ORACLE;
    }
}
