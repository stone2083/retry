package com.helijia.framework.mq;

/**
 *
 * @author jinli Jan 27, 2016
 */
public interface MqProducerService extends MqLifeCycle {

    public static final long TIMEOUT = 3000; // 默认超时时间, 3秒

    boolean send(String topic, byte[] body);

    boolean send(String topic, byte[] body, long timeout);

    boolean send(MqMessage msg);

    boolean send(MqMessage msg, long timeout);

    /**
     * 设置超时时间, 单位毫秒
     * 
     * @param timeout
     */
    void setTimeout(long timeout);

}
