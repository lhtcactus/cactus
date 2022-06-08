package com.cactus.core.toolkit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * bean manager
 * @author lht
 * @since 2022/6/2 9:52 下午
 */
@SuppressWarnings("unchecked")
public class BeanManager {
    private static final ConcurrentHashMap<Class<?>,Object> BEAN_MAP = new ConcurrentHashMap<>(16);

    /**
     * set bean
     * @param  key class
     * @param  val bean instance
     * @since  2022/6/2 10:03 下午
     * @author lht
     */
    public static <T> void setBean(Class<T> key,T val){
        BEAN_MAP.computeIfAbsent(key,key1 -> val);
    }


    /**
     * get bean instance
     * @param  key class
     * @since  2022/6/2 10:08 下午
     * @author lht
     */
    public static <T> T getBean(Class<T> key){
        return (T)BEAN_MAP.get(key);
    }
}
