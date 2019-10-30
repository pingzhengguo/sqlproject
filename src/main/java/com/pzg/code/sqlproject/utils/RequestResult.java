package com.pzg.code.sqlproject.utils;

/**
 * 请求返回参数
 */
public class RequestResult<E> {

    /**
     * 请求是否成功
     */
    private boolean success;
    /**
     * 响应错误信息
     */
    private String errorMsg;
    /**
     * 响应状态码(本项目自定义，非http状态码。暂时不用)
     */
    private Integer statusCode;
    /**
     * 响应内容
     */
    private E result;

    public RequestResult() {
    }

    public RequestResult(boolean success, String errorMsg, E result) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.result = result;
    }

    public RequestResult(boolean success, E result) {
        this.success = success;
        this.result = result;
    }

    public RequestResult(boolean success, String errorMsg) {
        this.success = success;
        this.errorMsg = errorMsg;
    }

    public RequestResult(boolean success, String errorMsg, Integer statusCode, E result) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.statusCode = statusCode;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }
}
