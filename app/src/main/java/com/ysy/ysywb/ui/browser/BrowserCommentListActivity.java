package com.ysy.ysywb.ui.browser;

import android.app.Activity;
import android.os.Bundle;

import com.ysy.ysywb.R;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 15:42
 */
public class BrowserCommentListActivity extends Activity {

    private String token = "";
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsercommentlistactivity_layout);

        token = getIntent().getStringExtra("token");
        id = getIntent().getStringExtra("id");
    }
}
