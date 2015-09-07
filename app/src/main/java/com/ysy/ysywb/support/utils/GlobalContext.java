package com.ysy.ysywb.support.utils;

import android.app.Application;

/**
 * User: ysy
 * Date: 2015/9/7
 */
public class GlobalContext extends Application {

    private static GlobalContext myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static GlobalContext getInstance() {
        return myApplication;
    }
}
