package com.ysy.ysywb.ui.browser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.CommentBean;
import com.ysy.ysywb.bean.CommentListBean;
import com.ysy.ysywb.dao.CommentsTimeLineMsgByIdDao;
import com.ysy.ysywb.support.utils.AppConfig;
import com.ysy.ysywb.ui.Abstract.AbstractAppActivity;
import com.ysy.ysywb.ui.main.AvatarBitmapWorkerTask;
import com.ysy.ysywb.ui.send.CommentNewActivity;
import com.ysy.ysywb.ui.timeline.Commander;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: ysy
 * Date: 2015/9/18
 * Time: 14:41
 */
public class CommentsByIdTimeLineFragment extends Fragment {
    protected View headerView;
    protected View footerView;
    public volatile boolean isBusying = false;
    protected Commander commander;

    protected ListView listView;
    protected TextView empty;
    protected ProgressBar progressBar;

    protected TimeLineAdapter timeLineAdapter;
    protected CommentListBean bean = new CommentListBean();

    public CommentListBean getList() {
        return bean;
    }

    private String token;
    private String id;

    public CommentsByIdTimeLineFragment(String token, String id) {
        this.token = token;
        this.id = id;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("bean", bean);
    }

    protected void refreshLayout(CommentListBean bean) {
        if (bean.getComments().size() > 0) {
            footerView.findViewById(R.id.listview_footer).setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        } else {
            footerView.findViewById(R.id.listview_footer).setVisibility(View.INVISIBLE);
            empty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commander = ((AbstractAppActivity) getActivity()).getCommander();
        if (savedInstanceState != null && bean.getComments().size() == 0) {
            bean = (CommentListBean) savedInstanceState.getSerializable("bean");
            timeLineAdapter.notifyDataSetChanged();
            refreshLayout(bean);
        } else {
            new SimpleTask().execute();
        }
    }

    private class SimpleTask extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            CommentListBean newValue = new CommentsTimeLineMsgByIdDao(token, id).getGSONMsgList();
            if (newValue != null) {
                bean = newValue;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            timeLineAdapter.notifyDataSetChanged();
            refreshLayout(bean);
            invlidateTabText();
            super.onPostExecute(o);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview_layout, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        empty = (TextView) view.findViewById(R.id.empty);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        listView.setScrollingCacheEnabled(false);
        headerView = inflater.inflate(R.layout.fragment_listview_header_layout, null);
        listView.addHeaderView(headerView);
        listView.setHeaderDividersEnabled(false);
        footerView = inflater.inflate(R.layout.fragment_listview_footer_layout, null);
        listView.addFooterView(footerView);

        if (bean.getComments().size() == 0) {
            footerView.findViewById(R.id.listview_footer).setVisibility(View.GONE);
        }

        timeLineAdapter = new TimeLineAdapter();
        listView.setAdapter(timeLineAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < getList().getComments().size()) {
                    listViewItemClick(parent, view, position - 1, id);
                } else {

                    listViewFooterViewClick(view);
                }
            }
        });


        return view;
    }

    protected class TimeLineAdapter extends BaseAdapter {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            if (getList() != null && getList().getComments() != null) {
                return getList().getComments().size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return getList().getComments().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.fragment_listview_item_comments_layout, parent, false);
                holder.username = (TextView) convertView.findViewById(R.id.username);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bindViewData(holder, position);

            return convertView;
        }

        private void bindViewData(ViewHolder holder, int position) {
            CommentBean msg = getList().getComments().get(position);


            holder.username.setText(msg.getUser().getScreen_name());
            String image_url = msg.getUser().getProfile_image_url();
            if (!TextUtils.isEmpty(image_url)) {
                downloadAvatar(holder.avatar, msg.getUser().getProfile_image_url(), position, listView);
            }
            holder.time.setText(msg.getCreated_at());
            holder.content.setText(msg.getText());

        }
    }

    static class ViewHolder {
        TextView username;
        TextView content;

        TextView time;
        ImageView avatar;

    }

    protected void listViewItemClick(AdapterView parent, View view, int position, long id) {

    }

    protected void listViewFooterViewClick(View view) {
        if (!isBusying) {
            new FriendsTimeLineGetOlderMsgListTask().execute();
        }
    }

    protected void downloadAvatar(ImageView view, String url, int position, ListView listView) {
        commander.downloadAvatar(view, url, position, listView);
    }


    public void refresh() {
        Map<String, AvatarBitmapWorkerTask> avatarBitmapWorkerTaskHashMap = ((AbstractAppActivity) getActivity()).getAvatarBitmapWorkerTaskHashMap();

        new FriendsTimeLineGetNewMsgListTask().execute();
        Set<String> keys = avatarBitmapWorkerTaskHashMap.keySet();
        for (String key : keys) {
            avatarBitmapWorkerTaskHashMap.get(key).cancel(true);
            avatarBitmapWorkerTaskHashMap.remove(key);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.commentsbyidtimelinefragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.commentsbyidtimelinefragment_comment:
                Intent intent = new Intent(getActivity(), CommentNewActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", id);
                startActivity(intent);

                break;
            case R.id.commentsbyidtimelinefragment_refresh:
                refresh();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class FriendsTimeLineGetNewMsgListTask extends AsyncTask<Void, CommentListBean, CommentListBean> {

        @Override
        protected void onPreExecute() {
            showListView();
            isBusying = true;
            footerView.findViewById(R.id.listview_footer).setVisibility(View.GONE);
            headerView.findViewById(R.id.header_progress).setVisibility(View.VISIBLE);
            headerView.findViewById(R.id.header_text).setVisibility(View.VISIBLE);
            Animation rotateAnimation = new RotateAnimation(0f, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setRepeatMode(Animation.RESTART);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            headerView.findViewById(R.id.header_progress).startAnimation(rotateAnimation);
            listView.setSelection(0);

        }

        @Override
        protected CommentListBean doInBackground(Void... params) {
            CommentsTimeLineMsgByIdDao dao = new CommentsTimeLineMsgByIdDao(token, id);
            if (getList().getComments().size() > 0) {
                dao.setSince_id(getList().getComments().get(0).getId());
            }
            CommentListBean result = dao.getGSONMsgList();
            return result;
        }

        @Override
        protected void onPostExecute(CommentListBean newValue) {
            if (newValue != null) {
                if (newValue.getComments().size() == 0) {
                    Toast.makeText(getActivity(), "no new message", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "total " + newValue.getComments().size() + " new messages", Toast.LENGTH_SHORT).show();
                    if (newValue.getComments().size() < AppConfig.DEFAULT_MSG_NUMBERS) {
                        newValue.getComments().addAll(getList().getComments());
                    }

                    bean = newValue;
                    timeLineAdapter.notifyDataSetChanged();
                    listView.setSelectionAfterHeaderView();
                    headerView.findViewById(R.id.header_progress).clearAnimation();


                }
            }
            headerView.findViewById(R.id.header_progress).setVisibility(View.GONE);
            headerView.findViewById(R.id.header_text).setVisibility(View.GONE);
            isBusying = false;
            if (bean.getComments().size() == 0) {
                footerView.findViewById(R.id.listview_footer).setVisibility(View.GONE);
            } else {
                footerView.findViewById(R.id.listview_footer).setVisibility(View.VISIBLE);
            }

            invlidateTabText();

            super.onPostExecute(newValue);

        }
    }

    class FriendsTimeLineGetOlderMsgListTask extends AsyncTask<Void, CommentListBean, CommentListBean> {
        @Override
        protected void onPreExecute() {
            showListView();
            isBusying = true;

            ((TextView) footerView.findViewById(R.id.listview_footer)).setText("loading");
            View view = footerView.findViewById(R.id.refresh);
            view.setVisibility(View.VISIBLE);

            Animation rotateAnimation = new RotateAnimation(0f, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setRepeatMode(Animation.RESTART);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            view.startAnimation(rotateAnimation);

        }

        @Override
        protected CommentListBean doInBackground(Void... params) {

            CommentsTimeLineMsgByIdDao dao = new CommentsTimeLineMsgByIdDao(token, id);
            if (getList().getComments().size() > 0) {
                dao.setMax_id(getList().getComments().get(getList().getComments().size() - 1).getId());
            }
            CommentListBean result = dao.getGSONMsgList();
            return result;
        }

        @Override
        protected void onPostExecute(CommentListBean newValue) {
            if (newValue != null && newValue.getComments().size() > 1) {
                Toast.makeText(getActivity(), "total " + newValue.getComments().size() + " old messages", Toast.LENGTH_SHORT).show();
                List<CommentBean> list = newValue.getComments();
                getList().getComments().addAll(list.subList(1, list.size() - 1));

            }

            isBusying = false;
            ((TextView) footerView.findViewById(R.id.listview_footer)).setText("click to load older message");
            footerView.findViewById(R.id.refresh).clearAnimation();
            footerView.findViewById(R.id.refresh).setVisibility(View.GONE);
            timeLineAdapter.notifyDataSetChanged();

            invlidateTabText();

            super.onPostExecute(newValue);
        }
    }

    private void showListView() {
        empty.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void invlidateTabText() {
        Activity activity = getActivity();
        if (activity != null) {
            ActionBar.Tab tab = activity.getActionBar().getTabAt(1);
            String name = tab.getText().toString();
            String num = "(" + bean.getComments().size() + ")";
            if (!name.endsWith(")")) {
                tab.setText(name + num);
            } else {
                int index = name.indexOf("(");
                String newName = name.substring(0, index);
                tab.setText(name + num);
            }
        }

    }
}
