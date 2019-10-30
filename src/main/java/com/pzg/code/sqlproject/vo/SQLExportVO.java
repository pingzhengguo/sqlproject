package com.pzg.code.sqlproject.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述：SQL服务查询导出VO
 *
 */
@Setter
@Getter
public class SQLExportVO {

    /**
     * 描述：存储资源主键
     */
    private String storageId;

    /**
     * 描述：用户输入的查询SQL语句
     */
    private String sql;

    /**
     * 描述：开始页
     */
    private Integer startPage;

    /**
     * 描述：结束页
     */
    private Integer endPage;

    /**
     * 描述：切割大小
     */
    private Integer split;

    /**
     * 描述：前端分页大小
     */
    private Integer pageSize;

    public void setSplit(Integer split){
        this.split = split;
    }

    public Integer getSplit(){
        return this.split==null ? 60000 : this.split;
    }



    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }

    public Integer getPageSize(){
        return pageSize==null || pageSize<=0 ? 1000 : pageSize;
    }

}

