package com.cactus.spring.log.format;

import com.cactus.spring.log.CactusLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lht
 * @since 2022/3/31 9:22 下午
 */
@Slf4j
@Service
@ConditionalOnMissingBean(LogFormat.class)
public class DefaultLogFormat implements LogFormat {
    @Override
    public void log(HttpServletRequest request, CactusLog cactusLog) {
        if (cactusLog.getSuccess()) {
            log.info(cactusLog.log());
        } else {
            log.error(cactusLog.log(), cactusLog.getThrowable());
        }
    }
}
