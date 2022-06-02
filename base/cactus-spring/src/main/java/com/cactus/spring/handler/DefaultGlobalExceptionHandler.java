package com.cactus.spring.handler;

import com.cactus.core.CactusConstant;
import com.cactus.core.exception.GlobalException;
import com.cactus.core.handler.GlobalExceptionHandler;
import com.cactus.core.result.Res;
import com.cactus.core.result.ResUtil;
import com.cactus.spring.log.CactusLog;
import com.cactus.spring.log.exception.LogException;
import com.cactus.spring.log.format.LogFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 *
 * @author lht
 * @since 2021/11/2 1:48 下午
 */
@ConditionalOnProperty(prefix = "cactus.global.handle",
        name="enable",
        havingValue = "true",
        matchIfMissing = true)
@ConditionalOnMissingBean(GlobalExceptionHandler.class)
@ControllerAdvice
@Slf4j
public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {

    @Autowired
    private LogFormat logFormat;

    @ExceptionHandler(value = LogException.class)
    @ResponseBody
    public Res<String> logException(HttpServletRequest req, HttpServletResponse response, LogException e){
        response.setStatus(200);

        if(e.getCause() instanceof GlobalException){
            return ResUtil.error(((GlobalException) e.getCause()).getResultMsg());
        }
        return ResUtil.error(e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Res<String> error(HttpServletRequest req, HttpServletResponse response, Exception e){
        //打印日志
        CactusLog cactusLogInfo = new CactusLog();
        cactusLogInfo.setRequestId(req.getHeader(CactusConstant.REQUEST_ID));
        cactusLogInfo.setErrorTime(LocalDateTime.now());
        cactusLogInfo.setUrl(req.getRequestURI());
        cactusLogInfo.setMethod(req.getMethod());
        cactusLogInfo.setSuccess(false);
        cactusLogInfo.setThrowable(e);
        logFormat.log(req,cactusLogInfo);

        response.setStatus(200);
        if(e instanceof GlobalException){
            return ResUtil.error(((GlobalException) e).getResultMsg());
        }
        return ResUtil.error(e.getMessage());
    }
}
