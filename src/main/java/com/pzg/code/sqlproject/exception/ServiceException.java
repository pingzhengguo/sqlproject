package com.pzg.code.sqlproject.exception;


/**描述：service层异常
 * @author yaodeting
 * @date 2019/4/19 11:29
 * @modified By yaodeting
 * @since
 */

public class ServiceException extends BaseException {

	/**
	 * Fields serialVersionUID: TODO(用一句话描述这个变量表示什么) 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * <p>Title: ServiceException </p>
	 * <p>Description: Constructor </p> 
	 * @param errorCode
	 * @param errorMsg
	 */
	public ServiceException(int errorCode, String errorMsg) {  
		super(errorCode, errorMsg);  
	}  
	/**
	 * 
	 * <p>Title: ServiceException </p>
	 * <p>Description: Constructor </p> 
	 * @param errorMsg
	 */
	public ServiceException(String errorMsg) {  
		super(errorMsg);  
	}  
	/**
	 * 
	 * <p>Title: ServiceException </p>
	 * <p>Description: Constructor </p> 
	 * @param cause
	 * @param errorCode
	 * @param errorMsg
	 * @param traceId
	 */
	public ServiceException(Throwable cause, int errorCode, String errorMsg,  
			String traceId) {  
		super(cause, errorCode, errorMsg, traceId);  
	}  
	/**
	 * 
	 * <p>Title: ServiceException </p>
	 * <p>Description: Constructor </p> 
	 * @param cause
	 * @param errorCode
	 * @param errorMsg
	 */
	public ServiceException(Throwable cause, int errorCode, String errorMsg) {  
		super(cause, errorCode, errorMsg);  
	}  
	/**
	 * 
	 * <p>Title: ServiceException </p>
	 * <p>Description: Constructor </p> 
	 * @param cause
	 * @param errorMsg
	 */
	public ServiceException(Throwable cause, String errorMsg) {  
		super(cause, errorMsg);  
	}  
	/**
	 * 
	 * <p>Title: ServiceException </p>
	 * <p>Description: Constructor </p> 
	 * @param cause
	 */
	public ServiceException(Throwable cause) {  
		super(cause);  
	}  

}
