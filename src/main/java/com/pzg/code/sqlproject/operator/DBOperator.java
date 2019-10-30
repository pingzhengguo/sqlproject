package com.pzg.code.sqlproject.operator;


import com.pzg.code.sqlproject.vo.DBTableColumn;
import com.pzg.code.sqlproject.vo.SampleData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DBOperator {

    /**
     * 获取当前数据源中的所有表
     *
     * @return 返回表名集合
     */
    List<String> getAllTables();

    /**
     * 获取当前数据源中指定表的字段信息
     *
     * @param tableName 表名
     * @return 每一个字段对应一个DBTableColumn对象，返回所有字段对应DBTableColumn的集合
     */
    List<DBTableColumn> getAllColumns(String tableName);

    /**
     * 获取当前数据源中指定表的样例数据
     *
     * @param tableName
     * @return
     */
    SampleData getSampleData(String tableName);

    Connection getConnection() throws SQLException;

    Map<String, Object> getCount(String name, String condition);
}
