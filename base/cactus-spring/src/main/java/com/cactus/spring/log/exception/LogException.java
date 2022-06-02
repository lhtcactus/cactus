package com.cactus.spring.log.exception;

/**
 * 日志处理后的异常
 * @author lht
 * @since 2022/2/17 11:05 上午
 */
public class LogException extends RuntimeException{
    private final Throwable cause;

    public LogException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
