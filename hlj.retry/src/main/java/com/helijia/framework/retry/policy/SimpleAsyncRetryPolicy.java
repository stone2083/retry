package com.helijia.framework.retry.policy;

import com.helijia.framework.retry.AsyncRetryContext;
import com.helijia.framework.retry.AsyncRetryPolicy;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class SimpleAsyncRetryPolicy extends SimpleRetryPolicy implements AsyncRetryPolicy {

    private int[] retryTimes;

    public SimpleAsyncRetryPolicy() {
        this(new int[] { 30, 60, 300 });    // 默认重试时间，半分钟，1分钟，5分钟
    }

    public SimpleAsyncRetryPolicy(int[] retryTimes) {
        if (retryTimes == null) {
            throw new IllegalArgumentException("retryTimes is invalid.");
        }
        for (long r : retryTimes) {
            if (r <= 0) {
                throw new IllegalArgumentException("retryTimes is invalid.");
            }
        }
        this.retryTimes = retryTimes;
        setMaxAttempts(retryTimes.length);
    }

    @Override
    public int getNextRetryTime(AsyncRetryContext ctx) {
        return (ctx.getRetryCount() < retryTimes.length)
                ? retryTimes[ctx.getRetryCount()] : retryTimes[retryTimes.length - 1];
    }

}
