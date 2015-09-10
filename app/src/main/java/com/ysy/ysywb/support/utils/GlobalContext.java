package com.ysy.ysywb.support.utils;

import android.app.Application;

/**
 * User: ysy
 * Date: 2015/9/7
 */
public class GlobalContext extends Application {

    private static GlobalContext myApplication = null;

    private boolean isAppForeground = false;


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static GlobalContext getInstance() {
        return myApplication;
    }

    public void setAppForegroundFlag() {
        isAppForeground = true;
    }

    public void removeAppForegroundFlag() {
        isAppForeground = false;
    }

    public boolean isAppForeground() {
        return isAppForeground;
    }


}
