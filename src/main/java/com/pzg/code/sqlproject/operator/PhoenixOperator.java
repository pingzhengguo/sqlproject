package com.pzg.code.sqlproject.operator;

import com.pzg.code.sqlproject.vo.DBTableColumn;
import com.pzg.code.sqlproject.vo.DataSourceProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhoenixOperator extends BaseDBOperator {
    public PhoenixOperator(DataSourceProperties dataSourceProperties) {
        super(dataSourceProperties, null, null, null);
    }

    public PhoenixOperator(DataSourceProperties dataSourceProperties, String allTablesSql, String allColumnsSql, String allSampleDataSqlSuffix) {
        super(dataSourceProperties, allTablesSql, allColumnsSql, allSampleDataSqlSuffix);
    }

    private static final String endSql = " limit %d offset  %d";

    private static final String DEFAULT_ALL_COLUMNS_SQL_START = "select COLUMN_NAME,SqlTypeName(DATA_TYPE) from SYSTEM.CATALOG where table_name = '";

    private static final String DEFAULT_ALL_COLUMNS_SQL_END = "' and COLUMN_NAME is not null";

    @Override
    public List<Map<String, Object>> queryForListMapPage(String sql, int[] arr) {
        sql = String.format(sql + endSql, arr[1], arr[0]);
        System.out.println("sql语句：" + sql);
        return getJdbcTemplate().queryForList(sql);
    }

    @Override
    public List<DBTableColumn> getAllColumns(String tableName) {
        List<Map<String, Object>> allColumnsMap = queryForListMap(DEFAULT_ALL_COLUMNS_SQL_START + tableName + DEFAULT_ALL_COLUMNS_SQL_END);
        return mappingAllColumnsResult(allColumnsMap);
    }

    @Override
    protected List<DBTableColumn> mappingAllColumnsResult(List<Map<String, Object>> allColumnsMap) {
        List<DBTableColumn> columnsList = new ArrayList();
        allColumnsMap.forEach(columnMap -> {
            String column_name = (String) columnMap.get("COLUMN_NAME");
            String type = (String) columnMap.get("SqlTypeName(DATA_TYPE)");
            columnsList.add(new DBTableColumn(column_name, type, ""));
        });
        return columnsList;
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
