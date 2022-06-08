package com.cactus.core.exception;


/**
 * 定义全局异常
 * @author lht
 * @since 2021/10/13 10:48 上午
 */
public class GlobalException extends RuntimeException{
    /**
     * 错误码
     */
    protected int code;
    /**
     * 错误信息
     */
    protected String resultMsg;

    public GlobalException(){
        this.code = ErrorCode.DEFAULT_ERROR_CODE;
        this.resultMsg = ErrorCode.DEFAULT_ERROR_MSG;
    }
    public GlobalException(String resultMsg) {
        super(resultMsg);
        this.code = ErrorCode.DEFAULT_ERROR_CODE;
        this.resultMsg = resultMsg;
    }


    public GlobalException(String resultMsg, String message) {
        super(message);
        this.code = ErrorCode.DEFAULT_ERROR_CODE;
        this.resultMsg = resultMsg;
    }

    public GlobalException(String resultMsg, Throwable cause) {
        super(resultMsg,cause);
        this.code = ErrorCode.DEFAULT_ERROR_CODE;
        this.resultMsg = resultMsg;
    }

    public GlobalException(String resultMsg, String message, Throwable cause) {
        super(message, cause);
        this.code = ErrorCode.DEFAULT_ERROR_CODE;
        this.resultMsg = resultMsg;
    }

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.resultMsg = errorCode.getMessage();
    }

    public GlobalException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
        this.resultMsg = errorCode.getMessage();
    }


    public GlobalException(int code, String resultMsg) {
        super(resultMsg);
        this.code = code;
        this.resultMsg = resultMsg;
    }

    public GlobalException(int code, String resultMsg, String message) {
        super(message);
        this.code = code;
        this.resultMsg = resultMsg;
    }

    public GlobalException(int code, String resultMsg, Throwable cause) {
        super(resultMsg,cause);
        this.code = code;
        this.resultMsg = resultMsg;
    }

    public GlobalException(int code, String resultMsg, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.resultMsg = resultMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
