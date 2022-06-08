一些常用框架的简单预设

## cactus-core

1. 常用的工具包：com.cactus.core.tookit
2. 统一返回对象：com.cactus.core.result
3. 全局异常定义：com.cactus.core.exception



## cactus-spring

1. 全局统一异常处理：com.cactus.spring.handler
2. 统一日志处理：com.cactus.spring.log
3. 请求request处理：com.cactus.spring.request
4. spring常用工具包：com.cactus.spring.util
5. validate处理：com.cactus.spring.validator



## cactus-redis

1. 基于Redisson实现的分布式锁：com.cactus.redis.lock
2. 分布式锁传递基于feign和dubbo：com.cactus.redis.lock.feign、com.cactus.redis.lock.dubbo、com.cactus.redis.config.FeignConfigAuto
3. 分布式锁在请求后自动解锁：com.cactus.redis.config.RedisUnlockAllMvcConfig
4. redis缓存配置：com.cactus.redis.config.RedisCacheConfig
5. RedisTemplate<String, Object>配置：com.cactus.redis.config.RedisTemplateConfig



## cactus-mybatis

1. 新增neo4j数据库操作
2. 设置mybatis的id生成规则和其他一些个性设置：com.cactus.mybatis.MybatisPlusAuto



## cactus-seata

1. 自定义数据验证，在rollback做镜像对比时，如果前面的对比失败，最后会做自定义字段的对比。具体需要看com.cactus.seata.SeataValidationProperties的fieldsGroups字段介绍。



## cactus-swagger

1. swagger的一些设置。