package com.ysy.ysywb.ui.send;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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

    private EditText et = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statusnewactivity_layout);

        token = getIntent().getStringExtra("token");
        id = getIntent().getStringExtra("id");
        getActionBar().setTitle(getString(R.string.repost));

        et = ((EditText) findViewById(R.id.status_new_content));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repostnewactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_send:

                new SimpleTask().execute();

                break;
        }
        return true;
    }

    class SimpleTask extends AsyncTask<Void, Void, WeiboMsgBean> {

        SendProgressFragment progressFragment = new SendProgressFragment();

        @Override
        protected void onPreExecute() {
            progressFragment.onCancel(new DialogInterface() {

                @Override
                public void cancel() {
                    SimpleTask.this.cancel(true);
                }

                @Override
                public void dismiss() {
                    SimpleTask.this.cancel(true);
                }
            });

            progressFragment.show(getFragmentManager(), "");
        }

        @Override
        protected WeiboMsgBean doInBackground(Void... params) {
            String content = et.getText().toString();
            if (TextUtils.isEmpty(content)) {
                content = "repost";
            }
            RepostNewMsgDao dao = new RepostNewMsgDao(token, id);
            dao.setStatus(content);
            return dao.sendNewMsg();
        }

        @Override
        protected void onPostExecute(WeiboMsgBean s) {
            progressFragment.dismissAllowingStateLoss();
            if (s != null) {
                finish();
                Toast.makeText(RepostNewActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RepostNewActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }
}
