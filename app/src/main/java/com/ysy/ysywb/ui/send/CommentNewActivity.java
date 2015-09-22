package com.ysy.ysywb.ui.send;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.CommentBean;
import com.ysy.ysywb.dao.CommentNewMsgDao;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 16:12
 */
public class CommentNewActivity extends AbstractAppActivity {
    private String id;

    private String token;

    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statusnewactivity_layout);

        content = ((EditText) findViewById(R.id.status_new_content));

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

    class SimpleTask extends AsyncTask<Void, Void, CommentBean> {

        String content1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content1 = ((EditText) findViewById(R.id.status_new_content)).getText().toString();
        }

        @Override
        protected CommentBean doInBackground(Void... params) {
            CommentNewMsgDao dao = new CommentNewMsgDao(token, id, content1);
            return dao.sendNewMsg();
        }
    }
}
