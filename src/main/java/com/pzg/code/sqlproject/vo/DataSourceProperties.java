package com.pzg.code.sqlproject.vo;


import com.pzg.code.sqlproject.driverUrlProperties.DriverUrlProperties;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Objects;

public class DataSourceProperties implements Serializable {

    private static final long serialVersionUID = 3830970525724532542L;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    private DBType dbType;

    private OracleInstanceType instanceType;

    private DriverUrlProperties driverUrlProperties;
    /**
     * 数据库主机名/IP
     */
    private String host;
    /**
     * 端口号
     */
    private String port;

    /**
     * 数据库名称
     */
    private String databaseName;

    public DataSourceProperties() {
    }

    public DataSourceProperties(String username, String password, DBType dbType, String host, String port, String databaseName) {
        this(username, password, dbType, host, port, databaseName, null);
    }

    public DataSourceProperties(String username, String password, DBType dbType, String host,
                                String port, String databaseName, OracleInstanceType instanceType) {
        this.username = username;
        this.password = password;
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.instanceType = instanceType;
    }

    public DataSourceProperties(String username, String password, Class<? extends DriverUrlProperties> clazz,
                                String host, String port, String databaseName) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        initDriverUrlProperties(clazz);
    }

    private void initDriverUrlProperties(Class<? extends DriverUrlProperties> clazz) {
        try {
            Constructor<? extends DriverUrlProperties> constructor = clazz.getConstructor(DataSourceProperties.class);
            this.driverUrlProperties = constructor.newInstance(this);
        } catch (Exception e) {
            throw new IllegalArgumentException(clazz + "实例化失败！", e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    public OracleInstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(OracleInstanceType instanceType) {
        this.instanceType = instanceType;
    }

    public DriverUrlProperties getDriverUrlProperties() {
        return driverUrlProperties;
    }

    public void setDriverUrlProperties(DriverUrlProperties driverUrlProperties) {
        this.driverUrlProperties = driverUrlProperties;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSourceProperties)) {
            return false;
        }
        DataSourceProperties that = (DataSourceProperties) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(driverUrlProperties, that.driverUrlProperties) &&
                Objects.equals(getHost(), that.getHost()) &&
                Objects.equals(getPort(), that.getPort()) &&
                Objects.equals(getDatabaseName(), that.getDatabaseName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), driverUrlProperties, getHost(), getPort(), getDatabaseName());
    }
}
