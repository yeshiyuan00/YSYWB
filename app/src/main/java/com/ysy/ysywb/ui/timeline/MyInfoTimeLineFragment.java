package com.ysy.ysywb.ui.timeline;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.UserBean;
import com.ysy.ysywb.dao.OAuthDao;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;
import com.ysy.ysywb.ui.browser.SimpleBitmapWorkerTask;
import com.ysy.ysywb.ui.login.AccountActivity;
import com.ysy.ysywb.ui.main.MainTimeLineActivity;
import com.ysy.ysywb.ui.preference.SettingActivity;

/**
 * User: ysy
 * Date: 2015/9/10
 * Time: 14:56
 */
public class MyInfoTimeLineFragment extends Fragment {

    private UserBean bean;

    private ImageView avatar;
    private TextView username;
    private TextView info;
    private Button weibo_number;
    private Button following_number;
    private Button fans_number;
    private Button fav_number;

    private Commander commander;

    public static interface IUserInfo {
        public UserBean getUser();
    }


    public MyInfoTimeLineFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bean = ((IUserInfo) getActivity()).getUser();
        commander = ((AbstractAppActivity) getActivity()).getCommander();
        setValue();
        new SimpleTask().execute();
    }

    private void setValue() {
        username.setText(bean.getScreen_name());
        info.setText(bean.getDescription());
        String avatarUrl = bean.getAvatar_large();
        if (!TextUtils.isEmpty(avatarUrl)) {
            new SimpleBitmapWorkerTask(avatar).execute(avatarUrl);
        }
        setTextViewNum(weibo_number, bean.getStatuses_count());
        setTextViewNum(fans_number, bean.getFollowers_count());
        setTextViewNum(following_number, bean.getFriends_count());
        setTextViewNum(fav_number, bean.getFavourites_count());

    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_layout, container, false);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        username = (TextView) view.findViewById(R.id.username);
        info = (TextView) view.findViewById(R.id.textView_info);
        weibo_number = (Button) view.findViewById(R.id.weibo_number);
        following_number = (Button) view.findViewById(R.id.following_number);
        fans_number = (Button) view.findViewById(R.id.fans_number);
        fav_number = (Button) view.findViewById(R.id.fav_number);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.myinfofragment_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account_management:
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

        }
        return true;
    }

    private class SimpleTask extends AsyncTask<Object, UserBean, UserBean> {
        @Override
        protected UserBean doInBackground(Object... params) {
            UserBean user = new OAuthDao(((MainTimeLineActivity) getActivity()).getToken()).getOAuthUserInfo();
            if (user != null) {
                bean = user;
            }else {
                cancel(true);
            }
            return user;
        }

        @Override
        protected void onPostExecute(UserBean o) {
            setValue();
            super.onPostExecute(o);
        }
    }

    private void setTextViewNum(TextView tv, String num) {

        String name = tv.getText().toString();
        String value = "(" + num + ")";
        if (!name.endsWith(")")) {
            tv.setText(name + value);
        } else {
            int index = name.indexOf("(");
            String newName = name.substring(0, index);
            tv.setText(newName + value);
        }

    }

}
