package com.ysy.ysywb.dao;

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
public class StatusNewMsg {

    public void sendNewMsg(String str) {
        String url = URLManager.getRealUrl("update");
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", str);
        HttpUtility.getInstance().execute(HttpMethod.Post, url, map);
    }
}
