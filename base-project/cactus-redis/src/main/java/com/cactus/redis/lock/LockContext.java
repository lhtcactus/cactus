package com.cactus.redis.lock;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * redis lock context
 * @author lht
 * @since 2021/10/19 2:01 下午
 */
public class LockContext {
    private static final ThreadLocal<Set<String>> LOCK_THREAD = ThreadLocal.withInitial(HashSet::new);

    /**
     * redis lock sign
     */
    public static final String LOCK_CONTEXT_TRANSFER_KEY = "cactus-redis-lock-transfer";

    /**
     * 是否已经加锁
     * @author lht
     * @since  2020/12/17 14:23
     * @param key lock  key
     */
    public static boolean isLock(String key){
        return LOCK_THREAD.get().contains(key);
    }


    /**
     * 批量记录lock
     * @author lht
     * @since  2020/12/17 15:50
     * @param keysReq lock keys
     */
    public static void recordLock(Collection<String> keysReq){
        if(!CollectionUtils.isEmpty(keysReq)){
            Set<String> keys = LOCK_THREAD.get();
            keys.addAll(keysReq);
        }
    }
    /**
     * 记录lock
     * @author lht
     * @since  2020/12/17 15:42
     * @param key lock key
     */
    public static void recordLock(String key){
        if(StringUtils.hasLength(key)){
            Set<String> keys = LOCK_THREAD.get();
            keys.add(key);
        }
    }


    /**
     * 获取当前上下文
     * @author lht
     * @since  2020/12/17 14:34
     */
    public static Set<String> getRecordedKeys(){
        return LOCK_THREAD.get();
    }



    /**
     * 清除当前lockid 的 key  lock
     * @author lht
     * @since  2020/12/17 16:15
     * @param key lock key
     */
    public static void remove(String key){
        Set<String> keys = LOCK_THREAD.get();
        keys.remove(key);
    }
    /**
     * 清除当前lockid lock
     * @author lht
     * @since  2020/12/17 14:46
     */
    public static void removeAll(){
        LOCK_THREAD.remove();
    }
}
