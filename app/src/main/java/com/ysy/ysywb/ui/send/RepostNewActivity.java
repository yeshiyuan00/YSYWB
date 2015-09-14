package com.ysy.ysywb.ui.send;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ysy.ysywb.R;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 16:12
 */
public class RepostNewActivity extends AbstractSendActivity {
    private String rePostContent;

    private EditText content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        rePostContent = intent.getStringExtra("repost_content");

        content= ((EditText) findViewById(R.id.status_new_content));
        content.setText(rePostContent);
    }
    @Override
    protected void executeTask(String content) {

    }
}
