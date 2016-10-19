package com.helijia.framework.retry;

/**
 * 异步重试策略。
 * 
 * @author jinli Jun 2, 2016
 */
public interface AsyncRetryPolicy extends RetryPolicy {

    /**
     * 返回下一次重试的间隔时间，单位是秒。
     * 
     * @param ctx
     * @return
     */
    int getNextRetryTime(AsyncRetryContext ctx);

}
