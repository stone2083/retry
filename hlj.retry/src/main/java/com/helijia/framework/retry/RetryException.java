package com.helijia.framework.retry;

/**
 * 重试异常
 * 
 * @author jinli Jun 2, 2016
 */
public class RetryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RetryException() {
        super();
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryException(String message) {
        super(message);
    }

    public RetryException(Throwable cause) {
        super(cause);
    }

}
