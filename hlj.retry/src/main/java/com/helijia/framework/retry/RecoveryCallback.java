package com.helijia.framework.retry;

/**
 * <pre>
 * 重试N次不成功，最终执行恢复操作
 * 比如日志记录，短信报警等等。
 * </pre>
 * 
 * @author jinli Jun 1, 2016
 */
public interface RecoveryCallback<T> {

    /**
     * 
     * @param ctx
     * @return
     * @throws Exception
     */
    T recover(RetryContext ctx) throws Exception;

}
