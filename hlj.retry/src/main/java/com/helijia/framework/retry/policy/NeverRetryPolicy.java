package com.helijia.framework.retry.policy;

import com.helijia.framework.retry.RetryContext;
import com.helijia.framework.retry.RetryPolicy;

/**
 *
 * @author jinli Jun 6, 2016
 */
public class NeverRetryPolicy implements RetryPolicy {

    @Override
    public boolean canRetry(RetryContext ctx) {
        return false;
    }

}
