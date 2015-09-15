package com.ysy.ysywb.support.imagetool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ysy.ysywb.support.file.FileLocationMethod;
import com.ysy.ysywb.support.file.FileManager;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 17:11
 */
public class ImageTool {
    private static Bitmap decodeBitmapFromSDCard(String url,
                                                 int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture);

        absoluteFilePath = absoluteFilePath + ".jpg";


        Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);

        if (bitmap != null) {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(absoluteFilePath, options);
        } else {
            return null;
        }
    }

    private static Bitmap decodeBitmapFromSDCard(String url) {

        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.avatar);

        absoluteFilePath = absoluteFilePath + ".jpg";

        File file = new File(absoluteFilePath);

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bitmap != null) {

            return bitmap;
        } else {

            return null;
        }
    }

    public static Bitmap getPictureBitmapFromSDCardOrNetWork(String url,
                                                             int reqWidth, int reqHeight) {
        Bitmap bitmap = decodeBitmapFromSDCard(url, reqWidth, reqHeight);

        if (bitmap != null) {
            return bitmap;
        } else {
            return getBitmapFromNetWork(url, reqWidth, reqHeight);
        }
    }

    public static Bitmap getAvatarBitmapFromSDCardOrNetWork(String url) {

        Bitmap bitmap = decodeBitmapFromSDCard(url);

        if (bitmap != null) {
            return bitmap;
        } else {
            return getBitmapFromNetWork(url);
        }
    }

    private static Bitmap getBitmapFromNetWork(String url,
                                               int reqWidth, int reqHeight) {
        Map<String, String> parm = new HashMap<String, String>();
        HttpUtility.getInstance().execute(HttpMethod.Get_AVATAR_File, url, parm);
        return decodeBitmapFromSDCard(url, reqWidth, reqHeight);
    }

    private static Bitmap getBitmapFromNetWork(String url) {
        Map<String, String> parm = new HashMap<String, String>();
        HttpUtility.getInstance().execute(HttpMethod.Get_AVATAR_File, url, parm);
        return decodeBitmapFromSDCard(url);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

        }
        return inSampleSize;
    }
}
