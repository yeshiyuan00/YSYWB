package com.ysy.ysywb.ui;

import android.support.v4.app.FragmentActivity;

import com.ysy.ysywb.support.utils.GlobalContext;

/**
 * User: ysy
 * Date: 2015/9/10
 * Time: 16:00
 */
public class AbstractMainActivity extends FragmentActivity {


    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.getInstance().setActivity(this);
    }

}
