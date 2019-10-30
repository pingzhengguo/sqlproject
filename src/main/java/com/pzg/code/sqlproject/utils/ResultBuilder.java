package com.pzg.code.sqlproject.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * Title: ResultBuilder
 * Description: 返回值的类
 *
 * =================================================================
 *    创建文件,实现基本功能
 * =================================================================
 */
@Data
public class ResultBuilder<T> implements Serializable {

    private static final long serialVersionUID = -602413775193033896L;
    private static final String MSG_SUCCESS = "success";
    private static final String MSG_FAIL = "failure";
    private static final String MSG_BASE_EXCEPTION = "服务端发生自定义异常，信息如下: ";
    private static final String MSG_OTHER_EXCEPTION = "服务端发生非自定义异常，信息如下: ";
    private static final int CODE_SUCCESS = 1;
    private static final int CODE_FAIL = 0;

    private int code;
    private String msg;
    private T result;

    private ResultBuilder() {
    }

    private static <T> ResultBuilder<T> resultBuilder(int code, String msg, T result) {
        ResultBuilder<T> resultBuilder = new ResultBuilder<>();
        resultBuilder.code = code;
        resultBuilder.msg = msg;
        resultBuilder.result = result;
        return resultBuilder;
    }

    public static <T> ResultBuilder<T> success(T result) {
        return resultBuilder(CODE_SUCCESS, MSG_SUCCESS, result);
    }

    public static <T> ResultBuilder<T> success() {
        return success(null);
    }

    public static <T> ResultBuilder<T> fail(T result) {
        return resultBuilder(CODE_FAIL, MSG_FAIL, result);
    }

    public static <T> ResultBuilder <T> fail() {
        return fail(null);
    }

}
