package com.ysy.ysywb.ui.browser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.WeiboMsg;
import com.ysy.ysywb.ui.AbstractMainActivity;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 9:40
 */
public class BrowserWeiboMsgActivity extends AbstractMainActivity {
    private WeiboMsg msg;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browserweibomsgactivity_layout);
        tv = (TextView) findViewById(R.id.textView);


        Intent intent = getIntent();
        msg = (WeiboMsg) intent.getSerializableExtra("msg");
        tv.setText(msg.getText());
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
