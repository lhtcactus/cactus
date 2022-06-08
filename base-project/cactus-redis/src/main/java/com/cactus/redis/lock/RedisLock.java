package com.cactus.redis.lock;

import cn.hutool.core.util.IdUtil;
import com.cactus.core.exception.GlobalException;
import com.cactus.redis.config.LockProperties;
import com.cactus.spring.utils.SpringContextUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis lock util
 * @author lht
 * @since 2021/10/19 1:57 下午
 */
public class RedisLock {
    /**
     * 存放Rlock,用于解锁使用
     */
    private static final ThreadLocal<Map<String, RLock>> LOCK_THREAD = ThreadLocal.withInitial(HashMap::new);
    /**
     * redisson
     */
    private static final Redisson REDISSON = SpringContextUtil.getBean(Redisson.class);
    /**
     * lock 需要的一些配置参数
     */
    private static final LockProperties LOCK_PROPERTIES =  SpringContextUtil.getBean(LockProperties.class);

    /**
     * 批量加锁
     * @param  keys lock keys
     * @since  2021/10/19 1:43 下午
     * @author lht
     * @return 解锁的keys
     */
    public static String multiLock(Collection<String> keys){
        String unkey = IdUtil.simpleUUID();
        if(!tryMultiLock(LOCK_PROPERTIES.getDefaultPrefixKey(),unkey,keys)){
            throwException();
        }
        return unkey;
    }

    /**
     * 批量加锁
     * @param  prefixKey key 前缀
     * @param  keys 解锁的keys
     * @since  2021/10/19 1:45 下午
     * @author lht
     */
    public static String multiLock(String prefixKey,Collection<String> keys){
        String unkey = IdUtil.simpleUUID();
        if(!tryMultiLock(prefixKey,unkey,keys)){
            throwException();
        }
        return unkey;
    }


    /**
     * 批量循环加锁
     * @param  keys lock keys
     * @since  2021/10/19 1:45 下午
     * @author lht
     */
    public static void lock(Collection<String> keys){
        if(!tryLock(LOCK_PROPERTIES.getDefaultPrefixKey(),keys)){
            throwException();
        }
    }

    /**
     * 批量循环加锁
     * @param  prefixKey keys 前缀
     * @param  keys lock keys
     * @since  2021/10/19 1:45 下午
     * @author lht
     */
    public static void lock(String prefixKey,Collection<String> keys){
        if(!tryLock(prefixKey,keys)){
            throwException();
        }
    }

    /**
     * 单独加锁
     * @param  key key
     * @since  2021/10/19 1:46 下午
     * @author lht
     */
    public static void lock(String key){
        if(!tryLock(LOCK_PROPERTIES.getDefaultPrefixKey(),key)){
            throwException();
        }
    }

    /**
     * 单独加锁
     * @param  prefixKey key 前缀
     * @param  key key
     * @since  2021/10/19 1:46 下午
     * @author lht
     */
    public static void lock(String prefixKey,String key){
        if(!tryLock(prefixKey,key)){
            throwException();
        }
    }

    /**
     * 批量循环加锁 实现
     * @param  prefixKey key前缀
     * @param  keys lock keys
     * @since  2021/10/19 1:47 下午
     * @author lht
     */
    private static boolean tryLock(String prefixKey,Collection<String> keys){
        if(!CollectionUtils.isEmpty(keys)){
            boolean isError = false;
            for(String key:keys){
                if(!tryLock(prefixKey,key)){
                    isError = true;
                    break;
                }
            }
            //如果有错误，将本次key全部释放
            if(isError){
                //释放
                unlock(keys);
                return false;
            }
        }
        return true;
    }


    /**
     * 批量加锁 实现
     * @param  prefixKey key前缀
     * @param  unkey unlock key sign
     * @param  keys lock keys
     * @since  2021/10/19 1:48 下午
     * @author lht
     */
    private static boolean tryMultiLock(String prefixKey,String unkey,Collection<String> keys){
        if(!CollectionUtils.isEmpty(keys)){
            List<RLock> list = new ArrayList<>();
            //先记录下来
            List<String> recordKeys = new ArrayList<>(keys.size());
            for(String key:keys){
                String temp = prefixKey+ key;
                //没有加锁
                if(!LockContext.isLock(temp)){
                    //执行加锁
                    list.add(REDISSON.getLock(LOCK_PROPERTIES.getLockPath()+temp));
                    //记录锁
                    recordKeys.add(temp);
                }
            }
            //如果全部已经加锁，直接返回true
            if(!list.isEmpty()){
                RLock[] rLocks = new RLock[list.size()];
                list.toArray(rLocks);
                RLock rLock = REDISSON.getRedLock(rLocks);
                //确定lock执行成功后，记录到context
                if(doTryLock(unkey,rLock)){
                    //批量记录lock
                    LockContext.recordLock(recordKeys);
                    //记录red lock
                    LockContext.recordLock(unkey);
                }else{
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 单独加锁 实现
     * @param  prefixKey key前缀
     * @param  key lock key
     * @since  2021/10/19 1:51 下午
     * @author lht
     */
    public static boolean tryLock(String prefixKey,String key){
        String temp = prefixKey+key;
        //已经加锁
        if(LockContext.isLock(temp)){
            return true;
        }
        RLock rLock = REDISSON.getLock(LOCK_PROPERTIES.getLockPath()+temp);
        if(doTryLock(temp,rLock)){
            //记录lock
            LockContext.recordLock(temp);
            return true;
        }else{
            return false;
        }
    }



    /**
     * 批量循环解锁
     * @param  keys unlock keys
     * @since  2021/10/19 1:52 下午
     * @author lht
     */
    public static boolean unlock(Collection<String> keys){
        return unlock(LOCK_PROPERTIES.getDefaultPrefixKey(),keys);
    }

    /**
     * 批量循环解锁
     * @param  prefixKey key前缀
     * @param  keys unlock keys
     * @since  2021/10/19 1:52 下午
     * @author lht
     */
    public static boolean unlock(String prefixKey,Collection<String> keys){
        boolean boo = true;
        for (String key : keys) {
            if(!unlock(prefixKey,key)){
                boo = false;
            }
        }
        return boo;
    }


    /**
     * 单个解锁
     * @param  key unlock key
     * @since  2021/10/19 1:52 下午
     * @author lht
     */
    public static boolean unlock(String key){
        return unlock(LOCK_PROPERTIES.getDefaultPrefixKey(),key);
    }

    /**
     * 单个解锁
     * @param  prefixKey key前缀
     * @param  key unlock key
     * @since  2021/10/19 1:53 下午
     * @author lht
     */
    public static boolean unlock(String prefixKey,String key){
        Map<String,RLock> lockMap = LOCK_THREAD.get();
        String temp = prefixKey+key;
        RLock lock = lockMap.get(temp);
        if(lock != null){
            try{
                lock.unlock();
            }catch (Exception e){
                return false;
            }finally {
                lockMap.remove(temp);
                LockContext.remove(temp);
            }
        }
        return true;
    }



    /**
     * 解锁当前线程所有key
     * @since  2021/10/19 1:54 下午
     * @author lht
     */
    public static boolean unlockAll(){
        Map<String,RLock> lockMap = LOCK_THREAD.get();
        for (RLock lock : lockMap.values()) {
            try{
                lock.unlock();
            }catch (Exception e){
                return false;
            }finally {
                LOCK_THREAD.remove();
            }
        }
        LockContext.removeAll();
        return true;
    }

    /**
     * 尝试加锁 加锁核心实现
     * @param  key lock key
     * @param  rLock RLock
     * @since  2021/10/19 1:54 下午
     * @author lht
     */
    private static boolean doTryLock(String key,RLock rLock){
        try {
            boolean lockSuccess;
            //如果leaseTime小于等于0 则保证锁持续持有
            if(LOCK_PROPERTIES.getLeaseTime()<=0){
                lockSuccess = rLock.tryLock(LOCK_PROPERTIES.getWaitTime(), TimeUnit.MILLISECONDS);
            }else{
                lockSuccess = rLock.tryLock(LOCK_PROPERTIES.getWaitTime(), LOCK_PROPERTIES.getLeaseTime(), TimeUnit.MILLISECONDS);
            }
            if (lockSuccess) {
                Map<String, RLock> rLockMap = LOCK_THREAD.get();
                rLockMap.put(key, rLock);
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    /**
     * 抛出锁失败异常
     * @since  2021/10/19 1:42 下午
     * @author lht
     */
    private static void throwException(){
        throw new GlobalException("redis lock error");
    }
}
