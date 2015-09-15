package com.ysy.ysywb.support.file;

import android.os.Environment;

import com.ysy.ysywb.support.utils.AppLogger;

import java.io.File;
import java.io.IOException;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 16:51
 */
public class FileManager {
    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String APP_NAME = "siyecao";
    private static final String AVATAR_CACHE = "avatar";
    private static final String PICTURE_CACHE = "picture";

    private static boolean isExternalStorageMounted() {
        boolean canRead = Environment.getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED);


        return !(!canRead || onlyRead || unMounted);

    }

    private static String getFileAbsolutePathFromRelativePath(String relativePath) {
        String result = SDCARD_PATH + File.separator + APP_NAME + relativePath;
        AppLogger.d(result);
        return result;
    }

    public static String getFileAbsolutePathFromUrl(String url, FileLocationMethod method) {
        String oldRelativePath = getFileRelativePathFromUrl(url);
        String newRelativePath = "";
        switch (method) {
            case avatar:
                newRelativePath = File.separator + AVATAR_CACHE + oldRelativePath;
                break;
            case picture:
                newRelativePath = File.separator + PICTURE_CACHE + oldRelativePath;
                break;
        }
        String absolutePath = getFileAbsolutePathFromRelativePath(newRelativePath);
        AppLogger.d(absolutePath);

        return absolutePath;
    }

    private static String getFileRelativePathFromUrl(String url) {
        //  String url = "http://tp2.sinaimg.cn/2500453793/50/5617547700/0";

        int index = url.indexOf("//");

        String s = url.substring(index + 2);

        String result = s.substring(s.indexOf("/"));

        AppLogger.d(result);

        return result;
    }

    public static File createNewFileInSDCard(String absolutePath) {
        if (!isExternalStorageMounted()) {
            AppLogger.e("sdcard unavailiable");
            return null;
        }

        String absoluteFilePath = getFileAbsolutePathFromRelativePath(absolutePath);
        String absoluteFileDirPath = absoluteFilePath.substring(0, absoluteFilePath.length() - 1);
        File file = new File(absoluteFilePath + ".jpg");
        if (file.exists()) {
            return file;
        } else {

            File dirFile = new File(absoluteFileDirPath);
            if (dirFile.mkdirs()) {

                try {
                    if (file.createNewFile()) {
                        return file;
                    }
                } catch (IOException e) {
                    AppLogger.d(e.getMessage());
                    return null;
                }
            }

        }
        return null;
    }
}
