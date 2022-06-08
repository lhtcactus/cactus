package com.cactus.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lht
 * @since 2021/10/19 3:56 下午
 */

@Configuration
@ConfigurationProperties("cactus.redis.lock")
public class LockProperties {
    /**
     * 等待获取锁时间
     */
    private Long waitTime = 3000L;
    /**
     * 保存锁时间
     */
    private Long leaseTime = 10000L;
    /**
     * redis lock path
     */
    private String lockPath = "redisson:lock:";
    /**
     * default key prefix
     */
    private String defaultPrefixKey = "";

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public Long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(Long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public String getLockPath() {
        return lockPath;
    }

    public void setLockPath(String lockPath) {
        this.lockPath = lockPath;
    }

    public String getDefaultPrefixKey() {
        return defaultPrefixKey;
    }

    public void setDefaultPrefixKey(String defaultPrefixKey) {
        this.defaultPrefixKey = defaultPrefixKey;
    }
}
