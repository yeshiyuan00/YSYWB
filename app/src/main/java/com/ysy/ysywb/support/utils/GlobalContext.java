package com.ysy.ysywb.support.utils;

import android.app.Activity;
import android.app.Application;

/**
 * User: ysy
 * Date: 2015/9/7
 */
public class GlobalContext extends Application {

    private static GlobalContext myApplication = null;

    private boolean isAppForeground = false;

    private Activity activity = null;


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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
