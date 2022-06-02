package com.cactus.core.exception;

/**
 * 错误信息编码接口
 * @author lht
 * @since 2022/2/15 2:13 下午
 */
public interface ErrorCode {
    /**
     * 默认成功返回code
     */
    int DEFAULT_SUCCESS_CODE = 200;
    /**
     * 默认失败返回code
     */
    int DEFAULT_ERROR_CODE = 500;
    /**
     * 操作成功默认返回消息
     */
    String DEFAULT_SUCCESS_MSG = "操作成功";
    /**
     * 操作失败默认返回消息
     */
    String DEFAULT_ERROR_MSG = "服务器异常";
    /**
     * 获取错误编码
     * @since  2022/2/15 2:14 下午
     * @author lht
     */
    int getCode();

    /**
     * 获取错误信息
     * @since  2022/2/15 2:14 下午
     * @author lht
     */
    String getMessage();
}
