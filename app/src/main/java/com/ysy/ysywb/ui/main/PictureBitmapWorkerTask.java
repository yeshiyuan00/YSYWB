package com.ysy.ysywb.ui.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ysy.ysywb.support.imagetool.ImageTool;

/**
 * User: ysy
 * Date: 2015/9/15
 * Time: 10:34
 */
public class PictureBitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    String data = "";


    @Override
    protected Bitmap doInBackground(String... url) {
        data = url[0];
        return ImageTool.getAvatarBitmapFromSDCardOrNetWork(data);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}
