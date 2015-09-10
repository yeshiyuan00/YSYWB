package com.ysy.ysywb.ui.timeline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.TimeLineMsgList;
import com.ysy.ysywb.dao.TimeLineFriendsMsg;
import com.ysy.ysywb.ui.MainTimeLineActivity;
import com.ysy.ysywb.ui.send.StatusNewActivity;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:03
 */
public class TimeLineFriendsFragment extends TimeLineAbstractFragment {

    @Override
    protected TimeLineMsgList getList() {
        return activity.getHomeList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setHasOptionsMenu(true);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.fragment_listview_layout, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        timeLineAdapter = new TimeLineAdapter();
        listView.setAdapter(timeLineAdapter);
        listView.setOnItemLongClickListener(onItemLongClickListener);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                activity.setHomelist_position(firstVisibleItem);
            }
        });
        new TimeLineTask().execute();
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);//To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.friendstimelinefragment_menu, menu);
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

    @Override
    public void refresh() {
        super.refresh();
    }

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            view.setSelected(true);
            MyAlertDialogFragment.newInstance().setView(view).show(getFragmentManager(), "");
            return false;
        }
    };

    static class MyAlertDialogFragment extends DialogFragment {
        View view;

        public static MyAlertDialogFragment newInstance() {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            frag.setRetainInstance(true);
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            view.setSelected(false);
        }

        public MyAlertDialogFragment setView(View view) {
            this.view = view;
            return this;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] items = {getString(R.string.take_camera), getString(R.string.select_pic)};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.select))
                    .setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case 0:

                                    break;
                                case 1:

                                    break;
                            }

                        }
                    });

            return builder.create();
        }
    }

    class TimeLineTask extends AsyncTask<Void, TimeLineMsgList, TimeLineMsgList> {
        DialogFragment dialogFragment = ProgressFragment.newInstance();

        @Override
        protected void onPreExecute() {
            dialogFragment.show(getFragmentManager(), "");
        }

        @Override
        protected TimeLineMsgList doInBackground(Void... params) {
            MainTimeLineActivity activity = (MainTimeLineActivity) getActivity();

            return new TimeLineFriendsMsg().getGSONMsgList(activity.getToken());
        }

        @Override
        protected void onPostExecute(TimeLineMsgList o) {
            if (o != null) {
                activity.setHomeList(o);
                Toast.makeText(getActivity(), "" + activity.getHomeList().getStatuses().size(), Toast.LENGTH_SHORT).show();

                timeLineAdapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(activity.getHomelist_position());
            }
            dialogFragment.dismissAllowingStateLoss();
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
