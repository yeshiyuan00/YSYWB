package com.ysy.ysywb.ui.timeline;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ysy.ysywb.R;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 15:19
 */
public class MentionsTimeLineFragment extends AbstractTimeLineFragment {



    private Commander commander;


    public static abstract class Commander {

        public volatile boolean isBusying = false;

        public void getNewFriendsTimeLineMsgList() {
        }

        public void getOlderFriendsTimeLineMsgList() {
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mentionstimelinefragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.friendstimelinefragment_refresh:

                break;
        }
        return true;
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    protected void scrollToBottom() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void listViewItemLongClick(AdapterView parent, View view, int position, long id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void listViewItemClick(AdapterView parent, View view, int position, long id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    protected void listViewFooterViewClick(View view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void downloadAvatar(ImageView view, String url, int position, ListView listView) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void downContentPic(ImageView view, String url, int position, ListView listView) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
