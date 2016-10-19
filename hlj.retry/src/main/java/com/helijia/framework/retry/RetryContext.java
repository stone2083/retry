package com.helijia.framework.retry;

/**
 * 重试上下文
 * 
 * @author jinli Jun 1, 2016
 */
public interface RetryContext {

    /**
     * 获得重试次数
     * 
     * @return 重试次数
     */
    int getRetryCount();

    /**
     * 增加重试次数
     */
    void increaseRetryCount();

}
