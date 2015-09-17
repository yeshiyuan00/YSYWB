package com.ysy.ysywb.ui.timeline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ysy.ysywb.ui.login.AccountActivity;
import com.ysy.ysywb.ui.preference.SettingActivity;

/**
 * User: ysy
 * Date: 2015/9/10
 * Time: 14:56
 */
public class MyInfoTimeLineFragment extends Fragment {

    public static interface IUserInfo {
        public UserBean getUser();
    }

    private UserBean bean;


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
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_layout, container, false);
        bean = ((IUserInfo) getActivity()).getUser();
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        TextView username = (TextView) view.findViewById(R.id.username);
        TextView jshao = (TextView) view.findViewById(R.id.textView_info);
        Button weibo_number = (Button) view.findViewById(R.id.weibo_number);
        Button following_number = (Button) view.findViewById(R.id.following_number);
        Button fans_number = (Button) view.findViewById(R.id.fans_number);

        username.setText(bean.getScreen_name());
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


}
