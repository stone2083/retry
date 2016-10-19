package com.helijia.framework.retry.support;

import com.helijia.framework.retry.RecoveryCallback;
import com.helijia.framework.retry.Retry;
import com.helijia.framework.retry.RetryCallback;
import com.helijia.framework.retry.RetryContext;
import com.helijia.framework.retry.RetryException;
import com.helijia.framework.retry.RetryPolicy;
import com.helijia.framework.retry.policy.SimpleRetryPolicy;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class RetryTemplate extends AbstractRetryTemplate implements Retry {

    private RetryPolicy policy = new SimpleRetryPolicy(3);  // 默认重试策略，最多重试3次

    @Override
    public <T> T execute(RetryCallback<T> retry) throws Exception {
        return doExecute(openRetryContext(), getRetryPolicy(), retry, null);
    }

    @Override
    public <T> T execute(RetryCallback<T> retry, RecoveryCallback<T> recovery) throws Exception {
        return doExecute(openRetryContext(), getRetryPolicy(), retry, recovery);
    }

    @Override
    public <T> T execute(RetryCallback<T> retry, RetryPolicy policy) throws Exception {
        return doExecute(openRetryContext(), policy, retry, null);
    }

    @Override
    public <T> T execute(RetryCallback<T> retry, RecoveryCallback<T> recovery, RetryPolicy policy) throws Exception {
        return doExecute(openRetryContext(), policy, retry, recovery);
    }

    protected RetryContext openRetryContext() {
        return new SimpleRetryContext();
    }

    public RetryPolicy getRetryPolicy() {
        return policy;
    }

    protected <T> T doExecute(RetryContext ctx, RetryPolicy policy, RetryCallback<T> retry, RecoveryCallback<T> recovery) throws Exception {
        if (ctx == null) {
            throw new IllegalArgumentException("retry context can not be null.");
        }
        if (policy == null) {
            throw new IllegalArgumentException("retry policy can not be null.");
        }
        if (retry == null) {
            throw new IllegalArgumentException("retry callback can not be null.");
        }

        Exception lastException = null;
        while (policy.canRetry(ctx)) {
            ctx.increaseRetryCount();

            try {
                T ret = retry.retry(ctx);
                fireRetrySuccess(ctx, retry);
                return ret;
            } catch (Exception e) {
                lastException = e;
                fireRetryError(ctx, retry, e);
            }
        }

        // 重试无结果
        if (recovery == null) {
            fireRetryExhausted(ctx, retry);
            throw new RetryException("retry exhausted. [retryCount=" + ctx.getRetryCount() + "]", lastException);
        }

        try {
            return recovery.recover(ctx);
        } catch (Exception e) {
            throw e;
        } finally {
            fireRecovery(ctx, recovery);
        }
    }

}
