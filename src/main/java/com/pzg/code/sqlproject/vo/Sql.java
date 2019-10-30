package com.pzg.code.sqlproject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**描述：sql服务
 */

@ApiModel(value = "com.pzg.code.sqlproject.vo.Sql")
@Data
public class Sql {

	@ApiModelProperty(value="数据源厂商")
	private String slsName;
	
	@ApiModelProperty(value="业务系统")
	private String slbName;
	
	@ApiModelProperty(value="存储类型Id")
	private String stId;
	
	@ApiModelProperty(value="存储类型名称")
	private String stName;
	
	@ApiModelProperty(value="资源Id")
	private String slId;
	
	@ApiModelProperty(value="资源名称")
	private String slName;
	
	@ApiModelProperty(value="表名称")
	private String slTableName;
	
	@ApiModelProperty(value="数据量")
	private Long sMount;
}
