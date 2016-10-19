package com.helijia.framework.retry;

/**
 *
 * @author jinli Jun 2, 2016
 */
public interface AsyncRetry {

    /**
     * 提交异步重试请求
     * 
     * @param ctx
     */
    void submitAsyncRetry(AsyncRetryContext ctx);

    /**
     * 执行异步重试请求
     * 
     * @param ctx
     */
    void executeAsyncRetry(AsyncRetryContext ctx);

}
