package com.ysy.ysywb.ui.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.ysy.ysywb.support.imagetool.ImageTool;

import java.util.Map;

/**
 * User: Jiang Qi
 * Date: 12-8-3
 * Time: 下午12:25
 */
public class AvatarBitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private int position;
    private ListView listView;
    private LruCache<String, Bitmap> lruCache;
    private String data = "";

    private ImageView view;

    private Map<String, AvatarBitmapWorkerTask> taskMap;

    public AvatarBitmapWorkerTask(LruCache<String, Bitmap> lruCache,
                                  Map<String, AvatarBitmapWorkerTask> taskMap,
                                  ImageView view,
                                  ListView listView,
                                  int position) {
        this.lruCache = lruCache;
        this.taskMap = taskMap;
        this.view = view;
        this.position = position;
        this.listView = listView;
    }

//    @Override
//    protected void onPreExecute() {
//        if (imageViewReference != null  ) {
//            final ImageView imageView = imageViewReference.get();
//            if (imageView != null) {
//                imageView.setImageDrawable(GlobalContext.getInstance().getResources().getDrawable(R.drawable.app));
//            }
//        }
//        super.onPreExecute();
//    }

    @Override
    protected Bitmap doInBackground(String... url) {
        data = url[0];
        return ImageTool.getAvatarBitmap(data);
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        if (bitmap != null) {
            lruCache.put(data, bitmap);
        }
        if (taskMap!=null&&taskMap.get(data) != null) {
            taskMap.remove(data);
        }
        super.onCancelled(bitmap);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            lruCache.put(data, bitmap);
            if (position >= listView.getFirstVisiblePosition() &&
                    position <= listView.getLastVisiblePosition()) {
                view.setImageBitmap(bitmap);
            }
        }
        if (taskMap != null && taskMap.get(data) != null) {
            taskMap.remove(data);
        }
    }


}
