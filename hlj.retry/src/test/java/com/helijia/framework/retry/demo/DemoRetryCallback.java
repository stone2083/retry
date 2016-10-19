package com.helijia.framework.retry.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.helijia.framework.retry.AsyncRetryContext;
import com.helijia.framework.retry.RetryCallback;
import com.helijia.framework.retry.RetryContext;

/**
 *
 * @author jinli Jun 2, 2016
 */
public class DemoRetryCallback implements RetryCallback<String> {

    private static final int DEFAULT_WHEN_FOR_SUCCESS = 3;

    private int whenForSuccess = DEFAULT_WHEN_FOR_SUCCESS;

    @Override
    public String retry(RetryContext ctx) throws Exception {
        if (ctx instanceof AsyncRetryContext) {
            AsyncRetryContext actx = (AsyncRetryContext) ctx;
            Date submit = JSON.parseObject(new String(actx.getBody()), Date.class);
            System.out.println(String.format("Retrying: [ctx=%s, submit=%s, retry=%s]", ctx, ftime(submit), ftime(new Date())));
        }

        if (whenForSuccess == ctx.getRetryCount()) {
            return "OK";
        }
        throw new RuntimeException("FAIL");
    }

    public void setWhenForSuccess(int whenForSuccess) {
        this.whenForSuccess = whenForSuccess;
    }

    private String ftime(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

}
