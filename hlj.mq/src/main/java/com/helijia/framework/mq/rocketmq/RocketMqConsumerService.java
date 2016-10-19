package com.helijia.framework.mq.rocketmq;

import static com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
import static com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.RECONSUME_LATER;
import static com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
import static com.helijia.framework.mq.MqStatus.SUCCESS;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.helijia.framework.mq.BaseMqConsumerService;
import com.helijia.framework.mq.MqConsumerService;
import com.helijia.framework.mq.MqMessage;
import com.helijia.framework.mq.MqMessageExtras;
import com.helijia.framework.mq.MqMessageListener;
import com.helijia.framework.mq.MqStatus;

/**
 *
 * @author jinli Jan 27, 2016
 */
public class RocketMqConsumerService extends BaseMqConsumerService implements MqConsumerService {

    private static final String EXPRESSION = "*";

    private DefaultMQPushConsumer consumer;

    @Override
    public void start() {
        validate();
        try {
            consumer = new DefaultMQPushConsumer();
            consumer.setNamesrvAddr(getAddress());
            consumer.setConsumerGroup(getGroup());
            consumer.setInstanceName(getInstance());
            consumer.setConsumeThreadMax(getMaxThreads());
            consumer.setConsumeThreadMin(getMinThreads());
            for (String topic : getTopics()) {
                consumer.subscribe(topic, EXPRESSION);
            }
            if (getListener().order()) {
                consumer.registerMessageListener(new MessageListenerOrderlyAdaptor(getListener()));
            } else {
                consumer.registerMessageListener(new MessageListenerConcurrentlyAdaptor(getListener()));
            }
            consumer.start();
        } catch (Exception e) {
            throw new RuntimeException("rocket mq consumer start fail.", e);
        }
    }

    @Override
    public void shutdown() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    private static final class MessageListenerConcurrentlyAdaptor implements MessageListenerConcurrently {

        private MqMessageListener listener;

        public MessageListenerConcurrentlyAdaptor(MqMessageListener listener) {
            this.listener = listener;
        }

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            List<MqMessage> messages = new ArrayList<>(msgs.size());
            for (MessageExt msg : msgs) {
                MqMessageExtras extras = new MqMessageExtras();
                extras.setMsgAddress(msg.getBornHost());

                messages.add(new MqMessage(msg.getTopic(), msg.getBody(), extras));
            }
            MqStatus status = listener.onMessageReceived(messages);
            return SUCCESS.equals(status) ? CONSUME_SUCCESS : RECONSUME_LATER;
        }

    }

    private static final class MessageListenerOrderlyAdaptor implements MessageListenerOrderly {

        private MqMessageListener listener;

        public MessageListenerOrderlyAdaptor(MqMessageListener listener) {
            this.listener = listener;
        }

        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            List<MqMessage> messages = new ArrayList<>(msgs.size());
            for (MessageExt msg : msgs) {
                MqMessageExtras extras = new MqMessageExtras();
                extras.setMsgAddress(msg.getBornHost());

                messages.add(new MqMessage(msg.getTopic(), msg.getBody(), extras));
            }
            MqStatus status = listener.onMessageReceived(messages);
            return SUCCESS.equals(status) ? ConsumeOrderlyStatus.SUCCESS : SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }

    }

}
