package com.helijia.framework.mq.rocketmq;

import org.junit.Test;

/**
 *
 * @author jinli Mar 10, 2016
 */
public class RocketMqConsumerExample {

    private static final String ADDRESS = "127.0.0.1:9876";
    private static final String TOPIC = "TestCase";

    @Test
    public void test() throws Exception {
        RocketMqConsumerService consumer1 = new RocketMqConsumerService();
        consumer1.setAddress(ADDRESS);
        consumer1.setGroup("consumer");
        consumer1.setTopic(TOPIC);
        consumer1.setListener(new TestCaseMqMessageListener("consumer", false));
        consumer1.start();
        Thread.sleep(2 * 60 * 1000);
    }

}
