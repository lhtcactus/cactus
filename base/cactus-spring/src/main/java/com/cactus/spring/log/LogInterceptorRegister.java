package com.cactus.spring.log;

import com.cactus.core.exception.GlobalException;
import com.cactus.spring.log.exception.process.ExceptionProcess;
import com.cactus.spring.log.format.LogFormat;
import com.cactus.spring.utils.SpringProxyUtil;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator Interceptor Register
 * @author lht
 * @since 2021/4/22 11:40
 */
@Order(2)
@AutoConfigureOrder(2)
@Configuration
@ConditionalOnProperty(prefix = "cactus.log",
        name="enable",
        havingValue = "true",
        matchIfMissing = true)
public class LogInterceptorRegister extends AbstractAutoProxyCreator implements  ApplicationContextAware {
    private static final Set<String> PROXYED_SET = new HashSet<>();

    @Autowired
    private List<ExceptionProcess> exceptionProcesses;

    @Autowired
    private LogFormat logFormatService;

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        try{
            synchronized (PROXYED_SET) {
                if (PROXYED_SET.contains(beanName)) {
                    return bean;
                }
                //判断是否需要拦截
                Class<?> serviceInterface = SpringProxyUtil.findTargetClass(bean);
                if(serviceInterface==null){
                    return bean;
                }
                RestController restController = serviceInterface.getAnnotation(RestController.class);
                Controller controller = serviceInterface.getAnnotation(Controller.class);
                if(restController == null&&controller==null){
                   return bean;

                }
                if (!AopUtils.isAopProxy(bean)) {
                    bean = super.wrapIfNecessary(bean, beanName, cacheKey);
                } else {
                    AdvisedSupport advised = SpringProxyUtil.getAdvisedSupport(bean);
                    Advisor[] advisor = buildAdvisors(beanName, getAdvicesAndAdvisorsForBean(null, null, null));
                    for (Advisor avr : advisor) {
                        advised.addAdvisor(0, avr);
                    }
                }
                PROXYED_SET.add(beanName);
                return bean;

            }
        }catch (Exception e){
            throw new GlobalException("validator register error",e);
        }
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[]{new SimpleLogInterceptor(exceptionProcesses.stream()
                .sorted(Comparator.comparing(ExceptionProcess::order))
                .collect(Collectors.toList()),
                logFormatService)};
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.setBeanFactory(applicationContext);
    }
}
