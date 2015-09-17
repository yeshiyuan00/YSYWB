package com.ysy.ysywb.ui.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.MessageListBean;
import com.ysy.ysywb.dao.FriendsTimeLineMsgDao;
import com.ysy.ysywb.support.database.DatabaseManager;
import com.ysy.ysywb.support.utils.AppConfig;
import com.ysy.ysywb.ui.Abstract.IAccountInfo;
import com.ysy.ysywb.ui.browser.BrowserWeiboMsgActivity;
import com.ysy.ysywb.ui.main.AvatarBitmapWorkerTask;
import com.ysy.ysywb.ui.main.MainTimeLineActivity;
import com.ysy.ysywb.ui.main.PictureBitmapWorkerTask;
import com.ysy.ysywb.ui.send.StatusNewActivity;

import java.util.Map;
import java.util.Set;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:03
 */
public class FriendsTimeLineFragment extends AbstractTimeLineFragment {

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("bean", bean);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainTimeLineActivity) getActivity()).setHomeListView(listView);

        if (savedInstanceState != null) {
            bean = (MessageListBean) savedInstanceState.getSerializable("bean");
        } else {
            bean = DatabaseManager.getInstance().getHomeLineMsgList(
                    ((IAccountInfo) getActivity()).getAccount().getUid());
        }

        timeLineAdapter.notifyDataSetChanged();
        if (bean.getStatuses().size() != 0) {
            footerView.findViewById(R.id.listview_footer).setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void listViewItemClick(AdapterView parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BrowserWeiboMsgActivity.class);
        intent.putExtra("msg", bean.getStatuses().get(position));
        intent.putExtra("token", ((MainTimeLineActivity) getActivity()).getToken());
        startActivity(intent);
    }


    @Override
    protected void listViewFooterViewClick(View view) {
        if (!isBusying) {

            new TimeLineGetOlderMsgListTask().execute();

        }
    }


    public void refresh() {
        Map<String, AvatarBitmapWorkerTask> avatarBitmapWorkerTaskHashMap = ((MainTimeLineActivity) getActivity()).getAvatarBitmapWorkerTaskHashMap();
        Map<String, PictureBitmapWorkerTask> pictureBitmapWorkerTaskMap = ((MainTimeLineActivity) getActivity()).getPictureBitmapWorkerTaskMap();
        new TimeLineGetNewMsgListTask().execute();

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
        inflater.inflate(R.menu.friendstimelinefragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friendstimelinefragment_new_weibo:
                Intent intent = new Intent(getActivity(), StatusNewActivity.class);
                intent.putExtra("token", ((MainTimeLineActivity) getActivity()).getToken());
                startActivity(intent);
                break;
            case R.id.friendstimelinefragment_refresh:
                if (!isBusying) {
                    refresh();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected MessageListBean getDoInBackgroundNewData() {
        FriendsTimeLineMsgDao dao = new FriendsTimeLineMsgDao(((MainTimeLineActivity) getActivity()).getToken());
        if (getList().getStatuses().size() > 0) {
            dao.setSince_id(getList().getStatuses().get(0).getId());
        }
        MessageListBean result = dao.getGSONMsgList();
        if (result != null) {
            if (result.getStatuses().size() < AppConfig.DEFAULT_MSG_NUMBERS) {
                DatabaseManager.getInstance().addHomeLineMsg(result, ((IAccountInfo) getActivity()).getAccount().getUid());
            } else {
                DatabaseManager.getInstance().replaceHomeLineMsg(result, ((IAccountInfo) getActivity()).getAccount().getUid());
            }
        }
        return result;
    }

    @Override
    protected MessageListBean getDoInBackgroundOldData() {
        FriendsTimeLineMsgDao dao = new FriendsTimeLineMsgDao(((MainTimeLineActivity) getActivity()).getToken());
        if (getList().getStatuses().size() > 0) {
            dao.setMax_id(getList().getStatuses().get(getList().getStatuses().size() - 1).getId());
        }
        MessageListBean result = dao.getGSONMsgList();

        return result;
    }
}
