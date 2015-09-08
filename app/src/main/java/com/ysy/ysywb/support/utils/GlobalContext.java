package com.ysy.ysywb.support.utils;

import android.app.Application;

/**
 * User: ysy
 * Date: 2015/9/7
 */
public class GlobalContext extends Application {

    private static GlobalContext myApplication = null;

    private static String token = "";

    private static String expires = "";

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        GlobalContext.expires = expires;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static GlobalContext getInstance() {
        return myApplication;
    }

    public void setToken(String value) {
        token = value;
    }

    public String getToken() {
        return token;
    }
}
