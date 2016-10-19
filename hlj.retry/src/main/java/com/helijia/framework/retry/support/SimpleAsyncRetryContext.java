package com.helijia.framework.retry.support;

import com.helijia.framework.retry.AsyncRetryContext;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class SimpleAsyncRetryContext extends SimpleRetryContext implements AsyncRetryContext {

    private String type;
    private String name;
    private byte[] body;

    public SimpleAsyncRetryContext() {
    }

    public SimpleAsyncRetryContext(String type, String name, byte[] body) {
        this.type = type;
        this.name = name;
        this.body = body;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "SimpleAsyncRetryContext [type=" + type + ", name=" + name + ", retryCount=" + getRetryCount() + "]";
    }

}
