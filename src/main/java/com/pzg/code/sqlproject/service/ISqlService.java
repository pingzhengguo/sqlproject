package com.pzg.code.sqlproject.service;

import com.github.pagehelper.PageInfo;
import com.pzg.code.sqlproject.vo.SQLExportVO;
import com.pzg.code.sqlproject.vo.Sql;
import com.pzg.code.sqlproject.vo.SqlQueryVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface ISqlService {

	/**
	* @Title: findByParam
	* @Description: TODO(条件分页查询)
	* @return PageInfo<Sql>    返回类型
	* @param sqlQueryVO
	* @return
	*/ 
	public PageInfo<Sql> findByParam(SqlQueryVO sqlQueryVO);


	/**
	 * 描述：SQL服务数据查询导出成EXCEL（多个文件打包成ZIP）
	 *
	 * @param req
	 * @param resp
	 * @param vo
	 * @return void
	 */
	void exportExcel(HttpServletRequest req,
                     HttpServletResponse resp,
                     SQLExportVO vo);

}
