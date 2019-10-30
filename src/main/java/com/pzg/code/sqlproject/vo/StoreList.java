package com.pzg.code.sqlproject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述：数据源列表
 *
 * @since
 */
@ApiModel(value = "com.pzg.code.sqlproject.vo.StoreList")
@Data
public class StoreList {

    @ApiModelProperty(value = "存储列表Id")
    private String slId;

    @ApiModelProperty(value = "数据源厂商外键ID")
    private String slSlsId;

    @ApiModelProperty(value = "业务系统外键ID")
    private String slSlbId;

    @ApiModelProperty(value = "存储类型外键Id")
    private String slStId;

    @ApiModelProperty(value = "资源名称")
    private String slName;

    @ApiModelProperty(value = "存储连接外键")
    private String slSlcId;

    @ApiModelProperty(value = "存储表")
    private String slTableName;

    @ApiModelProperty(value = "备注")
    private String slRemark;

    @ApiModelProperty(value = "是否删除")
    private Short isDelete;

    @ApiModelProperty(value = "是否为敏感表，1表示敏感，0表示不敏感")
    private Integer isSensitive;

    @ApiModelProperty(value = "是否注册，空值表示没有注册，1表示结构定义，2表示详情定义，3表示数据定义，1,2表示表结构和表详情定义")
    private String isRegister;
}
