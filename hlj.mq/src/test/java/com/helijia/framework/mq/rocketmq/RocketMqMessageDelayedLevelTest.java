package com.helijia.framework.mq.rocketmq;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * <pre>
 * DELAYED_1S(1, 1),            // 1秒
 * DELAYED_5S(2, 5),            // 2秒
 * DELAYED_10S(3, 10),          // 10秒
 * DELAYED_30S(4, 30),          // 30秒
 * DELAYED_1M(5, 60),           // 1分
 * DELAYED_2M(6, 2 * 60),       // 2分
 * DELAYED_3M(7, 3 * 60),       // 3分
 * DELAYED_4M(8, 4 * 60),       // 4分
 * DELAYED_5M(9, 5 * 60),       // 5分
 * DELAYED_6M(10, 6 * 60),      // 6分
 * DELAYED_7M(11, 7 * 60),      // 7分
 * DELAYED_8M(12, 8 * 60),      // 8分
 * DELAYED_9M(13, 9 * 60),      // 9分
 * DELAYED_10M(14, 10 * 60),    // 10分
 * DELAYED_20M(15, 20 * 60),    // 20分
 * DELAYED_30M(16, 30 * 60),    // 30分
 * DELAYED_1H(17, 60 * 60),     // 1时
 * DELAYED_2H(18, 2 * 60 * 60); // 2时
 * </pre>
 *
 * @author jinli May 23, 2016
 */
public class RocketMqMessageDelayedLevelTest {

    @Test
    public void test() {
        Map<Integer, RocketMqMessageDelayedLevel> levels = new HashMap<Integer, RocketMqMessageDelayedLevel>();
        init(levels, RocketMqMessageDelayedLevel.NO_DELAYED, 0, 0);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_1S, 1, 3);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_5S, 4, 7);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_10S, 8, 20);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_30S, 21, 45);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_1M, 46, 90);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_2M, 91, 150);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_3M, 151, 210);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_4M, 211, 270);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_5M, 271, 330);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_6M, 331, 390);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_7M, 391, 450);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_8M, 451, 510);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_9M, 511, 570);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_10M, 571, 900);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_20M, 901, 1500);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_30M, 1501, 2700);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_1H, 2701, 5400);
        init(levels, RocketMqMessageDelayedLevel.DELAYED_2H, 5401, 20000);

        for (int i = 0; i <= 10000; i++) {
            Assert.assertEquals(RocketMqMessageDelayedLevel.valueOf(i), levels.get(i));
        }
    }

    private void init(Map<Integer, RocketMqMessageDelayedLevel> levels, RocketMqMessageDelayedLevel level, int start, int end) {
        for (int i = start; i <= end; i++) {
            levels.put(i, level);
        }
    }

}
