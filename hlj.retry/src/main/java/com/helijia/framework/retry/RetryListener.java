package com.helijia.framework.retry;

/**
 *
 * @author jinli Jun 2, 2016
 */
public interface RetryListener {

    enum RetryState {
        SUCCESS, ERROR, EXHUSTED, RECOVERY;
    }

    /**
     * 重试成功
     * 
     * @param ctx
     * @param retry
     */
    void onRetrySuccess(RetryContext ctx, RetryCallback<?> retry);

    /**
     * 重试失败
     * 
     * @param ctx
     * @param retry
     * @param e
     */
    void onRetryError(RetryContext ctx, RetryCallback<?> retry, Exception e);

    /**
     * 连续重试无结果，达到重试次数上限
     * 
     * @param ctx
     * @param retry
     */
    void onRetryExhausted(RetryContext ctx, RetryCallback<?> retry);

    /**
     * 进入恢复阶段
     * 
     * @param ctx
     * @param recovery
     */
    void onRecovery(RetryContext ctx, RecoveryCallback<?> recovery);

}
