package com.helijia.framework.retry;

/**
 * 重试策略
 * 
 * @author jinli Jun 2, 2016
 */
public interface RetryPolicy {

    /**
     * 是否需要重试
     * 
     * @param ctx
     * @return
     */
    boolean canRetry(RetryContext ctx);

}
