package com.helijia.framework.retry;

/**
 *
 * @author jinli Jun 2, 2016
 */
public interface AsyncRetryContext extends RetryContext {

    /**
     * 重试类型
     * 
     * @return
     */
    String getType();

    /**
     * 重试名称, 不同类型下，唯一。
     * 
     * @return
     */
    String getName();

    /**
     * 重试信息
     * 
     * @return
     */
    byte[] getBody();

}
