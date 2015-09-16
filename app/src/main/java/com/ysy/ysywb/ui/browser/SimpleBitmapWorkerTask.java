package com.ysy.ysywb.ui.browser;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.ysy.ysywb.support.imagetool.ImageTool;
import com.ysy.ysywb.support.utils.GlobalContext;

/**
 * User: ysy
 * Date: 2015/9/16
 * Time: 9:28
 */
public class SimpleBitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private LruCache<String, Bitmap> lruCache;
    private String data = "";
    private ImageView view;

    public SimpleBitmapWorkerTask(ImageView view) {

        this.lruCache = GlobalContext.getInstance().getAvatarCache();
        this.view = view;

    }

    @Override
    protected Bitmap doInBackground(String... url) {
        data = url[0];
        if (!isCancelled()) {
            return ImageTool.getAvatarBitmap(data);
        }
        return null;
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        if (bitmap != null) {
            lruCache.put(data, bitmap);
        }
        super.onCancelled(bitmap);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {

            lruCache.put(data, bitmap);

            view.setImageBitmap(bitmap);


        }
    }
}
