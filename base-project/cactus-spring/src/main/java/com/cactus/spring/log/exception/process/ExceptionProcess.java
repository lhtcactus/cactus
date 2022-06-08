package com.cactus.spring.log.exception.process;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lht
 * @since 2021/11/9 10:24 下午
 */
public interface ExceptionProcess<T extends Exception> {
    /**
     * 处理特殊的异常
     * @param  t 捕捉的异常
     * @since  2021/11/9 10:25 下午
     * @author lht
     */
    Exception process(HttpServletRequest request,T t);
    /**
     * 是否支持
     * @param  t 捕捉的异常
     * @since  2021/11/9 10:28 下午
     * @author lht
     */
    boolean support(Throwable t);
    /**
     * 排序，从小向大排列，第一个被处理的不在向后继续
     * @since  2021/11/9 10:27 下午
     * @author lht
     */
    int order();
}
