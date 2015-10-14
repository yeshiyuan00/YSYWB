package com.ysy.ysywb.ui.browser;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ysy.ysywb.R;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/22
 * Time: 10:11
 */
public class BrowserRepostAndCommentListActivity extends AbstractAppActivity {

    private String token = "";
    private String id = "";

    private ViewPager mViewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintimelineactivity_viewpager_layout);

        token = getIntent().getStringExtra("token");
        id = getIntent().getStringExtra("id");

        buildViewPager();
        buildActionBarAndViewPagerTitles();
    }

    private void buildViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TimeLinePagerAdapter adapter = new TimeLinePagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(onPageChangeListener);

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                Integer index = getIntent().getIntExtra("tabindex", 0);
                mViewPager.setCurrentItem(index);
            }
        });
    }

    private void buildActionBarAndViewPagerTitles() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (getResources().getBoolean(R.bool.is_phone)) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.repost))
                .setTabListener(tabListener));

        actionBar.addTab(actionBar.newTab()
                .setText(getString(R.string.comments))
                .setTabListener(tabListener));

        Integer index = getIntent().getIntExtra("tabindex", 0);
        getActionBar().setSelectedNavigationItem(index);
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

    class TimeLinePagerAdapter extends FragmentPagerAdapter {

        List<Fragment> list = new ArrayList<Fragment>();

        public TimeLinePagerAdapter(FragmentManager fm) {
            super(fm);
            list.add(new RepostsByIdTimeLineFragment(token, id));
            list.add(new CommentsByIdTimeLineFragment(token, id));
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
}
