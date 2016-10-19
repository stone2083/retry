package com.helijia.framework.retry.support;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helijia.framework.retry.RecoveryCallback;
import com.helijia.framework.retry.RetryCallback;
import com.helijia.framework.retry.RetryContext;
import com.helijia.framework.retry.RetryListener;

/**
 *
 * @author jinli Jun 2, 2016
 */
public abstract class AbstractRetryTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRetryTemplate.class);

    private List<RetryListener> listeners;

    protected void fireRetrySuccess(RetryContext ctx, RetryCallback<?> retry) {
        if (listeners != null && !listeners.isEmpty()) {
            for (RetryListener l : listeners) {
                try {
                    l.onRetrySuccess(ctx, retry);
                } catch (Exception e) {
                    LOGGER.warn("failed to call retry listener.", e);
                }
            }
        }
    }

    protected void fireRetryError(RetryContext ctx, RetryCallback<?> retry, Exception exception) {
        if (listeners != null && !listeners.isEmpty()) {
            for (RetryListener l : listeners) {
                try {
                    l.onRetryError(ctx, retry, exception);
                } catch (Exception e) {
                    LOGGER.warn("failed to call retry listener.", e);
                }
            }
        }
    }

    protected void fireRetryExhausted(RetryContext ctx, RetryCallback<?> retry) {
        if (listeners != null && !listeners.isEmpty()) {
            for (RetryListener l : listeners) {
                try {
                    l.onRetryExhausted(ctx, retry);
                } catch (Exception e) {
                    LOGGER.warn("failed to call retry listener.", e);
                }
            }
        }
    }

    protected void fireRecovery(RetryContext ctx, RecoveryCallback<?> recovery) {
        if (listeners != null && !listeners.isEmpty()) {
            for (RetryListener l : listeners) {
                try {
                    l.onRecovery(ctx, recovery);
                } catch (Exception e) {
                    LOGGER.warn("failed to call retry listener.", e);
                }
            }
        }
    }

    public void setRetryListeners(List<RetryListener> listeners) {
        this.listeners = listeners;
    }

}
