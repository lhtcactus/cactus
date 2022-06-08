package com.cactus.core.toolkit;

import org.slf4j.helpers.MessageFormatter;

/**
 * log工具
 * @author lht
 * @since 2021/5/21 15:25
 */
public class LogUtil {
    /**
     * 格式化log使用，用法与log.info一致
     * @author lht
     * @since  2021/5/21 15:34
     * @param  format 需要格式化的串
     * @param  argArray 参数
     */
    public static String log(String format, Object... argArray) {
        return MessageFormatter.arrayFormat(format, argArray).getMessage();
    }
}
