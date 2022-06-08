package com.cactus.spring.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringContextUtil
 * @author lht
 * @since 2020/9/30 17:12
 */
@Component("springContextUtil")
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext appCtx;

    /**
     * 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量。
     *
     * @param applicationContext ApplicationContext 对象.
     * @throws BeansException
     * @author wangdf
     */
    @Override
    public void setApplicationContext( ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.appCtx = applicationContext;
    }
    /**
     * 获取ApplicationContext
     *
     * @return
     * @author wangdf
     */
    public static ApplicationContext getApplicationContext() {
        return appCtx;
    }

    /**
     * 这是一个便利的方法，帮助我们快速得到一个BEAN
     *
     * @param beanName bean的名字
     * @return 返回一个bean对象
     * @author wangdf
     */
    public static Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }
    public static <T> T getBean(Class<T> beanName) {
        return appCtx.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> aClass) {
        return appCtx.getBean(beanName, aClass);
    }
}
