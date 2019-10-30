package com.pzg.code.sqlproject.operator;


import com.pzg.code.sqlproject.utils.DataSourceUtils;
import com.pzg.code.sqlproject.vo.DBTableColumn;
import com.pzg.code.sqlproject.vo.DataSourceProperties;
import com.pzg.code.sqlproject.vo.SampleData;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseDBOperator implements DBOperator {

    private static final Logger logger = LoggerFactory.getLogger(BaseDBOperator.class);
    private static final String ALL_SAMPLE_DATA_SQL_PREFIX = "SELECT * FROM ? ";
    private String allSampleDataSqlSuffix = "";


    private String allTablesSql;
    private String allColumnsSql;
    private DataSourceProperties dataSourceProperties;

    public BaseDBOperator(DataSourceProperties dataSourceProperties, String allTablesSql, String allColumnsSql, String allSampleDataSqlSuffix) {
        this.dataSourceProperties = dataSourceProperties;
        this.allTablesSql = allTablesSql;
        this.allColumnsSql = allColumnsSql;
        this.allSampleDataSqlSuffix = allSampleDataSqlSuffix;
    }

    @Deprecated
    protected JdbcTemplate getJdbcTemplate(DataSourceProperties dataSourceProperties) {
        return new JdbcTemplate(DataSourceUtils.getDataSource(dataSourceProperties));
    }

    protected JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(DataSourceUtils.getDataSource(this.dataSourceProperties));
    }

    @Override
    public List<String> getAllTables() {
        logger.debug("获取所有表名称: 数据源: {}, sql: {}", dataSourceProperties, getAllTablesSql());
        List<Map<String, Object>> allTablesMap = queryForListMap(dataSourceProperties, getAllTablesSql());
        return mappingAllTablesResult(allTablesMap);
    }

    /**
     * 对查询所有表名称结果的映射，默认情况下值读取到Map中的第一个值
     * 如果查询返回多个字段的话，需要在子类中重写该方法获取指定字段（准确说应该是表名字段）上的值，进行table名称映射
     *
     * @param allTablesMap
     * @return
     */
    protected List<String> mappingAllTablesResult(List<Map<String, Object>> allTablesMap) {
        return allTablesMap.stream().map(tableMap -> String.valueOf(tableMap.entrySet().stream().findFirst().get().getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DBTableColumn> getAllColumns(String tableName) {
        logger.debug("获取 {}表信息: 数据源: {}, sql: {}", tableName, dataSourceProperties, getAllColumnsSql());
        List<Map<String, Object>> allColumnsMap = queryForListMap(getAllColumnsSql(), tableName);

        return mappingAllColumnsResult(allColumnsMap);
    }

    @Override
    public SampleData getSampleData(String tableName) {
        SampleData sampleData = new SampleData();
        List<DBTableColumn> tableColumns = getAllColumns(tableName);
        sampleData.setTableColumns(tableColumns);
        String sql = ALL_SAMPLE_DATA_SQL_PREFIX.replace("?", tableName) + allSampleDataSqlSuffix;
        sampleData.setSampleData(getJdbcTemplate().queryForList(sql));
        return sampleData;
    }

    /**
     * 对查询指定表信息结果的映射，默认情况下查询出来的字段名称应该与{@link DBTableColumn}中的属性名称一致
     * 如果查询返回的字段名称与{@link DBTableColumn}中的属性名称不一致，需要在子类中重写该方法
     *
     * @param allColumnsMap
     * @return
     */
    protected List<DBTableColumn> mappingAllColumnsResult(List<Map<String, Object>> allColumnsMap) {
        return allColumnsMap.stream().map(columnMap -> convertMap2Bean(columnMap, new DBTableColumn()))
                .collect(Collectors.toList());
    }

    @Deprecated
    protected List<Map<String, Object>> queryForListMap(DataSourceProperties dataSourceProperties, String sql, Object... params) {
        return getJdbcTemplate(dataSourceProperties).queryForList(sql, params);
    }

    protected List<Map<String, Object>> queryForListMap(String sql, Object... params) {

        return getJdbcTemplate().queryForList(sql, params);
    }

    private <T> T convertMap2Bean(Map<String, Object> map, T bean) {
        try {
            BeanUtils.populate(bean, map);
            return bean;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Map2Bean转换失败", e);
        }
    }

    public String getAllTablesSql() {
        return allTablesSql;
    }

    public void setAllTablesSql(String allTablesSql) {
        this.allTablesSql = allTablesSql;
    }

    public String getAllColumnsSql() {
        return allColumnsSql;
    }

    public void setAllColumnsSql(String allColumnsSql) {
        this.allColumnsSql = allColumnsSql;
    }

    public DataSourceProperties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    /**
     * 根据sql获取数据
     *
     * @param sql
     * @return
     */
    public abstract List<Map<String, Object>> queryForListMapPage(String sql, int[] arr);

    /**
     * 计算分页start和end
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public abstract int[] pageCount(int pageNum, int startPage, int endPage, int pageSize);

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = getJdbcTemplate().getDataSource().getConnection();
        return conn;

    }

    @Override
    public Map<String, Object> getCount(String name, String condition) {
        return getJdbcTemplate().queryForMap("select count(*) totalCount from " + name + " " + condition);
    }

}
