package com.cactus.core.result;

import lombok.Data;

import java.util.Collection;

/**
 * 统一返回
 * @author lht
 * @since 2021/10/20 4:16 下午
 */
@Data
public class Res<T> {
    /**
     * 返回状态码
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 数据体
     */
    private T data;
}
