package com.ysy.ysywb.dao;

import android.text.TextUtils;

import com.ysy.ysywb.support.error.WeiboException;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.http.URLManager;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 14:04
 */
public class StatusNewMsgDao {

    private String access_token;

    public StatusNewMsgDao(String access_token) {
        if (TextUtils.isEmpty(access_token))
            throw new IllegalArgumentException();
        this.access_token = access_token;
    }

    public void sendNewMsg(String str) {
        String url = URLManager.getRealUrl("update");
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("status", str);

        try {
            HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, url, map);
        } catch (WeiboException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
