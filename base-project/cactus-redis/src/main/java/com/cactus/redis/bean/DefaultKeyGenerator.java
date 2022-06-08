package com.cactus.redis.bean;


import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;

/**
 *
 * 默认规则
 *
 * @since  2022/2/15 3:06 下午
 * @author lht
 */
public class DefaultKeyGenerator implements KeyGenerator {
    SimpleKeyGenerator simpleKeyGenerator = new SimpleKeyGenerator();

    @Override
    public Object generate(Object target, Method method, Object... params) {

        return  simpleKeyGenerator.generate(target,method,params);
    }
}
