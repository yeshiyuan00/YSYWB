package com.ysy.ysywb.ui;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.TimeLineMsgList;
import com.ysy.ysywb.bean.WeiboMsg;
import com.ysy.ysywb.dao.FriendsTimeLineMsgDao;
import com.ysy.ysywb.ui.send.StatusNewActivity;
import com.ysy.ysywb.ui.timeline.AbstractTimeLineFragment;
import com.ysy.ysywb.ui.timeline.CommentsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.FriendsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.MailsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.MentionsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.MyInfoTimeLineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 15:04
 */
public class MainTimeLineActivity extends AbstractMainActivity {

    private ViewPager mViewPager = null;
    private TimeLinePagerAdapter timeLinePagerAdapter = null;

    private String token = "";

    private String screen_name = "";
    private AbstractTimeLineFragment home = null;
    private AbstractTimeLineFragment mentions = null;
    private AbstractTimeLineFragment comments = null;
    private AbstractTimeLineFragment mails = null;
    private Fragment info = null;

    private TimeLineMsgList homeList = new TimeLineMsgList();
    private TimeLineMsgList mentionList = new TimeLineMsgList();
    private TimeLineMsgList commentList = new TimeLineMsgList();
    private TimeLineMsgList mailList = new TimeLineMsgList();


    private int homelist_position = 0;
    private int mentionList_position = 0;
    private int commentList_position = 0;
    private int mailList_position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintimelineactivity_viewpager_layout);

        Intent intent = getIntent();

        token = intent.getStringExtra("token");
        screen_name = intent.getStringExtra("screen_name");

        buildViewPager();
        buildActionBarAndViewPagerTitles();


    }

    private void buildViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        timeLinePagerAdapter = new TimeLinePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(timeLinePagerAdapter);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    private void buildActionBarAndViewPagerTitles() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(screen_name);
        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.home))
                .setTabListener(tabListener));

        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.mentions))
                .setTabListener(tabListener));

        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.comments))
                .setTabListener(tabListener));

        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.mail))
                .setTabListener(tabListener));

        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.info))
                .setTabListener(tabListener));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            getActionBar().setSelectedNavigationItem(position);
        }
    };

    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    };


    FriendsTimeLineFragment.Commander frinedsTimeLineMsgCommand = new FriendsTimeLineFragment.Commander() {
        @Override
        public void getNewFriendsTimeLineMsg() {

            new FriendsTimeLineTask().execute();

        }

        @Override
        public void replayTo(int position) {

        }

        @Override
        public void newWeibo() {
            Intent intent = new Intent(MainTimeLineActivity.this, StatusNewActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
        }
    };

    class TimeLinePagerAdapter extends
            FragmentStatePagerAdapter {

        List<Fragment> list = new ArrayList<Fragment>();

        public TimeLinePagerAdapter(FragmentManager fm) {
            super(fm);

            home = new FriendsTimeLineFragment().setCommander(frinedsTimeLineMsgCommand);
            mentions = new MentionsTimeLineFragment();
            comments = new CommentsTimeLineFragment();
            mails = new MailsTimeLineFragment();
            info = new MyInfoTimeLineFragment();
//            home.setToken(token);
//            mentions.setToken(token);

            list.add(home);
            list.add(mentions);
            list.add(comments);
            list.add(mails);
            list.add(info);
        }

        @Override
        public Fragment getItem(int i) {

            return list.get(i);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }

    class FriendsTimeLineTask extends AsyncTask<Void, TimeLineMsgList, TimeLineMsgList> {

        DialogFragment dialogFragment = new ProgressFragment();

        @Override
        protected void onPreExecute() {
            dialogFragment.show(getSupportFragmentManager(), "");
        }

        @Override
        protected TimeLineMsgList doInBackground(Void... params) {

            FriendsTimeLineMsgDao dao = new FriendsTimeLineMsgDao(token);

            WeiboMsg msg = homeList.getStatuses().get(0);

            if (msg != null) {
                dao.setSince_id(msg.getId());
            }
            return dao.getGSONMsgList();
        }

        @Override
        protected void onPostExecute(TimeLineMsgList newValue) {
            if (newValue != null) {
                setHomeList(newValue);

                Toast.makeText(MainTimeLineActivity.this, "" + getHomeList().getStatuses().size(), Toast.LENGTH_SHORT).show();

                home.refreshAndScrollTo(homelist_position);
                //   listView.smoothScrollToPosition(activity.getHomelist_position());

            }
            dialogFragment.dismissAllowingStateLoss();
            super.onPostExecute(newValue);
        }
    }

    static class ProgressFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("刷新中");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);

            return dialog;
        }
    }


    public void setHomelist_position(int homelist_position) {
        this.homelist_position = homelist_position;
    }

    public String getToken() {
        return token;
    }

    public TimeLineMsgList getHomeList() {
        return homeList;
    }

    public void setHomeList(TimeLineMsgList homeList) {
        this.homeList = homeList;
    }
}
