package com.helijia.framework.retry.support;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.helijia.framework.mq.MqMessage;
import com.helijia.framework.mq.MqMessageListener;
import com.helijia.framework.mq.MqStatus;
import com.helijia.framework.retry.AsyncRetryContext;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class AsyncRetryJob implements MqMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRetryJob.class);

    private AsyncRetryTemplate asyncRetryTemplate;

    @Override
    public boolean order() {
        return false;
    }

    @Override
    public MqStatus onMessageReceived(List<MqMessage> msgs) {
        for (MqMessage msg : msgs) {
            try {
                AsyncRetryContext ctx = unserialize(msg.getBody());
                asyncRetryTemplate.executeAsyncRetry(ctx);
            } catch (Exception e) {
                LOGGER.error("failed to async retry.", e);
            }
        }
        return MqStatus.SUCCESS;
    }

    public void setAsyncRetryTemplate(AsyncRetryTemplate asyncRetryTemplate) {
        this.asyncRetryTemplate = asyncRetryTemplate;
    }

    private AsyncRetryContext unserialize(byte[] body) {
        try {
            return JSON.parseObject(new String(body, "UTF-8"), AsyncRetryContext.class);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
