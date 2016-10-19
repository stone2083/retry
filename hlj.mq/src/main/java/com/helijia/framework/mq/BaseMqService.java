package com.helijia.framework.mq;

import java.util.Random;

/**
 *
 * @author jinli Jan 27, 2016
 */
public abstract class BaseMqService {

    private String address;
    private String group;
    private String instance;

    public BaseMqService() {
        init();
    }

    protected void validate() {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("address is invalid.");
        }
        if (group == null || group.isEmpty()) {
            throw new IllegalArgumentException("group is invalid.");
        }
        if (instance == null || instance.isEmpty()) {
            throw new IllegalArgumentException("instance is invalid.");
        }
    }

    public void init() {
        this.instance = String.valueOf(System.currentTimeMillis() + new Random().nextInt());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

}
