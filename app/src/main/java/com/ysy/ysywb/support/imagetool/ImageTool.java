package com.ysy.ysywb.support.imagetool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ysy.ysywb.support.file.FileLocationMethod;
import com.ysy.ysywb.support.file.FileManager;
import com.ysy.ysywb.support.http.HttpUtility;

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
        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture_thumbnail);

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

    public static Bitmap getPictureThumbnailBitmap(String url) {

        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture_thumbnail);

        Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);

        if (bitmap != null) {
            return bitmap;
        } else {
            return getBitmapFromNetWork(url, absoluteFilePath);
        }
    }

    public static Bitmap getAvatarBitmap(String url) {
        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.avatar);
        absoluteFilePath = absoluteFilePath + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);

        if (bitmap != null) {
            return bitmap;
        } else {
            return getBitmapFromNetWork(url, absoluteFilePath);
        }
    }

    private static Bitmap getBitmapFromNetWork(String url, String path) {
        HttpUtility.getInstance().executeDownloadTask(url, path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }
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
