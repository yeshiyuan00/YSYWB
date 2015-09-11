package com.ysy.ysywb.ui.timeline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.bean.TimeLineMsgList;
import com.ysy.ysywb.bean.WeiboMsg;
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


    public void refresh() {

    }

    protected abstract TimeLineMsgList getList();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainTimeLineActivity) getActivity();
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
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fragment_listview_item_layout, parent, false);
                holder.screenName = (TextView) convertView.findViewById(R.id.username);
                holder.pic = (ImageView) convertView.findViewById(R.id.pic);
                holder.txt = (TextView) convertView.findViewById(R.id.content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            WeiboMsg msg = getList().getStatuses().get(position);
            holder.screenName.setText(msg.getUser().getScreen_name());

            holder.txt.setText(msg.getText());

            holder.time.setText(msg.getCreated_at());


            holder.pic.setImageDrawable(getResources().getDrawable(R.drawable.app));

            return convertView;
        }
    }

    static class ViewHolder {
        TextView screenName;
        TextView txt;
        ImageView pic;
        TextView time;
    }
}
