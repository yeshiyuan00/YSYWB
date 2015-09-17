package com.ysy.ysywb.ui.timeline;

import android.widget.ImageView;
import android.widget.ListView;

/**
 * User: ysy
 * Date: 2015/9/17
 * Time: 8:26
 */
public interface Commander {
    public void downloadAvatar(ImageView view, String url, int position, ListView listView);

    public void downContentPic(ImageView view, String url, int position, ListView listView);
}
