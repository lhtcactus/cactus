package com.cactus.spring.log;

import com.cactus.core.CactusConstant;
import com.cactus.spring.log.exception.LogException;
import com.cactus.spring.log.exception.process.ExceptionProcess;
import com.cactus.spring.log.format.LogFormat;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lht
 * @since 2022/2/16 5:00 下午
 */
@Slf4j
@SuppressWarnings("unchecked")
public class SimpleLogInterceptor implements MethodInterceptor {

    private final List<ExceptionProcess> exceptionProcesses;

    private final LogFormat logFormat;
    public SimpleLogInterceptor(List<ExceptionProcess> exceptionProcesses, LogFormat logFormat) {
        this.exceptionProcesses = exceptionProcesses;
        this.logFormat = logFormat;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        //get request
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        if(sra==null){
            log.warn("log ServletRequestAttributes is null, request not find!");
            return methodInvocation.proceed();
        }
        HttpServletRequest request = sra.getRequest();

        //set request log
        CactusLog cactusLogInfo = new CactusLog();
        cactusLogInfo.setRequestId(request.getHeader(CactusConstant.REQUEST_ID));
        cactusLogInfo.setStartTime(LocalDateTime.now());
        cactusLogInfo.setUrl(request.getRequestURI());
        cactusLogInfo.setMethod(request.getMethod());
        cactusLogInfo.setSuccess(true);

        try {
            List<Object> params = Stream.of(methodInvocation.getArguments())
                    .filter(obj-> !(obj instanceof HttpServletRequest||obj instanceof HttpServletResponse))
                    .collect(Collectors.toList());
            cactusLogInfo.setParams(params);
            return methodInvocation.proceed();
        }catch (Exception e) {
            //执行对exception的特殊处理
            for (ExceptionProcess process : exceptionProcesses) {
                if(process.support(e)){
                    cactusLogInfo.setThrowable(process.process(request,e));
                    break;
                }
            }
            cactusLogInfo.setSuccess(false);
            throw new LogException(cactusLogInfo.getThrowable());
        } finally {
            cactusLogInfo.setEndTime(LocalDateTime.now());
            logFormat.log(request,cactusLogInfo);
        }
    }
}
