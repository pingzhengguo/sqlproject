package com.pzg.code.sqlproject.exception;

import com.pzg.code.sqlproject.utils.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**描述：controller统一处理异常
 * @author yaodeting
 * @date 2019/4/19 11:29
 * @modified By yaodeting
 * @since
 */

@Slf4j
@ControllerAdvice(basePackages = "com.hydata.product.datamanagersystem.controller")
public class GlobalExceptionHandler {

	/**
	* @Title: handleException
	* @Description: TODO(controller异常统一处理)
	* @return ResultBuilder<Object>    返回类型
	* @param e
	* @return
	*/ 
	@ExceptionHandler(Exception.class)
	public @ResponseBody
	ResultBuilder<Object> handleException(Exception e) {
		log.error("统一异常处理机制捕获异常： "+e);
		return ResultBuilder.fail(e.getMessage());
	}
}
