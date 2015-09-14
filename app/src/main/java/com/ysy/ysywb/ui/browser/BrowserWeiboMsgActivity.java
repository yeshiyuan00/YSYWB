package com.ysy.ysywb.ui.browser;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.ui.AbstractMainActivity;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 9:40
 */
public class BrowserWeiboMsgActivity extends AbstractMainActivity {
    private WeiboMsgBean msg;
    private WeiboMsgBean retweetMsg;

    private TextView username;
    private TextView content;
    private TextView recontent;
    private TextView time;

    private Button comment_number;
    private Button retweet_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browserweibomsgactivity_layout);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("detail");


        Intent intent = getIntent();
        msg = (WeiboMsgBean) intent.getSerializableExtra("msg");
        retweetMsg = msg.getRetweeted_status();

        buildView();
        buildViewData();
    }

    private void buildView() {
        username = (TextView) findViewById(R.id.username);
        content = (TextView) findViewById(R.id.content);
        recontent = (TextView) findViewById(R.id.recontent);
        time = (TextView) findViewById(R.id.time);

        comment_number = (Button) findViewById(R.id.comment_number);
        retweet_number = (Button) findViewById(R.id.retweet_number);
        comment_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BrowserWeiboMsgActivity.this, BrowserCommentListActivity.class));
            }
        });

        retweet_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BrowserWeiboMsgActivity.this, BrowserRepostListActivity.class));
            }
        });
    }

    private void buildViewData() {

        username.setText(msg.getUser().getScreen_name());
        content.setText(msg.getText());
        time.setText(msg.getCreated_at());

        comment_number.setText("comments:" + msg.getComments_count());
        retweet_number.setText("retweets:" + msg.getReposts_count());

        if (retweetMsg != null) {
            recontent.setVisibility(View.VISIBLE);
            recontent.setText(retweetMsg.getUser().getScreen_name() + "--" + retweetMsg.getText());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browserweibomsgactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class UpdateMsgTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            buildViewData();
            super.onPostExecute(aVoid);
        }
    }
}
