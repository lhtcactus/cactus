package com.cactus.redis.lock.feign;

import com.cactus.core.exception.GlobalException;
import com.cactus.core.toolkit.JsonUtil;
import com.cactus.redis.lock.LockContext;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Set;

/**
 * feign 分布式锁传递接受解析
 * @author lht
 * @since 2022/2/15 4:18 下午
 */
@Slf4j
public class FeignTransferRedisLockMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String keysJson = request.getHeader(LockContext.LOCK_CONTEXT_TRANSFER_KEY);
                if(StringUtils.hasText(keysJson)){
                    try{
                        Set<String> keys = JsonUtil.transferToObj(
                                URLDecoder.decode(keysJson, "UTF-8"),
                                new TypeReference<Set<String>>(){});
                        LockContext.recordLock(keys);
                    }catch (Exception e){
                        log.error("feign 分布式锁传递接受解析异常 ",e);
                        throw new GlobalException("feign 分布式锁传递接受解析异常",e);
                    }
                }
                return true;
            }
        });
    }
}
