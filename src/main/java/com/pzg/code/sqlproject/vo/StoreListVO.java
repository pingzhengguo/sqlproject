package com.pzg.code.sqlproject.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**描述：存储表
 */
@Getter
@Setter
@ToString
public class StoreListVO {

	@ApiModelProperty(value="存储列表Id")
	private String slId;
	
	@ApiModelProperty(value="数据源厂商外键ID")
	private String slSlsId;
	
	@ApiModelProperty(value="业务系统外键ID")
	private String slSlbId;
	
	@ApiModelProperty(value="存储类型外键Id")
	private String slStId;
	
	@ApiModelProperty(value="资源名称")
	private String slName;
	
	@ApiModelProperty(value="存储连接外键")
	private String slSlcId;
	
	@ApiModelProperty(value="存储表")
	private String slTableName;
	
	@ApiModelProperty(value="备注")
	private String slRemark;
	
	@ApiModelProperty(value="是否删除")
	private Short isDelete;
	
	@ApiModelProperty(value="是否为敏感表，1表示敏感，0表示不敏感")
	private Integer isSensitive;
	
	@ApiModelProperty(value="是否注册，空值表示没有注册，1表示结构定义，2表示详情定义，3表示数据定义，1,2表示表结构和表详情定义")
	private String isRegister;
	
	// =======================================================================
	// 以下为组合字段，是为了在查询时直接返回给前台相应的字段展示用，而不再需要再此查询
	
	@ApiModelProperty(value="存储类型名称")
	private String stName;
	
	@ApiModelProperty(value="数据源厂商")
	private String slsName;
	
	@ApiModelProperty(value="业务系统")
	private String slbName;
	
	@ApiModelProperty(value="存储连接名称")
	private String slcName;
	
	@ApiModelProperty(value="存储连接内容")
	private String slcContent;
}
