package com.cactus.redis.lock.dubbo;

import com.cactus.core.toolkit.JsonUtil;
import com.cactus.redis.lock.LockContext;
import com.cactus.redis.lock.RedisLock;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * 分布式锁传递的服务提供者
 * @author lht
 * @since 2021/11/4 6:33 下午
 */
@Activate(group = {CommonConstants.PROVIDER},order = -9999)
public class RedisLockProviderFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //接受传递锁
        String lockContextTransferStr = RpcContext.getContext().getAttachment(LockContext.LOCK_CONTEXT_TRANSFER_KEY);
        if(StringUtils.hasLength(lockContextTransferStr)){
            LockContext.recordLock(JsonUtil.transferToObj(lockContextTransferStr, new TypeReference<Set<String>>() {}));
        }
        //执行结果
        Result result = invoker.invoke(invocation);
        //清除锁
        RedisLock.unlockAll();
        return result;
    }
}
