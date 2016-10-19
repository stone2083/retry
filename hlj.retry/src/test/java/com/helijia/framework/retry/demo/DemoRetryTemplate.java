package com.helijia.framework.retry.demo;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.helijia.framework.retry.policy.SimpleRetryPolicy;
import com.helijia.framework.retry.support.RetryTemplate;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class DemoRetryTemplate {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext app = null;
        try {
            app = new ClassPathXmlApplicationContext("retry/ApplicationContext-Retry-Demo.xml");
            RetryTemplate template = app.getBean("retryTemplate", RetryTemplate.class);
            DemoRetryCallback demoRetry = app.getBean("demoRetry", DemoRetryCallback.class);
            DemoRecoveryCallback demoRecovery = app.getBean("demoRecovery", DemoRecoveryCallback.class);
            String ret = null;
            // 模拟：重试三次成功
            ret = template.execute(demoRetry, new SimpleRetryPolicy(3));
            System.out.println(ret);
            // 模拟：重试二次失败，但没有配置Recovery
            try {
                template.execute(demoRetry, new SimpleRetryPolicy(2));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // 模拟：重试二次失败，但是配置了Recovery
            ret = template.execute(demoRetry, demoRecovery, new SimpleRetryPolicy(2));
            System.out.println(ret);

            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (app != null) {
                app.close();
            }
        }
    }

}
