package com.ysy.ysywb.ui.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ListView;

import com.ysy.ysywb.support.imagetool.ImageTool;

/**
 * User: Jiang Qi
 * Date: 12-8-3
 * Time: 下午12:25
 */
public class AvatarBitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private String position;
    private ListView listView;
    private LruCache<String, Bitmap> lruCache;
    private String data = "";

    public AvatarBitmapWorkerTask(LruCache<String, Bitmap> cache) {
        lruCache = cache;
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
        return ImageTool.getAvatarBitmapFromSDCardOrNetWork(data);
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
        }
    }


}
