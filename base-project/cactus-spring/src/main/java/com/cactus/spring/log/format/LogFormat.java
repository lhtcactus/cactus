package com.cactus.spring.log.format;


import com.cactus.spring.log.CactusLog;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lht
 * @since 2022/3/31 8:50 下午
 */

public interface LogFormat {
    /**
     * 打印日志
     * @param  request HttpServletRequest
     * @param  log CactusLog
     * @since  2022/3/31 8:59 下午
     * @author lht
     */
    void log(HttpServletRequest request, CactusLog log);
}
