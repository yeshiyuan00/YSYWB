package com.ysy.ysywb.ui.browser;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.dao.StatusesShowMsgDao;
import com.ysy.ysywb.ui.AbstractMainActivity;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 9:40
 */
public class BrowserWeiboMsgActivity extends AbstractMainActivity {
    private WeiboMsgBean msg;
    private WeiboMsgBean retweetMsg;
    private String token;

    private TextView username;
    private TextView content;
    private TextView recontent;
    private TextView time;

    private ImageView avatar;
    private ImageView content_pic;
    private ImageView repost_pic;


    private String comment_sum = "";
    private String retweet_sum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browserweibomsgactivity_layout);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.detail));


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        msg = (WeiboMsgBean) intent.getSerializableExtra("msg");
        retweetMsg = msg.getRetweeted_status();

        buildView();
        buildViewData();

        new UpdateMsgTask().execute();
    }

    private void buildView() {
        username = (TextView) findViewById(R.id.username);
        content = (TextView) findViewById(R.id.content);
        recontent = (TextView) findViewById(R.id.repost_content);
        time = (TextView) findViewById(R.id.time);

        avatar = (ImageView) findViewById(R.id.avatar);
        content_pic = (ImageView) findViewById(R.id.content_pic);
        repost_pic = (ImageView) findViewById(R.id.repost_content_pic);
    }

    private void buildViewData() {

        username.setText(msg.getUser().getScreen_name());
        content.setText(msg.getText());
        time.setText(msg.getCreated_at());

        comment_sum = msg.getComments_count();
        retweet_sum = msg.getReposts_count();

        invalidateOptionsMenu();
        SimpleBitmapWorkerTask avatarTask = new SimpleBitmapWorkerTask(avatar);
        avatarTask.execute(msg.getUser().getProfile_image_url());


        if (retweetMsg != null) {
            recontent.setVisibility(View.VISIBLE);
            if (retweetMsg.getUser() != null) {
                recontent.setText(retweetMsg.getUser().getScreen_name() + "：" + retweetMsg.getText());
            } else {
                recontent.setText(retweetMsg.getText());

            }
            if (!TextUtils.isEmpty(retweetMsg.getBmiddle_pic())) {
                repost_pic.setVisibility(View.VISIBLE);
                SimpleBitmapWorkerTask task = new SimpleBitmapWorkerTask(repost_pic);
                task.execute(retweetMsg.getBmiddle_pic());
            } else if (!TextUtils.isEmpty(retweetMsg.getThumbnail_pic())) {
                repost_pic.setVisibility(View.VISIBLE);
                SimpleBitmapWorkerTask task = new SimpleBitmapWorkerTask(repost_pic);
                task.execute(retweetMsg.getThumbnail_pic());
            }

        }
        if (!TextUtils.isEmpty(msg.getBmiddle_pic())) {
            content_pic.setVisibility(View.VISIBLE);
            SimpleBitmapWorkerTask task = new SimpleBitmapWorkerTask(content_pic);
            task.execute(msg.getBmiddle_pic());
        } else if (!TextUtils.isEmpty(msg.getThumbnail_pic())) {
            content_pic.setVisibility(View.VISIBLE);
            SimpleBitmapWorkerTask task = new SimpleBitmapWorkerTask(content_pic);
            task.execute(msg.getThumbnail_pic());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browserweibomsgactivity_menu, menu);
        menu.getItem(0).setTitle(menu.getItem(0).getTitle() + "(" + retweet_sum + ")");
        menu.getItem(1).setTitle(menu.getItem(1).getTitle() + "(" + comment_sum + ")");
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

    class UpdateMsgTask extends AsyncTask<Void, Void, WeiboMsgBean> {

        @Override
        protected WeiboMsgBean doInBackground(Void... params) {
            return new StatusesShowMsgDao(token, msg.getId()).getMsg();
        }

        @Override
        protected void onPostExecute(WeiboMsgBean newValue) {
            if (newValue != null) {
                msg = newValue;
                retweetMsg = msg.getRetweeted_status();
                buildViewData();
            }
            super.onPostExecute(newValue);
        }
    }
}
