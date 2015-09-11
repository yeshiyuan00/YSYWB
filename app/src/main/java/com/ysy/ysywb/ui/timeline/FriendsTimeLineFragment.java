package com.ysy.ysywb.ui.timeline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.TimeLineMsgList;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:03
 */
public class FriendsTimeLineFragment extends AbstractTimeLineFragment {
    private Commander commander;

    public static interface Commander {
        public void getNewFriendsTimeLineMsg();

        public void replayTo(int position);

        public void newWeibo();
    }

    public FriendsTimeLineFragment setCommander(Commander commander) {
        this.commander = commander;
        return this;
    }

    @Override
    protected TimeLineMsgList getList() {
        return activity.getHomeList();
    }

    @Override
    protected void scrollToBottom() {
        Toast.makeText(getActivity(), "底部", Toast.LENGTH_SHORT).show();
        commander.getNewFriendsTimeLineMsg();
    }

    @Override
    protected void listViewItemLongClick(AdapterView parent, View view, int position, long id) {
        view.setSelected(true);
        new MyAlertDialogFragment().setView(view).setPosition(position).show(getFragmentManager(), "");
    }


    public void refresh() {
        timeLineAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.friendstimelinefragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friendstimelinefragment_new_weibo:
                commander.newWeibo();
                break;
            case R.id.friendstimelinefragment_refresh:
                commander.getNewFriendsTimeLineMsg();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    class MyAlertDialogFragment extends DialogFragment {
        View view;

        int position;

        @Override
        public void onCancel(DialogInterface dialog) {
            view.setSelected(false);
        }

        public MyAlertDialogFragment setView(View view) {
            this.view = view;
            return this;
        }

        public MyAlertDialogFragment setPosition(int position) {
            this.position = position;
            return this;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String[] items = {"刷新", "回复"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.select))
                    .setItems(items, onClickListener);

            return builder.create();
        }

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        commander.getNewFriendsTimeLineMsg();
                        break;
                    case 1:
                        commander.replayTo(position);
                        break;
                }
            }
        };
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
