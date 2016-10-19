package com.helijia.framework.retry.support;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.helijia.framework.retry.RecoveryCallback;
import com.helijia.framework.retry.RetryCallback;
import com.helijia.framework.retry.RetryContext;
import com.helijia.framework.retry.RetryException;
import com.helijia.framework.retry.RetryListener;
import com.helijia.framework.retry.listener.TestCaseRetryListener;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class RetryTemplateTest {

    private RetryTemplate retryTemplate;

    @Before
    public void init() {
        retryTemplate = new RetryTemplate();
        RetryListener l = new TestCaseRetryListener();
        retryTemplate.setRetryListeners(Arrays.asList(l));
    }

    @Test
    public void test_illegal_arguments() {
        try {
            retryTemplate.execute(null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("retry callback can not be null."));
        }
    }

    @Test
    public void test_retry_success() {
        try {
            String ret = retryTemplate.execute(new RetryCallback<String>() {

                @Override
                public String retry(RetryContext context) throws Exception {
                    if (context.getRetryCount() >= 2) {
                        return "OK";
                    }
                    throw new Exception();
                }
            });
            Assert.assertEquals("OK", ret);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test_retry_exhausted() {
        try {
            retryTemplate.execute(new RetryCallback<String>() {

                @Override
                public String retry(RetryContext context) throws Exception {
                    throw new Exception();
                }
            });
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(RetryException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("retry exhausted."));
            Assert.assertTrue(e.getMessage().contains("retryCount=3"));
        }
    }

    @Test
    public void test_retry_with_recover_callback() {
        try {
            String ret = retryTemplate.execute(new RetryCallback<String>() {

                @Override
                public String retry(RetryContext context) throws Exception {
                    throw new Exception();
                }
            }, new RecoveryCallback<String>() {

                @Override
                public String recover(RetryContext context) throws Exception {
                    return "Recovery";
                }
            });
            Assert.assertEquals("Recovery", ret);
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
