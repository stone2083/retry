package com.helijia.framework.mq.rocketmq;

import org.junit.Test;

/**
 *
 * @author jinli Mar 10, 2016
 */
public class RocketMqProducerExample {

    private static final String ADDRESS = "127.0.0.1:9876";
    private static final String TOPIC = "TestCase";

    @Test
    public void test() {
        RocketMqProducerService producer = new RocketMqProducerService();
        producer.setAddress(ADDRESS);
        producer.setGroup("producer");
        producer.start();

        for (int i = 0; i < 20; i++) {
            producer.send(TOPIC, ("TestCase" + i).getBytes());
        }
        producer.shutdown();
    }

}
