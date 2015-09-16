package com.ysy.ysywb.ui.main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.WeiboAccountBean;
import com.ysy.ysywb.support.utils.GlobalContext;
import com.ysy.ysywb.ui.AbstractMainActivity;
import com.ysy.ysywb.ui.timeline.AbstractTimeLineFragment;
import com.ysy.ysywb.ui.timeline.CommentsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.FriendsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.MentionsTimeLineFragment;
import com.ysy.ysywb.ui.timeline.MyInfoTimeLineFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 15:04
 */
public class MainTimeLineActivity extends AbstractMainActivity {

    private ViewPager mViewPager = null;
    private TimeLinePagerAdapter timeLinePagerAdapter = null;

    private String token = "";

    private WeiboAccountBean weiboAccountBean = null;

    Map<String, AvatarBitmapWorkerTask> avatarBitmapWorkerTaskHashMap = new ConcurrentHashMap<String, AvatarBitmapWorkerTask>();
    Map<String, PictureBitmapWorkerTask> pictureBitmapWorkerTaskMap = new ConcurrentHashMap<String, PictureBitmapWorkerTask>();

    public Map<String, AvatarBitmapWorkerTask> getAvatarBitmapWorkerTaskHashMap() {
        return avatarBitmapWorkerTaskHashMap;
    }

    public Map<String, PictureBitmapWorkerTask> getPictureBitmapWorkerTaskMap() {
        return pictureBitmapWorkerTaskMap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintimelineactivity_viewpager_layout);

        Intent intent = getIntent();

        weiboAccountBean = (WeiboAccountBean) intent.getSerializableExtra("account");
        token = weiboAccountBean.getAccess_token();

        // homeList = DatabaseManager.getInstance().getHomeLineMsgList();

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

    private Bitmap getBitmapFromMemCache(String key) {
        return GlobalContext.getInstance().getAvatarCache().get(key);
    }

    FriendsTimeLineFragment.Commander frinedsTimeLineMsgCommand = new FriendsTimeLineFragment.Commander() {


        @Override
        public void downloadAvatar(ImageView view, String urlKey, int position, ListView listView) {
            Bitmap bitmap = getBitmapFromMemCache(urlKey);
            if (bitmap != null) {
                view.setImageBitmap(bitmap);
                avatarBitmapWorkerTaskHashMap.remove(urlKey);
            } else {
                view.setImageDrawable(getResources().getDrawable(R.drawable.account));
                if (avatarBitmapWorkerTaskHashMap.get(urlKey) == null) {
                    AvatarBitmapWorkerTask avatarTask = new AvatarBitmapWorkerTask(
                            GlobalContext.getInstance().getAvatarCache(),
                            avatarBitmapWorkerTaskHashMap, view, listView, position);
                    avatarTask.execute(urlKey);
                    avatarBitmapWorkerTaskHashMap.put(urlKey, avatarTask);
                }
            }
        }

        @Override
        public void downContentPic(ImageView view, String urlKey, int position, ListView listView) {
            Bitmap bitmap = getBitmapFromMemCache(urlKey);
            if (bitmap != null) {
                view.setImageBitmap(bitmap);
                pictureBitmapWorkerTaskMap.remove(urlKey);
            } else {
                view.setImageDrawable(getResources().getDrawable(R.drawable.picture));
                if (pictureBitmapWorkerTaskMap.get(urlKey) == null) {
                    PictureBitmapWorkerTask avatarTask = new PictureBitmapWorkerTask(
                            GlobalContext.getInstance().getAvatarCache(),
                            pictureBitmapWorkerTaskMap, view, listView, position);
                    avatarTask.execute(urlKey);
                    pictureBitmapWorkerTaskMap.put(urlKey, avatarTask);
                }
            }

        }

    };

    class TimeLinePagerAdapter extends
            FragmentStatePagerAdapter {

        List<Fragment> list = new ArrayList<Fragment>();

        public TimeLinePagerAdapter(FragmentManager fm) {
            super(fm);

            AbstractTimeLineFragment home = new FriendsTimeLineFragment().setCommander(frinedsTimeLineMsgCommand);
            AbstractTimeLineFragment mentions = new MentionsTimeLineFragment().setCommander(frinedsTimeLineMsgCommand);

            Fragment comments = new CommentsTimeLineFragment().setCommander(frinedsTimeLineMsgCommand);

            MyInfoTimeLineFragment info = new MyInfoTimeLineFragment();

            info.setAccountBean(weiboAccountBean);

            list.add(home);
            list.add(mentions);
            list.add(comments);

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

    public String getToken() {
        return token;
    }

}
