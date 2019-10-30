package com.pzg.code.sqlproject.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 描述： 数据源列表Dao
 */
@Mapper
public interface ApiOracleDao extends ApiDao {

    public Map selectRlidByTablename(String tableName);

}
