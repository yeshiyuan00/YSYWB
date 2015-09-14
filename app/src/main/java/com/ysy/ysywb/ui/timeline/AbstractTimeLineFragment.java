package com.ysy.ysywb.ui.timeline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.TimeLineMsgListBean;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.ui.MainTimeLineActivity;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 10:17
 */
public abstract class AbstractTimeLineFragment extends Fragment {
    protected ListView listView;

    protected TimeLineAdapter timeLineAdapter;
    protected MainTimeLineActivity activity;

    public abstract void refreshAndScrollTo(int positon);

    public abstract void refresh();

    protected abstract TimeLineMsgListBean getList();

    protected abstract void scrollToBottom();

    protected abstract void listViewItemLongClick(AdapterView parent, View view, int position, long id);

    protected abstract void listViewItemClick(AdapterView parent, View view, int position, long id);

    protected abstract void rememberListViewPosition(int position);

    protected abstract void listViewFooterViewClick(View view);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainTimeLineActivity) getActivity();
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    public ListView getListView() {
        return listView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview_layout, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        listView.setScrollingCacheEnabled(false);

        View footerView = inflater.inflate(R.layout.fragment_listview_footer_layout, null);
        listView.addFooterView(footerView);

        timeLineAdapter = new TimeLineAdapter();
        listView.setAdapter(timeLineAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch ((scrollState)) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            scrollToBottom();
                        }
                        rememberListViewPosition(view.getFirstVisiblePosition());

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listViewItemLongClick(parent, view, position, id);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < getList().getStatuses().size()) {
                    listViewItemClick(parent, view, position, id);
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
            if (getList() != null && getList().getStatuses() != null) {
                return getList().getStatuses().size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return getList().getStatuses().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.fragment_listview_item_layout, parent, false);
                holder.screenName = (TextView) convertView.findViewById(R.id.username);
                holder.pic = (ImageView) convertView.findViewById(R.id.pic);
                holder.txt = (TextView) convertView.findViewById(R.id.content);
                holder.recontent = (TextView) convertView.findViewById(R.id.recontent);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bindViewData(holder, position);

            return convertView;
        }

        private void bindViewData(ViewHolder holder, int position) {
            WeiboMsgBean msg = getList().getStatuses().get(position);
            holder.screenName.setText(msg.getUser().getScreen_name());

            holder.txt.setText(msg.getText());


            if (!TextUtils.isEmpty(msg.getListviewItemShowTime())) {
                holder.time.setText(msg.getListviewItemShowTime());
            } else {
                holder.time.setText(msg.getCreated_at());
            }

            holder.pic.setImageDrawable(getResources().getDrawable(R.drawable.app));
            WeiboMsgBean recontent = msg.getRetweeted_status();
            if (recontent != null) {
                holder.recontent.setVisibility(View.VISIBLE);
                holder.recontent.setText(recontent.getUser().getScreen_name() + "：" + recontent.getText());
            } else {
                holder.recontent.setVisibility(View.GONE);
            }
        }
    }

    static class ViewHolder {
        TextView screenName;
        TextView txt;
        TextView recontent;
        ImageView pic;
        TextView time;
    }
}
