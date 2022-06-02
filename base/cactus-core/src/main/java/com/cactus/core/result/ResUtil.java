package com.cactus.core.result;

import com.cactus.core.exception.ErrorCode;


/**
 * 统一返回工具类
 *
 * @author lht
 * @since 2021/10/20 4:24 下午
 */
public class ResUtil {
    public static <T> Res<T> success() {
        Res<T> res = new Res<>();
        res.setCode(ErrorCode.DEFAULT_SUCCESS_CODE);
        res.setMessage(ErrorCode.DEFAULT_SUCCESS_MSG);
        return res;
    }

    public static <T> Res<T> success(String message) {
        Res<T> res = new Res<>();
        res.setCode(ErrorCode.DEFAULT_SUCCESS_CODE);
        res.setMessage(message);
        return res;
    }

    public static <T> Res<T> success(T data) {
        return success(data, ErrorCode.DEFAULT_SUCCESS_MSG);
    }

    public static <T> Res<T> success(T data, String message) {
        return success(ErrorCode.DEFAULT_SUCCESS_CODE, data, message);
    }


    public static <T> Res<T> success(int code, T data, String message) {
        Res<T> res = new Res<>();
        res.setCode(code);
        res.setMessage(message);
        res.setData(data);
        return res;
    }


    public static <T> Res<T> error() {
        Res<T> res = new Res<>();
        res.setCode(ErrorCode.DEFAULT_ERROR_CODE);
        res.setMessage(ErrorCode.DEFAULT_ERROR_MSG);
        return res;
    }
    public static <T> Res<T> error(String message) {
        Res<T> res = new Res<>();
        res.setCode(ErrorCode.DEFAULT_ERROR_CODE);
        res.setMessage(message);
        return res;
    }

    public static <T> Res<T> error(int code) {
        Res<T> res = new Res<>();
        res.setCode(code);
        res.setMessage(ErrorCode.DEFAULT_ERROR_MSG);
        return res;
    }

    public static <T> Res<T> error(int code, String message) {
        Res<T> res = new Res<>();
        res.setCode(code);
        res.setMessage(message);
        return res;
    }

    public static <T> Res<T> error(ErrorCode errorCode) {
        Res<T> res = new Res<>();
        res.setCode(errorCode.getCode());
        res.setMessage(errorCode.getMessage());
        return res;
    }


}
