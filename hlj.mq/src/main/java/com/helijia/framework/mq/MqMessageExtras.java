package com.helijia.framework.mq;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 *
 * @author jinli Feb 4, 2016
 */
public class MqMessageExtras implements Serializable {

    private static final long serialVersionUID = 1L;

    private SocketAddress msgAddress;

    public SocketAddress getMsgAddress() {
        return msgAddress;
    }

    public void setMsgAddress(SocketAddress msgAddress) {
        this.msgAddress = msgAddress;
    }
    
    

}
