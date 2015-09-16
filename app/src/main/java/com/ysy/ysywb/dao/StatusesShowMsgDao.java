package com.ysy.ysywb.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/16
 * Time: 15:47
 */
public class StatusesShowMsgDao {

    private String access_token;
    private String id;

    public StatusesShowMsgDao(String access_token, String id) {
        this.access_token = access_token;
        this.id = id;
    }

    public WeiboMsgBean getMsg() {
        String url = URLHelper.getStatuses_Show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("id", id);
        String json = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);

        Gson gson = new Gson();

        WeiboMsgBean value = null;
        try {
            value = gson.fromJson(json, WeiboMsgBean.class);
            System.out.println("token="+access_token+"json="+json);
        } catch (JsonSyntaxException e) {

            AppLogger.e(e.getMessage().toString());
        }

        return value;
    }
}
