package com.cactus.spring.validator.config;

import com.cactus.core.exception.GlobalException;
import com.cactus.spring.utils.SpringProxyUtil;
import com.cactus.spring.validator.ValidateMethodInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

/**
 * Validator Interceptor Register
 * @author lht
 * @since 2021/4/22 11:40
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "cactus.validate",
        name="enable",
        havingValue = "true",
        matchIfMissing = true)
public class ValidatorInterceptorRegister extends AbstractAutoProxyCreator implements  ApplicationContextAware {
    private static final Set<String> PROXYED_SET = new HashSet<>();

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
                Validated validated = serviceInterface.getAnnotation(Validated.class);
                if(validated == null){
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
        return new Object[]{new ValidateMethodInterceptor()};
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.setBeanFactory(applicationContext);
    }
}
