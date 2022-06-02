package com.cactus.core.toolkit;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author lht
 * @date 2021/6/25 10:03 上午
 */
@FunctionalInterface
public interface SerializableFuncrion<T,R> extends Function<T,R>, Serializable {

}
