package com.ysy.ysywb.ui.browser;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ysy.ysywb.R;
import com.ysy.ysywb.ui.AbstractMainActivity;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 9:40
 */
public class BrowserWeiboMsgActivity extends AbstractMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browserweibomsgactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
