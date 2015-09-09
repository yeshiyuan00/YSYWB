package com.ysy.ysywb.ui.timeline;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.dao.TimeLineFriendsMsg;
import com.ysy.ysywb.ui.send.StatusNewActivity;

import java.util.List;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:03
 */
public class TimeLineFriendsFragment extends TimeLineAbstractFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.timeline, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        timeLineAdapter = new TimeLineAdapter();
        listView.setAdapter(timeLineAdapter);

        new TimeLineTask().execute();

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friendstimelinefragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_weibo:

                startActivity(new Intent(getActivity(), StatusNewActivity.class));

                break;
            case R.id.menu_refresh_timeline:

                new TimeLineTask().execute();
                break;
        }
        return true;
    }

    class TimeLineTask extends AsyncTask<Void, List<Map<String, String>>, List<Map<String, String>>> {
        DialogFragment dialogFragment = ProgressFragment.newInstance();

        @Override
        protected void onPreExecute() {
            dialogFragment.show(getFragmentManager(), "");
        }

        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {
            return new TimeLineFriendsMsg().getMsgList();
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> o) {
            list=o;
            Toast.makeText(getActivity(), "" + list.size(), Toast.LENGTH_SHORT).show();
            dialogFragment.dismissAllowingStateLoss();
            timeLineAdapter.notifyDataSetChanged();
            super.onPostExecute(o);
        }
    }

    static class ProgressFragment extends DialogFragment {
        public static ProgressFragment newInstance() {
            ProgressFragment frag = new ProgressFragment();
            frag.setRetainInstance(true); //注意这句
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("刷新中");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            return dialog;
        }
    }
}
