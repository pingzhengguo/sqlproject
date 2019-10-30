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
public class StoreListQueryVO {
	
	@ApiModelProperty(value="存储源Id")
	private String slId;
	
	@ApiModelProperty(value="存储类型外键Id")
	private String slStId;
	
	@ApiModelProperty(value="存储类型名称")
	private String stName;
	
	@ApiModelProperty(value="数据源厂商外键ID")
	private String slSlsId;
	
	@ApiModelProperty(value="业务系统外键ID")
	private String slSlbId;
	
	@ApiModelProperty(value="资源名称")
	private String slName;
	
	@ApiModelProperty(value = "页码", example = "1")
	private Integer pageNum;

	@ApiModelProperty(value = "页面大小", example = "3")
	private Integer pageSize;
}