package com.ysy.ysywb.support.utils;

import android.widget.Toast;

/**
 * User: ysy
 * Date: 2015/9/11
 * Time: 14:11
 */
public class ActivityUtils {
    public static void showTips(final String str) {
        GlobalContext.getInstance().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GlobalContext.getInstance(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
