package com.ysy.ysywb.ui.browser;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.dao.StatusesShowMsgDao;
import com.ysy.ysywb.support.error.WeiboException;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 9:40
 */
public class BrowserWeiboMsgActivity extends AbstractAppActivity {
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
        //TODO 新浪微博禁用了根据ID获取单条微博的接口，只可以获取授权用户所发的微博
        //UpdateMsgTask task = new UpdateMsgTask();
        //task.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void buildView() {
        username = (TextView) findViewById(R.id.username);
        content = (TextView) findViewById(R.id.content);
        recontent = (TextView) findViewById(R.id.repost_content);
        time = (TextView) findViewById(R.id.time);

        avatar = (ImageView) findViewById(R.id.avatar);
        content_pic = (ImageView) findViewById(R.id.content_pic);
        repost_pic = (ImageView) findViewById(R.id.repost_content_pic);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BrowserWeiboMsgActivity.this, "ing", Toast.LENGTH_SHORT).show();
            }
        });
        content_pic.setOnClickListener(picOnClickListener);
        repost_pic.setOnClickListener(picOnClickListener);
    }

    private android.view.View.OnClickListener picOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(BrowserWeiboMsgActivity.this, "ing", Toast.LENGTH_SHORT).show();
        }
    };


    class TimeLinePagerAdapter extends FragmentPagerAdapter {

        List<Fragment> list = new ArrayList<Fragment>();

        public TimeLinePagerAdapter(FragmentManager fm) {
            super(fm);
            list.add(new RepostsByIdTimeLineFragment(token, msg.getId()));
            list.add(new CommentsByIdTimeLineFragment(token, msg.getId()));
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    private void buildViewData() {

        if (msg.getUser() != null) {
            username.setText(msg.getUser().getScreen_name());
            SimpleBitmapWorkerTask avatarTask = new SimpleBitmapWorkerTask(avatar);
            avatarTask.execute(msg.getUser().getProfile_image_url());
        }


        content.setText(msg.getText());
        time.setText(msg.getCreated_at());

        comment_sum = msg.getComments_count();
        retweet_sum = msg.getReposts_count();

        invalidateOptionsMenu();

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
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_repost:
                intent = new Intent(this, BrowserRepostAndCommentListActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", msg.getId());
                intent.putExtra("tabindex", 0);
                startActivity(intent);
                return true;
            case R.id.menu_comment:
                intent = new Intent(this, BrowserRepostAndCommentListActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", msg.getId());
                intent.putExtra("tabindex", 1);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class UpdateMsgTask extends AsyncTask<Void, Void, WeiboMsgBean> {
        WeiboException e;

        @Override
        protected WeiboMsgBean doInBackground(Void... params) {
            try {
                return new StatusesShowMsgDao(token, msg.getId()).getMsg();
            } catch (WeiboException e) {
                this.e = e;
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onCancelled(WeiboMsgBean weiboMsgBean) {
            dealWithException(e);
            setTextViewDeleted();
            super.onCancelled(weiboMsgBean);
        }

        @Override
        protected void onPostExecute(WeiboMsgBean newValue) {
            if (newValue != null && e == null) {
                msg = newValue;
                retweetMsg = msg.getRetweeted_status();
                buildViewData();
                invalidateOptionsMenu();
            }
            super.onPostExecute(newValue);
        }
    }
    private void setTextViewDeleted() {
        SpannableString ss = new SpannableString(content.getText().toString());
        ss.setSpan(new StrikethroughSpan(), 0, ss.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setText(ss);
    }
}
