package com.pzg.code.sqlproject.mapper;


import com.pzg.code.sqlproject.vo.Sql;
import com.pzg.code.sqlproject.vo.SqlQueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 描述：sql服务mapper
 *
 * @since
 */
@Mapper
public interface SqlMapper {

    public List<Sql> findByParam(SqlQueryVO sqlQueryVO);

}
