package com.ysy.ysywb.ui.timeline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.dao.FriendsTimeLineMsg;

import java.util.List;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:03
 */
public class TimeLineFriendsFragment extends TimeLineAbstractFragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.timeline, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        timeLineAdapter = new TimeLineAdapter();
        listView.setAdapter(timeLineAdapter);

        new AsyncTask<Void, List<Map<String, String>>, List<Map<String, String>>>() {


            @Override
            protected List<Map<String, String>> doInBackground(Void... params) {
                return new FriendsTimeLineMsg().getMsgList();
            }

            @Override
            protected void onPostExecute(List<Map<String, String>> o) {
                list = o;
                timeLineAdapter.notifyDataSetChanged();
                super.onPostExecute(o);
            }
        }.execute();

        return view;
    }

}
