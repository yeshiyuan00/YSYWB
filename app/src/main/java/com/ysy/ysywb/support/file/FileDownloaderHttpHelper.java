package com.ysy.ysywb.support.file;

import com.ysy.ysywb.support.utils.AppLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.StatusLine;
import ch.boye.httpclientandroidlib.util.EntityUtils;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 16:47
 */
public class FileDownloaderHttpHelper {

    public static String saveFile(String url, HttpResponse response,FileLocationMethod method) {
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();

        if (statusCode != HttpStatus.SC_OK) {
            return dealWithError(response);
        }

        return saveFileAndGetFileRelativePath(response, url,method);
    }

    private static String dealWithError(HttpResponse response) {

        return "";
    }

    private static String saveFileAndGetFileRelativePath(HttpResponse response, String url,FileLocationMethod method) {
        HttpEntity entity = response.getEntity();
        String imageAbsolutePath = FileManager.getFileAbsolutePathFromUrl(url, method);
        File file = FileManager.createNewFileInSDCard(imageAbsolutePath);
        FileOutputStream out = null;
        InputStream in = null;
        String result = "";
        if (file != null) try {
            int bytesum = 0;
            int byteread = 0;
            out = new FileOutputStream(file);
            in = entity.getContent();
            byte[] buffer = new byte[1444];
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);
            }
            result = imageAbsolutePath;

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }

            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        try {
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            AppLogger.e(e.getMessage());
        }

        return result;
    }
}
