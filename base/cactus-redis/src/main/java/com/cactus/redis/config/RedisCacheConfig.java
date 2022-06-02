package com.cactus.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.cactus.redis.bean.DefaultKeyGenerator;
import com.cactus.redis.bean.EmptyKeyGenerator;
import com.cactus.redis.bean.IgnoreExceptionCacheErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * 缓存
 * @since  2021/10/19 1:07 下午
 * @author lht
 */
@ConditionalOnProperty(prefix = "cactus.redis",
        name = {"cache_enable"},
        havingValue = "true",
        matchIfMissing = true)
@ConfigurationProperties("spring.redis")
@EnableCaching
@CacheConfig(keyGenerator="keyGenerator",cacheManager = "redisCacheManager")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@SuppressWarnings("all")
public class RedisCacheConfig extends CachingConfigurerSupport {
    @Value("${mapper.cache.timeout:10}")
    private Long mybatisCacheTimeOut;

    @Value("${mapper.cache.prefix.name:'mapper:cache:'}")
    private String prefixCacheNameWith;

    /**
     * 自定义cacheManager
     * @param  connectionFactory 连接工厂
     * @since  2022/2/15 3:08 下午
     * @author lht
     */
    @Bean
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisSerializationContext.SerializationPair serializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(getRedisSerializer());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(prefixCacheNameWith)
                .entryTtl(Duration.ofSeconds(mybatisCacheTimeOut))
                .serializeValuesWith(serializationPair);
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }


    @Override
    public CacheErrorHandler errorHandler() {
        return new IgnoreExceptionCacheErrorHandler();
    }


    @Override
    @Bean("keyGenerator")
    public KeyGenerator keyGenerator() {
        return new DefaultKeyGenerator();
    }

    @Bean("emptyKeyGenerator")
    public EmptyKeyGenerator emptyKeyGenerator(){
        return new EmptyKeyGenerator();
    }

    private RedisSerializer<Object> getRedisSerializer(){
        return new GenericFastJsonRedisSerializer();
    }
}
