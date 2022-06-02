package com.cactus.core.toolkit;

import cn.hutool.core.lang.SimpleCache;
import com.cactus.core.exception.GlobalException;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * Lambda utils
 *
 * @author lht
 * @date 2021/6/24 5:53 下午
 */
public class LambdaUtil {
    /**
     * SerializedLambda after cache resolution
     */
    private final static SimpleCache<String, SerializedLambda> FUNC_CACHE = new SimpleCache<>();

    /**
     * parsing lamdba function get SerializedLambda
     *
     * @param func lambda function
     * @author lht
     * @date 2021/6/25 10:31 上午
     */
    public static SerializedLambda resolve(SerializableFuncrion<?, ?> func) {
        Class<?> clazz = func.getClass();
        SerializedLambda serializedLambda = FUNC_CACHE.get(clazz.getName());
        if(serializedLambda==null){
            try {
                Method method = func.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(true);
                serializedLambda = (SerializedLambda) method.invoke(func);
                FUNC_CACHE.put(clazz.getName(),serializedLambda);
            } catch (Exception e) {
                throw new GlobalException("lambda解析失败", e);
            }
        }
        return serializedLambda;
    }

}
