package com.cactus.redis.lock.dubbo;

import com.cactus.core.toolkit.JsonUtil;
import com.cactus.redis.lock.LockContext;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 分布式锁传递的服务消费者
 * @author lht
 * @since 2021/11/4 6:33 下午
 */
@Activate(group = {CommonConstants.CONSUMER},order = -9999)
public class RedisLockConsumerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //传递锁
        Set<String> lockContextTransfer = LockContext.getRecordedKeys();
        if(!CollectionUtils.isEmpty(lockContextTransfer)){
            RpcContext.getContext().setAttachment(LockContext.LOCK_CONTEXT_TRANSFER_KEY,
                    JsonUtil.transferToJson(LockContext.getRecordedKeys()));
        }
        return invoker.invoke(invocation);
    }
}
