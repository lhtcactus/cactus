package com.cactus.spring.log.exception.process;

import com.cactus.core.exception.GlobalException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认异常处理
 * @author lht
 * @since 2022/2/16 1:50 下午
 */
@Component
public class DefaultExceptionProcess implements ExceptionProcess<Exception>{
    @Override
    public Exception process(HttpServletRequest request,Exception t) {
        if(t instanceof GlobalException){
            return t;
        }
        String msg = t.getMessage();
        msg = StringUtils.hasLength(msg)?msg:"操作异常，请联系管理员！";
        return new GlobalException(msg,t);
    }

    @Override
    public boolean support(Throwable t) {
        return true;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
