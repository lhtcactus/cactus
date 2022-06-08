package com.cactus.core.toolkit;

import cn.hutool.core.lang.SimpleCache;
import com.cactus.core.exception.GlobalException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.invoke.SerializedLambda;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Property Utils
 *
 * @author lht
 * @date 2021/6/25 9:39 上午
 */
@SuppressWarnings("all")
public class PropertyUtils {
    /**
     * cache class set/get method PropertyDescriptor
     */
    private final static SimpleCache<String, Map<String, PropertyDescriptor>> PROP_CACHE = new SimpleCache<>();

    /**
     * get PropertyDescriptor used for reflex
     *
     * @param clazz     class
     * @param fieldName field name
     * @author lht
     * @date 2021/6/25 10:25 上午
     */
    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String fieldName) {
        Map<String, PropertyDescriptor> propMap = PROP_CACHE.get(clazz.getName());
        if(propMap==null){
            propMap = new ConcurrentHashMap<>();
            PROP_CACHE.put(clazz.getName(),propMap);
        }

        return propMap.computeIfAbsent(fieldName.toUpperCase(),filename->{
            try {
               return new PropertyDescriptor(fieldName, clazz);
            } catch (IntrospectionException e) {
                throw new GlobalException(error(clazz.getName(), fieldName, "getPropertyDescriptor"));
            }
        });
    }


    /**
     * use field name reflex get method
     *
     * @param obj       reflex object
     * @param fieldName field Name
     * @author lht
     * @date 2021/6/25 10:24 上午
     */
    public static Object get(Object obj, String fieldName) {
        try {
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(obj.getClass(), fieldName);
            return propertyDescriptor.getReadMethod().invoke(obj);
        } catch (Exception e) {
            throw new GlobalException(error(obj.getClass().getName(), fieldName, "get"));
        }
    }

    /**
     * use lambda reflex get method
     *
     * @param obj       reflex object
     * @param sFunction lambda function to feild name
     * @author lht
     * @date 2021/6/25 10:22 上午
     */
    public static <T> Object get(T obj, SerializableFuncrion<T, ?> sFunction) {
        SerializedLambda serializedLambda = LambdaUtil.resolve(sFunction);
        String fieldName = methodToProperty(serializedLambda.getImplMethodName());
        return get(obj, fieldName);
    }


    /**
     * use field name reflex set method
     *
     * @param obj       reflex object
     * @param fieldName field name
     * @param val       set value
     * @author lht
     * @date 2021/6/25 10:22 上午
     */
    public static void set(Object obj, String fieldName, Object val) {
        try {
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(obj.getClass(), fieldName);
            propertyDescriptor.getWriteMethod().invoke(obj, val);
        } catch (Exception e) {
            throw new GlobalException(error(obj.getClass().getName(), fieldName, "set"));
        }
    }

    /**
     * use lambda reflex set method
     *
     * @param obj       reflex object
     * @param sFunction lambda function to feild name
     * @param val       set value
     * @author lht
     * @date 2021/6/25 10:20 上午
     */
    public static <T> void set(T obj, SerializableFuncrion<T, ?> sFunction, Object val) {
        SerializedLambda serializedLambda = LambdaUtil.resolve(sFunction);
        String fieldName = methodToProperty(serializedLambda.getImplMethodName());
        set(obj, fieldName, val);
    }


    /**
     * get or set method to field
     *
     * @param name get or set method name
     * @author lht
     * @date 2021/6/25 10:19 上午
     */
    private static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else {
            if (!name.startsWith("get") && !name.startsWith("set")) {
                throw new GlobalException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
            }

            name = name.substring(3);
        }

        if (name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    /**
     * 组装异常信息
     *
     * @param className  class name
     * @param fieldName  field name
     * @param methodName exec method name
     * @author lht
     * @date 2021/6/25 10:19 上午
     */
    private static String error(String className, String fieldName, String methodName) throws GlobalException {
        return LogUtil.log("class {} , field {}反射{}方法异常"
                , className
                , fieldName
                , methodName
        );
    }

}
