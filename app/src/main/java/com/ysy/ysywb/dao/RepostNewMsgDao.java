package com.ysy.ysywb.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.utils.ActivityUtils;
import com.ysy.ysywb.support.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/22
 * Time: 8:46
 */
public class RepostNewMsgDao {
    public WeiboMsgBean sendNewMsg() {
        String url = URLHelper.new_Repost();
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("status", status);
        map.put("is_comment", is_comment);

        String jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, url, map);

        Gson gson = new Gson();

        WeiboMsgBean value = null;
        try {
            value = gson.fromJson(jsonData, WeiboMsgBean.class);
        } catch (JsonSyntaxException e) {
            ActivityUtils.showTips("发生错误，请重刷");
            AppLogger.e(e.getMessage().toString());
        }


        return value;

    }

    public RepostNewMsgDao(String token, String id) {
        this.access_token = token;
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    private String access_token;
    private String id;
    private String status;
    private String is_comment;
}
