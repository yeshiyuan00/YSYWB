package com.ysy.ysywb.ui.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.ysy.ysywb.support.imagetool.ImageTool;

import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/15
 * Time: 10:34
 */
public class PictureBitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private int position;
    private ListView listView;
    private LruCache<String, Bitmap> lruCache;
    private String data = "";
    private ImageView view;

    private Map<String, PictureBitmapWorkerTask> taskMap;


    public PictureBitmapWorkerTask(LruCache<String, Bitmap> lruCache,
                                   Map<String, PictureBitmapWorkerTask> taskMap,
                                   ImageView view,
                                   ListView listView,
                                   int position) {

        this.lruCache = lruCache;
        this.taskMap = taskMap;
        this.view = view;
        this.position = position;
        this.listView = listView;

    }

    @Override
    protected Bitmap doInBackground(String... url) {
        data = url[0];
        return ImageTool.getPictureThumbnailBitmap(data);
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        if (bitmap != null) {
            lruCache.put(data, bitmap);

        }
        if (taskMap.get(data) != null) {
            taskMap.remove(data);
        }
        super.onCancelled(bitmap);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (bitmap != null) {
            lruCache.put(data, bitmap);
            if (position >= listView.getFirstVisiblePosition() && position <= listView.getLastVisiblePosition()) {
                view.setImageBitmap(bitmap);
            }
        }
        if (taskMap.get(data) != null) {
            taskMap.remove(data);
        }
    }
}
