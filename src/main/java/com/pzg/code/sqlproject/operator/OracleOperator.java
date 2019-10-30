package com.pzg.code.sqlproject.operator;


import com.pzg.code.sqlproject.vo.DataSourceProperties;

import java.util.List;
import java.util.Map;

public class OracleOperator extends BaseDBOperator {

    private static final String DEFAULT_ALL_TABLES_SQL = "select table_name from user_tables";
    private static final String DEFAULT_ALL_COLUMNS_SQL = "select t.table_name as \"tableName\", t.COLUMN_NAME as \"columnName\"," +
            " t.data_type as \"dataType\", c.COMMENTS as \"comments\" from user_tab_cols t, user_col_comments c " +
            "where c.table_name = t.table_name and c.column_name = t.column_name and t.table_name = ?";
    private static final String allSampleDataSqlSuffix = "WHERE ROWNUM <= 100";

    private static final String startSql = "SELECT * FROM (SELECT A.*, ROWNUM num FROM (";

    private static final String endSql = ") A WHERE ROWNUM <= %d) WHERE num >= %d";

    public OracleOperator(DataSourceProperties dataSourceProperties) {
        this(dataSourceProperties, DEFAULT_ALL_TABLES_SQL, DEFAULT_ALL_COLUMNS_SQL, allSampleDataSqlSuffix);
    }

    public OracleOperator(DataSourceProperties dataSourceProperties, String allTablesSql, String allColumnsSql, String allSampleDataSqlSuffix) {
        super(dataSourceProperties, allTablesSql, allColumnsSql, allSampleDataSqlSuffix);
    }

    @Override
    public List<Map<String, Object>> queryForListMapPage(String sql, int[] arr) {
        sql = String.format(startSql + sql + endSql, arr[1], arr[0]);
        return getJdbcTemplate().queryForList(sql);
    }

    @Override
    public int[] pageCount(int pageNum, int startPage, int endPage, int pageSize) {
        int[] arr = new int[2];
        int start = 0;
        int end = 0;
        if (pageNum != 0) {
            start = (pageNum - 1) * pageSize + 1;
            end = start + pageSize - 1;

        } else if (startPage != 0 && endPage != 0) {
            start = (startPage - 1) * pageSize + 1;
            end = endPage * pageSize;
        }
        arr[0] = start;
        arr[1] = end;
        return arr;
    }

}
