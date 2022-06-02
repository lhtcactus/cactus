package com.cactus.redis.config;

import com.cactus.redis.lock.feign.FeignRedisLockInterceptor;
import com.cactus.redis.lock.feign.FeignTransferRedisLockMvcConfig;
import com.cactus.redis.lock.feign.hystrix.RedisLockHystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import feign.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁传递与feign相关
 * @author lht
 * @since 2022/2/15 4:29 下午
 */
@Configuration
@ConditionalOnClass(Client.class)
@ConditionalOnProperty(prefix = "cactus.redis",
        name = {"feign_enable"},
        havingValue = "true",
        matchIfMissing = true)
public class FeignConfigAuto {

    @Bean
    @ConditionalOnClass({HystrixConcurrencyStrategy.class})
    public RedisLockHystrixConcurrencyStrategy redisLockHystrixConcurrencyStrategy(){
        return new RedisLockHystrixConcurrencyStrategy();
    }

    @Bean
    public FeignRedisLockInterceptor feignRedisLockInterceptor(){
        return new FeignRedisLockInterceptor();
    }

    @Bean
    public FeignTransferRedisLockMvcConfig feignTransferRedisLockMvcConfig(){
        return new FeignTransferRedisLockMvcConfig();
    }

}
