package com.helijia.framework.mq;

/**
 *
 * @author jinli Jan 27, 2016
 */
public abstract class BaseMqProducerService extends BaseMqService implements MqProducerService {

    private long timeout = TIMEOUT;

    @Override
    protected void validate() {
        super.validate();
        if (timeout < 1L) {
            throw new IllegalArgumentException("timeout muse be great than zero.");
        }
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(long timeout) {
        if (timeout < 1L) {
            throw new IllegalArgumentException("timeout muse be great than zero.");
        }
        this.timeout = timeout;
    }

}
