package com.pzg.code.sqlproject.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DataAccessMapper {

    Map findSourcePropertis(String sourceId);

    Map findStorePropertis(String storageId);

}
