package com.helijia.framework.mq;

import java.io.Serializable;

import com.helijia.framework.mq.rocketmq.RocketMqMessageDelayedLevel;

/**
 * <pre>
 * MQ消息。
 * 
 * 关于延迟功能的说明：
 *     支持延迟发送功能，通过delayed属性，表达要延迟的时间，单位为秒。
 *     但是并非所有的MQ实现，都严格遵守delayed属性，只能取一个最接近的值。
 *     比如 {@link RocketMqMessageDelayedLevel}，假定设置的delayed为4s，则取最接近的5S时间点。
 * </pre>
 * 
 * @author jinli Jan 27, 2016
 */
public class MqMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String topic;   // 消息主题
    private byte[] body;    // 消息内容
    private int delayed;    // 消息延迟时间，默认0表示不延时，单位为秒

    private MqMessageExtras extras;

    public MqMessage() {
    }

    public MqMessage(String topic, byte[] body) {
        this(topic, body, 0, null);
    }

    public MqMessage(String topic, byte[] body, int delayed) {
        this(topic, body, delayed, null);
    }

    public MqMessage(String topic, byte[] body, MqMessageExtras extras) {
        this(topic, body, 0, extras);
    }

    public MqMessage(String topic, byte[] body, int delayed, MqMessageExtras extras) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("topic is illegal.");
        }
        if (delayed < 0) {
            throw new IllegalArgumentException("delayed is illegal.");
        }
        this.topic = topic;
        this.body = body;
        this.delayed = delayed;
        this.extras = extras;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getDelayed() {
        return delayed;
    }

    public void setDelayed(int delayed) {
        if (delayed < 0) {
            throw new IllegalArgumentException("delayed is illegal.");
        }
        this.delayed = delayed;
    }

    public MqMessageExtras getExtras() {
        return extras;
    }

    public void setExtras(MqMessageExtras extras) {
        this.extras = extras;
    }

}
