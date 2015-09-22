package com.ysy.ysywb.ui.send;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.dao.RepostNewMsgDao;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 16:12
 */
public class RepostNewActivity extends AbstractAppActivity {
    private String rePostContent;

    private String id;

    private String token;

    private EditText content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statusnewactivity_layout);

        Intent intent = getIntent();
        rePostContent = intent.getStringExtra("repost_content");

        content = ((EditText) findViewById(R.id.status_new_content));
        content.setText(rePostContent);

        token = getIntent().getStringExtra("token");
        id = getIntent().getStringExtra("id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statusnewactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_send:

                final String content = ((EditText) findViewById(R.id.status_new_content)).getText().toString();

                if (!TextUtils.isEmpty(content)) {


                    new SimpleTask().execute();

                }
                break;
        }
        return true;
    }
    class SimpleTask extends AsyncTask<Void, Void, WeiboMsgBean> {

        String content1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content1=((EditText) findViewById(R.id.status_new_content)).getText().toString();
        }

        @Override
        protected WeiboMsgBean doInBackground(Void... params) {
            RepostNewMsgDao dao = new RepostNewMsgDao(token, id);
            dao.setStatus(content1);
            return dao.sendNewMsg();
        }
    }
}
