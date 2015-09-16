package com.ysy.ysywb.ui.timeline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.TimeLineMsgListBean;
import com.ysy.ysywb.dao.MentionsTimeLineMsgDao;
import com.ysy.ysywb.support.utils.AppConfig;
import com.ysy.ysywb.ui.browser.BrowserWeiboMsgActivity;
import com.ysy.ysywb.ui.main.AvatarBitmapWorkerTask;
import com.ysy.ysywb.ui.main.MainTimeLineActivity;
import com.ysy.ysywb.ui.main.PictureBitmapWorkerTask;
import com.ysy.ysywb.ui.main.ProgressFragment;

import java.util.Map;
import java.util.Set;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 15:19
 */
public class MentionsTimeLineFragment extends AbstractTimeLineFragment {


    public volatile boolean isBusying = false;

    private FriendsTimeLineFragment.Commander commander;

    public MentionsTimeLineFragment() {
//        bean = DatabaseManager.getInstance().getHomeLineMsgList();
    }


    public MentionsTimeLineFragment setCommander(FriendsTimeLineFragment.Commander commander) {
        this.commander = commander;
        return this;
    }

    @Override
    protected void scrollToBottom() {

    }

    @Override
    public void listViewItemLongClick(AdapterView parent, View view, int position, long id) {
        view.setSelected(true);
        new MyAlertDialogFragment().setView(view).setPosition(position).show(getFragmentManager(), "");
    }

    @Override
    protected void listViewItemClick(AdapterView parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BrowserWeiboMsgActivity.class);
        intent.putExtra("msg", bean.getStatuses().get(position));
        startActivity(intent);
    }

    @Override
    protected void listViewFooterViewClick(View view) {
        if (!isBusying) {

            new FriendsTimeLineGetOlderMsgListTask(view).execute();

        }
    }

    @Override
    protected void downloadAvatar(ImageView view, String url, int position, ListView listView) {
        commander.downloadAvatar(view, url, position, listView);
    }

    @Override
    protected void downContentPic(ImageView view, String url, int position, ListView listView) {
        commander.downContentPic(view, url, position, listView);
    }

    public void refresh() {
        Map<String, AvatarBitmapWorkerTask> avatarBitmapWorkerTaskHashMap = ((MainTimeLineActivity) getActivity()).getAvatarBitmapWorkerTaskHashMap();
        Map<String, PictureBitmapWorkerTask> pictureBitmapWorkerTaskMap = ((MainTimeLineActivity) getActivity()).getPictureBitmapWorkerTaskMap();


        new FriendsTimeLineGetNewMsgListTask().execute();
        Set<String> keys = avatarBitmapWorkerTaskHashMap.keySet();
        for (String key : keys) {
            avatarBitmapWorkerTaskHashMap.get(key).cancel(true);
            avatarBitmapWorkerTaskHashMap.remove(key);
        }

        Set<String> pKeys = pictureBitmapWorkerTaskMap.keySet();
        for (String pkey : pKeys) {
            pictureBitmapWorkerTaskMap.get(pkey).cancel(true);
            pictureBitmapWorkerTaskMap.remove(pkey);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mentionstimelinefragment_menu, menu);
        menu.add("weibo dont have messages group api");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mentionstimelinefragment_refresh:

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

                        break;
                    case 1:

                        break;
                }
            }
        };
    }

    class FriendsTimeLineGetNewMsgListTask extends AsyncTask<Void, TimeLineMsgListBean, TimeLineMsgListBean> {

        DialogFragment dialogFragment = new ProgressFragment();

        @Override
        protected void onPreExecute() {

            dialogFragment.show(getActivity().getSupportFragmentManager(), "");
        }

        @Override
        protected TimeLineMsgListBean doInBackground(Void... params) {
            MentionsTimeLineMsgDao dao = new MentionsTimeLineMsgDao(((MainTimeLineActivity) getActivity()).getToken());
            if (getList().getStatuses().size() > 0) {
                dao.setSince_id(getList().getStatuses().get(0).getId());
            }
            TimeLineMsgListBean result = dao.getGSONMsgList();
            if (result != null) {
                if (result.getStatuses().size() < AppConfig.DEFAULT_MSG_NUMBERS) {
//                    DatabaseManager.getInstance().addHomeLineMsg(result);
                } else {
//                    DatabaseManager.getInstance().replaceHomeLineMsg(result);
                }
            }
            return result;

        }

        @Override
        protected void onPostExecute(TimeLineMsgListBean newValue) {
            if (newValue != null) {
                if (newValue.getStatuses().size() == 0) {
                    Toast.makeText(getActivity(), "no new message", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "total " + newValue.getStatuses().size() + " new messages", Toast.LENGTH_SHORT).show();
                    if (newValue.getStatuses().size() < AppConfig.DEFAULT_MSG_NUMBERS) {
                        //if position equal 0,listview don't scroll because this is the first time to refresh
                        if (position > 0)
                            position += newValue.getStatuses().size();
                        newValue.getStatuses().addAll(getList().getStatuses());
                    } else {
                        position = 0;
                    }

                    bean = newValue;
                    timeLineAdapter.notifyDataSetChanged();
                    listView.setSelectionAfterHeaderView();

                }
            }
            dialogFragment.dismissAllowingStateLoss();
            super.onPostExecute(newValue);
        }
    }


    class FriendsTimeLineGetOlderMsgListTask extends AsyncTask<Void, TimeLineMsgListBean, TimeLineMsgListBean> {
        View footerView;

        public FriendsTimeLineGetOlderMsgListTask(View view) {
            footerView = view;
        }

        @Override
        protected void onPreExecute() {
            isBusying = true;

            ((TextView) footerView.findViewById(R.id.listview_footer)).setText("loading");

        }

        @Override
        protected TimeLineMsgListBean doInBackground(Void... params) {

            MentionsTimeLineMsgDao dao = new MentionsTimeLineMsgDao(((MainTimeLineActivity) getActivity()).getToken());
            if (getList().getStatuses().size() > 0) {
                dao.setMax_id(getList().getStatuses().get(getList().getStatuses().size() - 1).getId());
            }
            TimeLineMsgListBean result = dao.getGSONMsgList();

            return result;

        }

        @Override
        protected void onPostExecute(TimeLineMsgListBean newValue) {
            if (newValue != null) {
                Toast.makeText(getActivity(), "total " + newValue.getStatuses().size() + " old messages", Toast.LENGTH_SHORT).show();

                getList().getStatuses().addAll(newValue.getStatuses().subList(1, newValue.getStatuses().size() - 1));

            }

            isBusying = false;
            ((TextView) footerView.findViewById(R.id.listview_footer)).setText("click to load older message");
            timeLineAdapter.notifyDataSetChanged();
            super.onPostExecute(newValue);
        }
    }

}
