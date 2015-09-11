package com.ysy.ysywb.ui.timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.TimeLineMsgList;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 15:19
 */
public class MentionsTimeLineFragment extends AbstractTimeLineFragment {

    @Override
    public void refresh() {

    }

    @Override
    protected TimeLineMsgList getList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void scrollToBottom() {

    }

    @Override
    protected void listViewItemLongClick(AdapterView parent, View view, int position, long id) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.fragment_listview_layout, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        timeLineAdapter = new TimeLineAdapter();
        listView.setAdapter(timeLineAdapter);

//        new AsyncTask<Void, List<Map<String, String>>, List<Map<String, String>>>() {
//
//            @Override
//            protected List<Map<String, String>> doInBackground(Void... params) {
//                return new TimeLineMentionsMsg().getMsgList();
//            }
//
//            @Override
//            protected void onPostExecute(List<Map<String, String>> o) {
//                list = o;
//                timeLineAdapter.notifyDataSetChanged();
//                super.onPostExecute(o);
//            }
//        }.execute();
        return view;
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

}
