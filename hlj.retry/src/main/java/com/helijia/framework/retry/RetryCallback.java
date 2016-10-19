package com.helijia.framework.retry;

/**
 * 重试逻辑
 * 
 * @author jinli Jun 1, 2016
 */
public interface RetryCallback<T> {

    /**
     * 执行重试逻辑
     * 
     * @param ctx 重试上下文
     * @return 重试成功对象
     * @throws Exception
     */
    T retry(RetryContext ctx) throws Exception;

}
