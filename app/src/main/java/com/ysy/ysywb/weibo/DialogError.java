package com.ysy.ysywb.weibo;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 15:26
 */
public class DialogError extends Throwable {

    private static final long serialVersionUID = 1L;

    private int mErrorCode;

    private String mFailingUrl;

    public DialogError(String message, int errorCode, String failingUrl) {
        super(message);
        mErrorCode = errorCode;
        mFailingUrl = failingUrl;
    }

}
