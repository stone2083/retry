package com.helijia.framework.mq;

import java.util.List;

/**
 *
 * @author jinli Jan 27, 2016
 */
public interface MqConsumerService extends MqLifeCycle {
    
    public static final int MAX_THREADS = 64;
    public static final int MIN_THREADS = 16;

    void setListener(MqMessageListener listener);

    void setTopic(String topic);

    void setTopics(List<String> topics);

    void setMaxThreads(int threads);

    void setMinThreads(int threads);

}
