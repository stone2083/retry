package com.helijia.framework.mq.rocketmq;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jinli Jan 27, 2016
 */
public class RocketMqServiceIntegration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqServiceIntegration.class);

    private static final String ADDRESS = "127.0.0.1:9876";
    private static final String TOPIC = "TestCase";

    @Test
    public void test() throws Exception {
        RocketMqProducerService producer = new RocketMqProducerService();
        producer.setAddress(ADDRESS);
        producer.setGroup("producer");
        producer.start();

        RocketMqConsumerService consumer1 = new RocketMqConsumerService();
        consumer1.setAddress(ADDRESS);
        consumer1.setGroup("consumer1");
        consumer1.setTopic(TOPIC);
        consumer1.setListener(new TestCaseMqMessageListener("consumer1", true));
        consumer1.start();

        RocketMqConsumerService consumer2 = new RocketMqConsumerService();
        consumer2.setAddress(ADDRESS);
        consumer2.setGroup("consumer2");
        consumer2.setTopic(TOPIC);
        consumer2.setListener(new TestCaseMqMessageListener("consumer2", false));
        consumer2.start();

        for (int i = 0; i < 10; i++) {
            producer.send(TOPIC, ("TestCase" + i).getBytes());
        }

        Thread.sleep(2000);
        producer.shutdown();
        consumer1.shutdown();
        consumer2.shutdown();
    }

    @Test
    public void test_timeout() throws Exception {
        RocketMqProducerService producer = new RocketMqProducerService();
        producer.setAddress(ADDRESS);
        producer.setGroup("producer");
        producer.setInstance("producer-1");
        producer.start();
        boolean r = producer.send(TOPIC, "Timeout".getBytes(), 1);
        LOGGER.info("test timeout result is: {}", r);

        RocketMqConsumerService consumer1 = new RocketMqConsumerService();
        consumer1.setAddress(ADDRESS);
        consumer1.setGroup("consumer1");
        consumer1.setInstance("consumer1-1");
        consumer1.setTopic(TOPIC);
        consumer1.setListener(new TestCaseMqMessageListener("consumer1", true));
        consumer1.start();

        Thread.sleep(2000);
        producer.shutdown();
        consumer1.shutdown();
    }

}
