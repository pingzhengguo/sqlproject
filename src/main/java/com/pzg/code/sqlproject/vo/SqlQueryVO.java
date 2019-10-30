package com.pzg.code.sqlproject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**描述：sql服务
 * @author LiangJian
 * @date 2018/11/7 11:29
 * @modified By yaodeting
 * @since
 */
@ApiModel(value = "com.pzg.code.sqlproject.vo.SqlQueryVO")
@Data
public class SqlQueryVO {

	@ApiModelProperty(value="数据源厂商Id")
	private String slsId;
	
	@ApiModelProperty(value="业务系统Id")
	private String slbId;
	
	@ApiModelProperty(value="存储类型Id")
	private String stId;
	
	@ApiModelProperty(value="资源名称")
	private String slName;
	
	@ApiModelProperty(value = "页码", example = "1")
	private Integer pageNum;

	@ApiModelProperty(value = "页面大小", example = "3")
	private Integer pageSize;
	
}
