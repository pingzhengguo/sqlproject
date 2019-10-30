package com.pzg.code.sqlproject.driverUrlProperties;



import com.pzg.code.sqlproject.vo.DataSourceProperties;
import org.springframework.util.StringUtils;

import java.util.Objects;

public abstract class BaseDriverUrlProperties implements DriverUrlProperties {

    private final String driverClassName;
    private final String urlPattern;
    private final String urlParameter;
    private final DataSourceProperties dataSourceProperties;

    protected BaseDriverUrlProperties(String driverClassName, String urlPattern, String urlParameter, DataSourceProperties dataSourceProperties) {
        this.driverClassName = driverClassName;
        this.urlPattern = urlPattern;
        this.urlParameter = urlParameter;
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public String driverClassName() {
        return this.driverClassName;
    }

    @Override
    public String url() {
        String formatParameter = StringUtils.isEmpty(this.urlParameter) ? "" : "?" + this.urlParameter;
        return formatUrl(urlPattern, dataSourceProperties) + formatParameter;
    }

    /**
     * 格式化url，由子类进行重写
     * @param urlPattern
     * @param dataSourceProperties
     * @return
     */
    protected abstract String formatUrl(String urlPattern, DataSourceProperties dataSourceProperties);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseDriverUrlProperties)) {
            return false;
        }
        BaseDriverUrlProperties that = (BaseDriverUrlProperties) o;
        return Objects.equals(driverClassName, that.driverClassName) &&
                Objects.equals(urlPattern, that.urlPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverClassName, urlPattern);
    }

    @Override
    public String toString() {
        return "BaseDriverUrlProperties{" +
                "driverClassName='" + driverClassName + '\'' +
                ", urlPattern='" + urlPattern + '\'' +
                ", urlParameter='" + urlParameter + '\'' +
                '}';
    }
}
