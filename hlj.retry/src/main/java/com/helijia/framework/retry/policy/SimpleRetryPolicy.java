package com.helijia.framework.retry.policy;

import com.helijia.framework.retry.RetryContext;
import com.helijia.framework.retry.RetryPolicy;

/**
 * 最简单的重试策略，只带最大重试次数。
 * 
 * @author jinli Jun 2, 2016
 */
public class SimpleRetryPolicy implements RetryPolicy {

    private static final int DEFAULT_MAX_ATTEMPTS = 3;

    private int maxAttempts = DEFAULT_MAX_ATTEMPTS;

    public SimpleRetryPolicy() {
    }

    public SimpleRetryPolicy(int maxAttempts) {
        setMaxAttempts(maxAttempts);
    }

    @Override
    public boolean canRetry(RetryContext context) {
        return maxAttempts > context.getRetryCount();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts is invalid.");
        }
        this.maxAttempts = maxAttempts;
    }

}
