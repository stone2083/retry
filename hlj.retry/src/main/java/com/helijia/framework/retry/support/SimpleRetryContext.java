package com.helijia.framework.retry.support;

import com.helijia.framework.retry.RetryContext;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class SimpleRetryContext implements RetryContext {

    private int retryCount;

    public SimpleRetryContext() {
        this(0);
    }

    public SimpleRetryContext(int count) {
        setRetryCount(count);
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public void increaseRetryCount() {
        retryCount++;
    }

    public void setRetryCount(int retryCount) {
        if (retryCount < 0) {
            throw new IllegalArgumentException("count is invalid.");
        }
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "SimpleRetryContext [count=" + retryCount + "]";
    }

}
