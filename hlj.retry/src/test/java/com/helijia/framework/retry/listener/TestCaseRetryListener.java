package com.helijia.framework.retry.listener;

import com.helijia.framework.retry.RecoveryCallback;
import com.helijia.framework.retry.RetryCallback;
import com.helijia.framework.retry.RetryContext;
import com.helijia.framework.retry.RetryListener;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class TestCaseRetryListener implements RetryListener {

    @Override
    public void onRetrySuccess(RetryContext context, RetryCallback<?> retry) {
        System.out.println("retry success.");
    }

    @Override
    public void onRetryError(RetryContext context, RetryCallback<?> retry, Exception exception) {
        System.out.println("retry error.");

    }

    @Override
    public void onRetryExhausted(RetryContext context, RetryCallback<?> retry) {
        System.out.println("retry exhausted.");
    }

    @Override
    public void onRecovery(RetryContext context, RecoveryCallback<?> recovery) {
        System.out.println("retry recovery.");
    }

}
