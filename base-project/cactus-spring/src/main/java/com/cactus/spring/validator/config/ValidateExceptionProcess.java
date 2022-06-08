package com.cactus.spring.validator.config;

import com.cactus.core.exception.GlobalException;
import com.cactus.spring.log.exception.process.ExceptionProcess;
import com.cactus.spring.validator.exception.ValidateException;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lht
 * @since 2022/2/16 2:00 下午
 */
@Configuration
public class ValidateExceptionProcess implements ExceptionProcess<ValidateException> {
    @Override
    public Exception process(HttpServletRequest request, ValidateException t) {
        return new GlobalException(t.getResultMsg());
    }

    @Override
    public boolean support(Throwable t) {
        return t instanceof ValidateException;
    }

    @Override
    public int order() {
        return 0;
    }
}
