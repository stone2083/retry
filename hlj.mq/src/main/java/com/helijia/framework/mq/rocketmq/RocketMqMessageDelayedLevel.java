package com.helijia.framework.mq.rocketmq;

/**
 * 此处需要和服务端配置配合使用。
 * 目前RocketMQ服务端的配置为：
 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 * 
 * @author jinli May 23, 2016
 */
public enum RocketMqMessageDelayedLevel {

    NO_DELAYED(0, 0),               // 0秒
    DELAYED_1S(1, 1),               // 1秒
    DELAYED_5S(2, 5),               // 2秒
    DELAYED_10S(3, 10),             // 10秒
    DELAYED_30S(4, 30),             // 30秒
    DELAYED_1M(5, 60),              // 1分
    DELAYED_2M(6, 2 * 60),          // 2分
    DELAYED_3M(7, 3 * 60),          // 3分
    DELAYED_4M(8, 4 * 60),          // 4分
    DELAYED_5M(9, 5 * 60),          // 5分
    DELAYED_6M(10, 6 * 60),         // 6分
    DELAYED_7M(11, 7 * 60),         // 7分
    DELAYED_8M(12, 8 * 60),         // 8分
    DELAYED_9M(13, 9 * 60),         // 9分
    DELAYED_10M(14, 10 * 60),       // 10分
    DELAYED_20M(15, 20 * 60),       // 20分
    DELAYED_30M(16, 30 * 60),       // 30分
    DELAYED_1H(17, 60 * 60),        // 1时
    DELAYED_2H(18, 2 * 60 * 60);    // 2时

    private int level;
    private int delayed;

    private RocketMqMessageDelayedLevel(int level, int delayed) {
        this.level = level;
        this.delayed = delayed;
    }

    public int getLevel() {
        return level;
    }

    public int getDelayed() {
        return delayed;
    }

    public static RocketMqMessageDelayedLevel valueOf(int delayed) {
        RocketMqMessageDelayedLevel last = RocketMqMessageDelayedLevel.NO_DELAYED;
        for (RocketMqMessageDelayedLevel level : values()) {
            // 刚好相等，则直接返回。
            if (level.getDelayed() == delayed) {
                return level;
            }
            // 配置的等级比较小，则继续选择下一个。
            if (level.getDelayed() < delayed) {
                last = level;
            }
            // 配置的等级比较大，则从当前配合 和 min中，选择一个误差更小的。
            else {
                return delayed <= (last.getDelayed() + level.getDelayed()) / 2 ? last : level;
            }
        }
        return RocketMqMessageDelayedLevel.DELAYED_2H;
    }

}
