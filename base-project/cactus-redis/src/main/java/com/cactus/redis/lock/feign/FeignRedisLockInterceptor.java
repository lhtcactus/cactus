package com.cactus.redis.lock.feign;

import com.alibaba.fastjson.JSON;
import com.cactus.redis.lock.LockContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.net.URLEncoder;
import java.util.Set;

/**
 * 设置feign发送的header中的lockContext
 * @author lht
 * @since 2020/11/27 11:34
 */
@Slf4j
public class FeignRedisLockInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        setLockContext(requestTemplate);
    }


    /**
     * 设置 分布式锁 Context
     * @author lht
     * @since  2020/12/17 17:34
     * @param requestTemplate feign requestTemplate
     */
    private void setLockContext(RequestTemplate requestTemplate){
        Set<String> keys = LockContext.getRecordedKeys();
        try {
            if(keys.isEmpty()){
                return ;
            }
            String keysJson = JSON.toJSONString(keys);
            requestTemplate.header(LockContext.LOCK_CONTEXT_TRANSFER_KEY, URLEncoder.encode(keysJson, "UTF-8"));
        } catch (Exception e) {
            log.error("feign传递分布式锁异常",e);
        }finally {
            LockContext.removeAll();
        }
    }

}
