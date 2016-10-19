package com.helijia.framework.mq;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jinli Jan 27, 2016
 */
public abstract class BaseMqConsumerService extends BaseMqService implements MqConsumerService {

    private MqMessageListener listener;
    private List<String> topics;
    private int maxThreads = MAX_THREADS;
    private int minThreads = MIN_THREADS;

    protected void validate() {
        super.validate();
        if (getListener() == null) {
            throw new IllegalArgumentException("listener is null.");
        }
        if (getTopics() == null || getTopics().isEmpty()) {
            throw new IllegalArgumentException("topics is empty.");
        }
        if (minThreads < 1 || minThreads > maxThreads) {
            throw new IllegalArgumentException("minThreads is invalid.");
        }
        if (maxThreads < 1 || maxThreads < minThreads) {
            throw new IllegalArgumentException("maxThreads is invalid.");
        }
    }

    public MqMessageListener getListener() {
        return listener;
    }

    public List<String> getTopics() {
        return topics;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public int getMinThreads() {
        return minThreads;
    }

    @Override
    public void setListener(MqMessageListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null.");
        }
        this.listener = listener;
    }

    @Override
    public void setTopic(String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("topic is empty.");
        }
        if (this.topics == null) {
            this.topics = new ArrayList<>();
        }
        this.topics.clear();
        this.topics.add(topic);
    }

    @Override
    public void setTopics(List<String> topics) {
        if (topics == null || topics.isEmpty()) {
            throw new IllegalArgumentException("topics is empty.");
        }
        this.topics = topics;
    }

    @Override
    public void setMaxThreads(int threads) {
        if (threads < 1) {
            throw new IllegalArgumentException("threads must be great than zero");
        }
        this.maxThreads = threads;
    }

    @Override
    public void setMinThreads(int threads) {
        if (threads < 1) {
            throw new IllegalArgumentException("threads must be great than zero");
        }
        this.minThreads = threads;
    }

}
