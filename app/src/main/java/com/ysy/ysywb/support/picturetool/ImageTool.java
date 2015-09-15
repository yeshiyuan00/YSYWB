package com.ysy.ysywb.support.picturetool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
        String absoluteFilePath = FileManager.getFileAbsolutePathFromRelativePath(
                FileManager.getFileRelativePathFromUrl(url));

        absoluteFilePath = absoluteFilePath + ".jpg";

        File file = new File(absoluteFilePath);

        boolean is = file.exists();

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bitmap != null) {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(absoluteFilePath, options);
        } else {
            return null;
        }
    }

    public static Bitmap getBitmapFromSDCardOrNetWork(String url,
                                                      int reqWidth, int reqHeight) {
        Bitmap bitmap = decodeBitmapFromSDCard(url, reqWidth, reqHeight);

        if (bitmap != null) {
            return bitmap;
        } else {
            return getBitmapFromNetWork(url, reqWidth, reqHeight);
        }
    }

    private static Bitmap getBitmapFromNetWork(String url,
                                               int reqWidth, int reqHeight) {
        Map<String, String> parms = new HashMap<String, String>();
        String relativeFilePaht = HttpUtility.getInstance().execute(HttpMethod.Get_File, url, parms);
        return decodeBitmapFromSDCard(url, reqWidth, reqHeight);
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
