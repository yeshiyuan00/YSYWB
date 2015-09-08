package com.ysy.ysywb.support.http;

import android.content.Context;
import android.text.TextUtils;

import com.ysy.ysywb.R;
import com.ysy.ysywb.support.utils.GlobalContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 14:32
 */
public class URLManager {
    private static final String URL_SINA_WEIBO = "https://api.weibo.com/2";
    private static final String URL_FORMAT = "%s%s";
    private static Properties properties = new Properties();

    public final static String getUrl(String name) {

        try {
            if (properties.isEmpty()) {
                Context context = GlobalContext.getInstance();
                InputStream inputStream = context.getResources().openRawResource(R.raw.url);
                properties.load(inputStream);
            }
        } catch (IOException ignored) {

        }
        return properties.get(name).toString();
    }

    /**
     * 获得HTTP全路径地址
     *
     * @param urlContent
     * @return
     */
    public static String getRealUrl(String urlContent) {

        if (TextUtils.isEmpty(urlContent))
            return null;

        String url=getUrl(urlContent);
        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        return String.format(URL_FORMAT, URL_SINA_WEIBO, url);
    }
}
