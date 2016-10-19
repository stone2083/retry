package com.helijia.framework.retry.support;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.helijia.framework.mq.MqMessage;
import com.helijia.framework.mq.MqProducerService;
import com.helijia.framework.retry.AsyncRetry;
import com.helijia.framework.retry.AsyncRetryContext;
import com.helijia.framework.retry.AsyncRetryPolicy;
import com.helijia.framework.retry.RecoveryCallback;
import com.helijia.framework.retry.RetryCallback;
import com.helijia.framework.retry.RetryException;
import com.helijia.framework.retry.policy.SimpleAsyncRetryPolicy;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class AsyncRetryTemplate extends AbstractRetryTemplate implements AsyncRetry {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRetryTemplate.class);

    private AsyncRetryPolicy policy = new SimpleAsyncRetryPolicy();

    private Map<String, RetryCallback<?>> retries;
    private Map<String, RecoveryCallback<?>> recoveries;
    private Map<String, AsyncRetryPolicy> policies;

    private MqProducerService producerService;

    @Override
    public void submitAsyncRetry(AsyncRetryContext ctx) {
        checkAsyncRetryContext(ctx);

        String key = buildKey(ctx);
        AsyncRetryPolicy retryPolicy = (policies != null && policies.containsKey(key)) ? policies.get(key) : policy;

        MqMessage msg = new MqMessage(buildTopicName(ctx), serialize(ctx), retryPolicy.getNextRetryTime(ctx));
        boolean flag = producerService.send(msg);
        if (!flag) {
            throw new RetryException("failed to submit aysnc retry.");
        }
    }

    @Override
    public void executeAsyncRetry(AsyncRetryContext ctx) {
        try {
            checkAsyncRetryContext(ctx);
        } catch (Exception e) {
            LOGGER.error("failed to async retry. ctx is invalid. ctx={}]", ctx);
            return;
        }

        String key = buildKey(ctx);
        RetryCallback<?> retry = retries.get(key);
        RecoveryCallback<?> recovery = (recoveries != null) ? recoveries.get(key) : null;
        AsyncRetryPolicy retryPolicy = (policies != null && policies.containsKey(key)) ? policies.get(key) : policy;

        // 理论上，这种情况是不存在的。 因为无法尝试的请求，不会再被持久化。
        if (!retryPolicy.canRetry(ctx)) {
            LOGGER.warn("unreasonable code. ctx is {}", ctx);
            return;
        }

        try {
            ctx.increaseRetryCount();
            retry.retry(ctx);
            fireRetrySuccess(ctx, retry);
        } catch (Exception e) {
            fireRetryError(ctx, retry, e);
            LOGGER.warn("failed to async retry. ctx is {}", ctx);

            // 下一次重试
            if (retryPolicy.canRetry(ctx)) {
                try {
                    submitAsyncRetry(ctx);
                } catch (RetryException e1) {
                    LOGGER.error("failed to submit async retry.", e1);
                }
            }
            // 无法继续重试了
            else {
                // 重试无结果
                if (recovery == null) {
                    fireRetryExhausted(ctx, retry);
                    LOGGER.error("async retry exhausted. ctx is {}", ctx);
                }

                else {
                    try {
                        recovery.recover(ctx);
                    } catch (Exception e1) {
                        LOGGER.error("async recovery error. ctx is {}", ctx);
                    } finally {
                        fireRecovery(ctx, recovery);
                    }
                }
            }
        }
    }

    private void checkAsyncRetryContext(AsyncRetryContext ctx) {
        if (ctx == null || ctx.getType() == null || ctx.getName() == null) {
            throw new IllegalArgumentException("ctx is invalid.");
        }
        if (retries == null || !retries.containsKey(buildKey(ctx))) {
            throw new RetryException("retry callback not found.");
        }
        if ((policies == null || !policies.containsKey(buildKey(ctx))) && policy == null) {
            throw new RetryException("retry policy not found.");
        }
    }

    private String buildKey(AsyncRetryContext ctx) {
        return ctx.getType() + ":" + ctx.getName();
    }

    private String buildTopicName(AsyncRetryContext ctx) {
        return "Retry-" + ctx.getType();
    }

    private byte[] serialize(AsyncRetryContext ctx) {
        try {
            return JSON.toJSONString(ctx, SerializerFeature.WriteClassName).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPolicy(AsyncRetryPolicy policy) {
        this.policy = policy;
    }

    public void setRetries(Map<String, RetryCallback<?>> retries) {
        this.retries = retries;
    }

    public void setRecoveries(Map<String, RecoveryCallback<?>> recoveries) {
        this.recoveries = recoveries;
    }

    public void setPolicies(Map<String, AsyncRetryPolicy> policies) {
        this.policies = policies;
    }

    public void setMqProducerService(MqProducerService producerService) {
        this.producerService = producerService;
    }

}
