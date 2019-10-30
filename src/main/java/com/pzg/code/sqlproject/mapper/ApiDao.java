package com.pzg.code.sqlproject.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述：api查询表接口
 *
 */
@Mapper
public interface ApiDao {
    /**
     * 根据表名查询表基本信息
     * @param tableName
     * @return
     */
    public List<Map> selectTableInfo(String tableName);
    /**
     * 根据表名查询表数据
     * @param tableName
     * @return
     */
    public List<Map> selectTableData(String tableName);
    /**
     * 根据表名查询表数据量
     * @param tableName
     * @return
     */
    public Map selectTableDataCount(String tableName);
}
