package com.pzg.code.sqlproject.operator;


import com.pzg.code.sqlproject.vo.DBTableColumn;
import com.pzg.code.sqlproject.vo.DataSourceProperties;

import java.util.List;
import java.util.Map;

public class MySqlOperator extends BaseDBOperator {

    private static final String DEFAULT_ALL_TABLES_SQL = "SELECT DISTINCT(table_name) FROM `information_schema`.`columns` " +
            "WHERE `table_schema` = ?";
    private static final String DEFAULT_ALL_COLUMNS_SQL = "SELECT table_name as tableName, column_name as columnName," +
            " data_type as dataType, column_comment as comments " +
            " FROM information_schema.columns WHERE table_name = ? AND table_schema = ?";
    private static final String allSampleDataSqlSuffix = "limit 100";

    private static final String endSql = " limit %d,%d";

    public MySqlOperator(DataSourceProperties dataSourceProperties) {
        this(dataSourceProperties, DEFAULT_ALL_TABLES_SQL, DEFAULT_ALL_COLUMNS_SQL, allSampleDataSqlSuffix);
    }

    public MySqlOperator(DataSourceProperties dataSourceProperties, String allTablesSql,
                         String allColumnsSql, String allSampleDataSqlSuffix) {
        super(dataSourceProperties, allTablesSql, allColumnsSql, allSampleDataSqlSuffix);
    }

    @Override
    public List<String> getAllTables() {
        List<Map<String, Object>> allTablesMap = queryForListMap(getAllTablesSql(),
                getDataSourceProperties().getDatabaseName());
        return mappingAllTablesResult(allTablesMap);
    }

    @Override
    public List<DBTableColumn> getAllColumns(String tableName) {
        List<Map<String, Object>> allColumnsMap = queryForListMap(getAllColumnsSql(),
                tableName, getDataSourceProperties().getDatabaseName());
        return mappingAllColumnsResult(allColumnsMap);
    }

    @Override
    public List<Map<String, Object>> queryForListMapPage(String sql, int[] arr) {
        sql = String.format(sql + endSql, arr[0], arr[1]);
        return getJdbcTemplate().queryForList(sql);
    }

    @Override
    public int[] pageCount(int pageNum, int startPage, int endPage, int pageSize) {
        int[] arr = new int[2];
        int start = 0;
        int end = 0;
        if (pageNum != 0) {
            start = (pageNum - 1) * pageSize;
            end = pageSize;

        } else if (startPage != 0 && endPage != 0) {
            start = (startPage - 1) * pageSize;
            end = endPage * pageSize - start;
        }
        arr[0] = start;
        arr[1] = end;
        return arr;
    }
}
