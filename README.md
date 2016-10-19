# 什么是Retry框架
```
Retry框架，顾名思义，就是根据一系列重试策略，对某个业务逻辑进行重复处理的一套机制。
根据业务需要，重试支持 同步重试 和 异步重试  两种选择。
列举一个典型的场景：
当支付系统成功的时候，需要通知订单系统，但是过程中由于网络原因，或者订单系统不稳定（比如被shutdown了），会导致无法通知，导致双方系统的数据不一致。
在这种场景下，Retry框架闪亮登场，只需要业务开发者配置一个重试策略（比如最多重试N次，分别间隔时间5秒，30秒，5分钟...），编写重试逻辑（通知订单），Retry框架就能把这一切需求自动实现了。
```

# Retry核心组件介绍

### RetryContext
重试上下文，主要保存重试的次数，以及重试间隔时间。

### RetryCallback
重试逻辑，需要业务开发者自己定义重试逻辑，交给Retry框架进行重试操作。

### RecoveryCallback
恢复逻辑，当重试一直不成功，到达重试上限，则进行恢复逻辑。

典型的恢复逻辑包括，事务回滚，或者日志记录，或者监控报警等。

### RetryPolicy
重试策略，定义能够重试的条件， 以及每次重试之间的间隔时间。

典型的重试逻辑有，重试N次放弃，每次间隔时间如10秒，30秒，5分钟等。

属于SPI，可以扩展重试策略的不同实现，以满足业务要求。

### RetryListener
重试监听器，定义了4个不同的重试阶段，通知给监听器。

* onRetrySuccess 当重试成功了，进行通知
* onRetryError 当重试失败了，进行通知
* onRetryExhausted 当一直重试无结果，并且不配置RecoveryCallback时，进行通知
* onRecovery 当执行恢复逻辑的时候，进行通知

# 如何使用
### 引入mvn依赖
```xml
<dependency>
    <groupId>com.helijia.framework</groupId>
    <artifactId>helijia.retry</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 同步重试例子
同步重试，非常简单，只需要准备好：RetryCallback，RetryPolicy（可选），RecoveryCallback（可选）。
```java
RetryTemplate retryTemplate = new RetryTemplate();
// 默认最多重试3次
retryTemplate.execute(new RetryCallback<String>() {
    @Override
    public String retry(RetryContext context) throws Exception {
        return "Retry OK";
    }
});
 
// 指定重试策略，最多重试5次
retryTemplate.execute(new RetryCallback<String>() {
    @Override
    public String retry(RetryContext context) throws Exception {
        return "Retry OK";
    }
}, new SimpleRetryPolicy(5));
 
// 指定恢复逻辑，指定重试策略（最多重试10次）
retryTemplate.execute(new RetryCallback<String>() {
    @Override
    public String retry(RetryContext context) throws Exception {
        return "Retry OK";
    }
}, new RecoveryCallback<String>() {
    public String recover(RetryContext context) throws Exception {
        return "Recovery";
    }
}, new SimpleRetryPolicy(10));
```

### 异步重试例子
同同步差不多，但主要工作量在于配置上，配置好RetryCallback，RetryPolicy（可选），RecoveryCallback（可选），配置好MQ。

```xml
<!-- 重试逻辑 -->
<bean id="demoRetry" class="com.helijia.framework.retry.demo.DemoRetryCallback" />
<!-- 恢复逻辑 -->
<bean id="demoRecovery" class="com.helijia.framework.retry.demo.DemoRecoveryCallback" />
<!-- 重试策略， 最多重试3次，间隔分别为30秒钟，1分钟，2分钟 -->
<bean id="asyncRetryPolicy" class="com.helijia.framework.retry.policy.SimpleAsyncRetryPolicy">
    <constructor-arg index="0">
        <array>
            <value>30</value>     <!-- 第一次重试，间隔30秒钟 -->
            <value>60</value>     <!-- 第二次重试，间隔1分钟 -->
            <value>120</value>    <!-- 第三次重试，间隔2分钟 -->
        </array>
    </constructor-arg>
</bean>
<!-- 配置异步重试模板 -->
<bean id="asyncRetryTemplate" class="com.helijia.framework.retry.support.AsyncRetryTemplate">
    <property name="mqProducerService" ref="mqProducerService" />
    <property name="retries">
        <map>
            <entry key="Demo:AsyncRetry" value-ref="demoRetry" />
        </map>
    </property>
    <property name="recoveries">
        <map>
            <entry key="Demo:AsyncRetry" value-ref="demoRecovery" />
        </map>
    </property>
    <property name="policies">
        <map>
            <entry key="Demo:AsyncRetry" value-ref="asyncRetryPolicy" />
        </map>
    </property>
    <property name="policy" ref="asyncRetryPolicy" />
</bean>
<!-- 配置重试任务 -->
<bean id="asyncRetryJob" class="com.helijia.framework.retry.support.AsyncRetryJob">
    <property name="asyncRetryTemplate" ref="asyncRetryTemplate" />
</bean>
<!-- MQ相关配置， Retry框架底层依赖MQ -->
<bean id="mqProducerService" class="com.helijia.framework.mq.rocketmq.RocketMqProducerService" init-method="start" destroy-method="shutdown">
    <property name="address" value="172.16.254.20:9876" />
    <property name="group" value="retry" />
</bean>
<bean id="mqConsumerService" class="com.helijia.framework.mq.rocketmq.RocketMqConsumerService" init-method="start" destroy-method="shutdown">
    <property name="address" value="172.16.254.20:9876" />
    <property name="group" value="retry" />
    <property name="listener" ref="asyncRetryJob" />
    <property name="topics">
        <list>
            <value>Retry-Demo</value>
        </list>
    </property>
</bean>
```

使用上，非常方便，只需要 template.submitAsyncRetry(ctx); 即可。
```java
ConfigurableApplicationContext app = null;
try {
    app = new ClassPathXmlApplicationContext("retry/ApplicationContext-Retry-Demo.xml");
    AsyncRetryTemplate template = app.getBean("asyncRetryTemplate", AsyncRetryTemplate.class);
    SimpleAsyncRetryContext ctx = new SimpleAsyncRetryContext("Demo", "AsyncRetry", JSON.toJSONBytes(new Date()));
    template.submitAsyncRetry(ctx);
    Thread.sleep(5 * 60 * 1000);
} catch (Exception e) {
    System.out.println(e);
} finally {
    if (app != null) {
        app.close();
    }
}
```
* 关于KEY的说明
```txt
异步重试有两个概念，retry type 和 retry name。
retry type，表明重试类型，一般一个模块为一个类型，比如Pay，User等；
retry name，表明重试名，在retry type下需要唯一，定义重试名。
 
配置中，retry callback key，recovery callback key， retry policy key，规则是： 【${retry type}:${retry name}】唯一定位；
MQ Topic，规则是：【Retry-${retry type}】；也就是说一个retry type独占一个MQ Topic。
```

# 备注
#### 演示代码
<a href="https://github.com/stone2083/retry/tree/master/hlj.retry/src/test/java/com/helijia/framework/retry/demo">演示代码</a>

#### 时间间隔
```txt
Retry框架，依赖了MQ的底层实现，所以在延迟时间上，只支持：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
如果选择其他时间，则会就近落在上面的某个时间上。
```
