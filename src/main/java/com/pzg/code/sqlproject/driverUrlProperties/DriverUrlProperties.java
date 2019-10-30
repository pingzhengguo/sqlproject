package com.pzg.code.sqlproject.driverUrlProperties;


import com.pzg.code.sqlproject.vo.DBType;

public interface DriverUrlProperties {

    /**
     * 数据库类型
     * @return
     */
    DBType dbType();

    /**
     * 驱动类名
     * @return
     */
    String driverClassName();

    /**
     * 数据库连接url
     * @return
     */
    String url();

}
