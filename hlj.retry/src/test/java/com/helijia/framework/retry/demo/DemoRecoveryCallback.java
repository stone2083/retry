package com.helijia.framework.retry.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.helijia.framework.retry.AsyncRetryContext;
import com.helijia.framework.retry.RecoveryCallback;
import com.helijia.framework.retry.RetryContext;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class DemoRecoveryCallback implements RecoveryCallback<String> {

    @Override
    public String recover(RetryContext ctx) throws Exception {
        if (ctx instanceof AsyncRetryContext) {
            AsyncRetryContext actx = (AsyncRetryContext) ctx;
            Date submit = JSON.parseObject(new String(actx.getBody()), Date.class);
            System.out.println(String.format("Recovery: [ctx=%s, submit=%s, retry=%s]", ctx, ftime(submit), ftime(new Date())));
        }
        return "Recovery";
    }

    private String ftime(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

}
