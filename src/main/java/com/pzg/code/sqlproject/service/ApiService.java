package com.pzg.code.sqlproject.service;


import com.pzg.code.sqlproject.utils.RequestResult;

/**
 * 描述：api相关查询处理
 */
public interface ApiService {

    /**
     * 根据slid查询表信息
     *
     * @param slId
     * @return
     */
    public RequestResult getTableInfo(String slId, String account);

    /**
     * 根据slid查询表数据
     *
     * @param slId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public RequestResult getTableDatas(String slId, Integer pageNum, Integer pageSize, String account);

    /**
     * 根据slid查询表数据量
     *
     * @param slId
     * @return
     */
    public RequestResult getTableDataCount(String slId, String account);
}
