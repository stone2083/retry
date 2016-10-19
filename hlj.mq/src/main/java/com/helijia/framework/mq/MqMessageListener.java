package com.helijia.framework.mq;

import java.util.List;

/**
 *
 * @author jinli Jan 27, 2016
 */
public interface MqMessageListener {

    boolean order();

    MqStatus onMessageReceived(List<MqMessage> msgs);

}
