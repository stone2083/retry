package com.helijia.framework.retry.demo;

import java.util.Date;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.helijia.framework.retry.support.AsyncRetryTemplate;
import com.helijia.framework.retry.support.SimpleAsyncRetryContext;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class DemoAsyncRetryTemplate {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext app = null;
        try {
            app = new ClassPathXmlApplicationContext("retry/ApplicationContext-Retry-Demo.xml");
            AsyncRetryTemplate template = app.getBean("asyncRetryTemplate", AsyncRetryTemplate.class);

            SimpleAsyncRetryContext ctx;

            System.out.println("\n==========演示重试，第三次成功===========");
            ctx = new SimpleAsyncRetryContext("Demo", "AsyncRetry", JSON.toJSONBytes(new Date()));
            template.submitAsyncRetry(ctx);
            Thread.sleep(5 * 60 * 1000);

            System.out.println("\n==========演示重试一次后失败，不做Recovery===========");
            ctx = new SimpleAsyncRetryContext("Demo", "AsyncRetry-Exhausted", JSON.toJSONBytes(new Date()));
            template.submitAsyncRetry(ctx);
            Thread.sleep(1 * 60 * 1000);

            System.out.println("\n==========演示重试一次后失败，执行Recovery===========");
            ctx = new SimpleAsyncRetryContext("Demo", "AsyncRetry-Recovery", JSON.toJSONBytes(new Date()));
            template.submitAsyncRetry(ctx);
            Thread.sleep(1 * 60 * 1000);
            
            System.out.println("\n==========演示结束，欢迎使用===========");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (app != null) {
                app.close();
            }
        }
    }

}
