package com.ysy.ysywb.ui.main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ListView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.AccountBean;
import com.ysy.ysywb.bean.UserBean;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;
import com.ysy.ysywb.ui.Abstract.IAccountInfo;
import com.ysy.ysywb.ui.timeline.CommentsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.FriendsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.MentionsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.MyInfoTimeLineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 15:04
 */
public class MainTimeLineActivity extends AbstractAppActivity implements
        MyInfoTimeLineFragment.IUserInfo, IAccountInfo {

    private ViewPager mViewPager = null;

    private String token = "";

    private AccountBean accountBean = null;


    private ListView homeListView = null;
    private ListView mentionsListView = null;
    private ListView commentsListView = null;

    public void setHomeListView(ListView homeListView) {
        this.homeListView = homeListView;
    }

    public void setMentionsListView(ListView mentionsListView) {
        this.mentionsListView = mentionsListView;
    }

    public void setCommentsListView(ListView commentsListView) {
        this.commentsListView = commentsListView;
    }

    public String getToken() {
        return token;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintimelineactivity_viewpager_layout);

        Intent intent = getIntent();

        accountBean = (AccountBean) intent.getSerializableExtra("account");
        token = accountBean.getAccess_token();

        buildViewPager();
        buildActionBarAndViewPagerTitles();


    }


    private void buildViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TimeLinePagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    private void buildActionBarAndViewPagerTitles() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
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
        boolean home = false;
        boolean mentions = false;
        boolean comments = false;

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(tab.getPosition());
            switch (tab.getPosition()) {
                case 0:
                    home = true;
                    break;
                case 1:
                    mentions = true;
                    break;
                case 2:
                    comments = true;
                    break;
                case 3:
                    break;
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            switch (tab.getPosition()) {
                case 0:
                    home = false;
                    break;
                case 1:
                    mentions = false;
                    break;
                case 2:
                    comments = false;
                    break;
                case 3:
                    break;
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            switch (tab.getPosition()) {
                case 0:
                    if (home) homeListView.smoothScrollToPosition(0);
                    break;
                case 1:
                    if (mentions) mentionsListView.smoothScrollToPosition(0);
                    break;
                case 2:
                    if (comments) mentionsListView.smoothScrollToPosition(0);
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    public UserBean getUser() {
        UserBean bean = new UserBean();
        bean.setScreen_name(accountBean.getUsernick());
        bean.setId(accountBean.getUid());
        return bean;
    }

    @Override
    public AccountBean getAccount() {
        return accountBean;
    }

    public MainTimeLineActivity() {
    }

    ;


    class TimeLinePagerAdapter extends
            FragmentStatePagerAdapter {

        List<Fragment> list = new ArrayList<Fragment>();

        public TimeLinePagerAdapter(FragmentManager fm) {
            super(fm);

            list.add(new FriendsTimeLineFragment());
            list.add(new MentionsTimeLineFragment());
            list.add(new CommentsTimeLineFragment());

            list.add(new MyInfoTimeLineFragment());
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

}
