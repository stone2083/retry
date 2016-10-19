package com.helijia.framework.mq.rocketmq;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.helijia.framework.mq.MqMessage;
import com.helijia.framework.mq.MqMessageListener;
import com.helijia.framework.mq.MqStatus;

/**
 *
 * @author jinli May 23, 2016
 */
public class RocketMqDeleyedMessageTest {

    private static final String ADDRESS = "127.0.0.1:9876";
    private static final String TOPIC = "TestCase-Delayed";

    @Test
    public void test() throws Exception {
        CountDownLatch latch = new CountDownLatch(5);

        RocketMqConsumerService consumer = new RocketMqConsumerService();
        consumer.setAddress(ADDRESS);
        consumer.setGroup("producer1");
        consumer.setTopic(TOPIC);
        consumer.setListener(new RocketMqDeleyedMessageListener(latch));
        consumer.start();

        RocketMqProducerService producer = new RocketMqProducerService();
        producer.setAddress(ADDRESS);
        producer.setGroup("producer1");
        producer.start();

        producer.send(new MqMessage(TOPIC, JSON.toJSONBytes(new RocketMqDeleyedMessageInfo("1S", 1, System.currentTimeMillis())), 1));
        producer.send(new MqMessage(TOPIC, JSON.toJSONBytes(new RocketMqDeleyedMessageInfo("5S", 5, System.currentTimeMillis())), 5));
        producer.send(new MqMessage(TOPIC, JSON.toJSONBytes(new RocketMqDeleyedMessageInfo("10S", 10, System.currentTimeMillis())), 10));
        producer.send(new MqMessage(TOPIC, JSON.toJSONBytes(new RocketMqDeleyedMessageInfo("30S", 30, System.currentTimeMillis())), 30));
        producer.send(new MqMessage(TOPIC, JSON.toJSONBytes(new RocketMqDeleyedMessageInfo("60S", 60, System.currentTimeMillis())), 60));

        latch.await();
        producer.shutdown();
        consumer.shutdown();
    }

    private static class RocketMqDeleyedMessageListener implements MqMessageListener {

        private CountDownLatch latch;

        private RocketMqDeleyedMessageListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public boolean order() {
            return false;
        }

        @Override
        public MqStatus onMessageReceived(List<MqMessage> msgs) {
            for (MqMessage msg : msgs) {
                try {
                    RocketMqDeleyedMessageInfo info = JSON.parseObject(msg.getBody(), RocketMqDeleyedMessageInfo.class);
                    info.setConsumeTime(System.currentTimeMillis());
                    System.out.println(info);
                    Assert.assertTrue(info.getReallyDelayed() - info.getDelayed() < 2);
                    Assert.assertEquals(info.getDelayed() + "S", info.getName());
                } catch (Throwable e) {
                    System.out.println(e.getMessage());
                }
                latch.countDown();
            }
            return MqStatus.SUCCESS;
        }

    }

    @SuppressWarnings("unused")
    private static class RocketMqDeleyedMessageInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        private String name;
        private int delayed;
        private long produceTime;
        private long consumeTime;

        public RocketMqDeleyedMessageInfo() {
        }

        public RocketMqDeleyedMessageInfo(String name, int delayed, long produceTime) {
            this.name = name;
            this.delayed = delayed;
            this.produceTime = produceTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDelayed() {
            return delayed;
        }

        public void setDelayed(int delayed) {
            this.delayed = delayed;
        }

        public long getProduceTime() {
            return produceTime;
        }

        public void setProduceTime(long produceTime) {
            this.produceTime = produceTime;
        }

        public long getConsumeTime() {
            return consumeTime;
        }

        public void setConsumeTime(long consumeTime) {
            this.consumeTime = consumeTime;
        }

        public long getReallyDelayed() {
            return (getConsumeTime() - getProduceTime()) / 1000;
        }

        @Override
        public String toString() {
            return "[name=" + name + ", delayed=" + delayed + ", produceTime=" + produceTime + ", consumeTime="
                    + consumeTime + ", reallyDelayed=" + getReallyDelayed() + "]";
        }

    }
}
