package com.helijia.framework.retry;

/**
 * 同步重试
 * 
 * @author jinli Jun 2, 2016
 */
public interface Retry {

    <T> T execute(RetryCallback<T> retry) throws Exception;

    <T> T execute(RetryCallback<T> retry, RecoveryCallback<T> recovery) throws Exception;

    <T> T execute(RetryCallback<T> retry, RetryPolicy policy) throws Exception;

    <T> T execute(RetryCallback<T> retry, RecoveryCallback<T> recovery, RetryPolicy policy) throws Exception;

}
