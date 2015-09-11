package com.ysy.ysywb.ui.timeline;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ysy.ysywb.R;
import com.ysy.ysywb.ui.login.AccountActivity;

/**
 * User: ysy
 * Date: 2015/9/10
 * Time: 14:56
 */
public class MyInfoTimeLineFragment extends Fragment {



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.myinfofragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account_management:
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
        return true;
    }


}
