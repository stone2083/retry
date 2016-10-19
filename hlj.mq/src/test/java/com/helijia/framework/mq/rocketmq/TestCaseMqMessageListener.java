package com.helijia.framework.mq.rocketmq;

import java.util.List;

import com.helijia.framework.mq.MqMessage;
import com.helijia.framework.mq.MqMessageListener;
import com.helijia.framework.mq.MqStatus;

/**
 *
 * @author jinli Mar 10, 2016
 */
public class TestCaseMqMessageListener implements MqMessageListener {

    private String consumer;
    private boolean order;

    public TestCaseMqMessageListener(String consumer, boolean order) {
        this.consumer = consumer;
        this.order = order;
    }

    @Override
    public boolean order() {
        return order;
    }

    @Override
    public MqStatus onMessageReceived(List<MqMessage> msgs) {
        try {
            for (MqMessage msg : msgs) {
                System.out.println(consumer + ":" + msg.getTopic() + ":" + new String(msg.getBody()));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return MqStatus.SUCCESS;
    }

}
