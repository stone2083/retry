package com.helijia.framework.mq.rocketmq;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.helijia.framework.mq.BaseMqProducerService;
import com.helijia.framework.mq.MqMessage;
import com.helijia.framework.mq.MqProducerService;

/**
 *
 * @author jinli Jan 27, 2016
 */
public class RocketMqProducerService extends BaseMqProducerService implements MqProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqProducerService.class);

    private DefaultMQProducer producer;

    @Override
    public void start() {
        validate();
        producer = new DefaultMQProducer();
        producer.setNamesrvAddr(getAddress());
        producer.setProducerGroup(getGroup());
        producer.setInstanceName(getInstance());
        try {
            producer.start();
        } catch (Exception e) {
            throw new RuntimeException("rocket mq producer start fail.", e);
        }
    }

    @Override
    public void shutdown() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    @Override
    public boolean send(String topic, byte[] body) {
        return send(new MqMessage(topic, body), getTimeout());
    }

    @Override
    public boolean send(String topic, byte[] body, long timeout) {
        return send(new MqMessage(topic, body), timeout);
    }

    @Override
    public boolean send(MqMessage msg) {
        return send(msg, getTimeout());
    }

    @Override
    public boolean send(MqMessage msg, long timeout) {
        if (msg == null || msg.getTopic() == null | msg.getTopic().isEmpty() || msg.getBody() == null) {
            throw new IllegalArgumentException("msg is invalid.");
        }
        if (timeout < 1L) {
            throw new IllegalArgumentException("timeout must be great than zero.");
        }
        try {
            Message message = new Message(msg.getTopic(), msg.getBody());
            if (msg.getDelayed() > 0) {
                message.setDelayTimeLevel(RocketMqMessageDelayedLevel.valueOf(msg.getDelayed()).getLevel());
            }
            SendResult r = producer.send(message, timeout);
            if (r == null || r.getSendStatus() != SendStatus.SEND_OK) {
                LOGGER.warn(format("not fully completed to send mq. [status=%s]", r != null ? r.getSendStatus() : "unknown"));
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(format("failed to send mq. [topic=%s]", msg.getTopic()), e);
            return false;
        }
    }

}
