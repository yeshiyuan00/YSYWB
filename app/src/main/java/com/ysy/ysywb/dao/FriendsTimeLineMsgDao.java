package com.ysy.ysywb.dao;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.TimeLineMsgListBean;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.utils.ActivityUtils;
import com.ysy.ysywb.support.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 14:14
 */
public class FriendsTimeLineMsgDao {

    private String getMsgListJson() {
        String url = URLHelper.getFriendsTimeLine();

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("base_app", base_app);
        map.put("feature", feature);
        map.put("trim_user", trim_user);

        String jsonData = HttpUtility.getInstance().execute(HttpMethod.Get, url, map);

        return jsonData;
    }


    public TimeLineMsgListBean getGSONMsgList() {
        String json = getMsgListJson();
        Gson gson = new Gson();

        TimeLineMsgListBean value = null;
        try {
            value = gson.fromJson(json, TimeLineMsgListBean.class);

        } catch (JsonSyntaxException e) {
            ActivityUtils.showTips("发生错误，请重刷");
            AppLogger.e(e.getMessage().toString());
        }
        return value;
    }


    public String access_token;
    public String since_id;
    public String max_id;
    public String count;
    public String page;
    public String base_app;
    public String feature;
    public String trim_user;

    public FriendsTimeLineMsgDao(String access_token) {
        if (TextUtils.isEmpty(access_token))
            throw new IllegalArgumentException();
        this.access_token = access_token;
    }

    public FriendsTimeLineMsgDao setSince_id(String since_id) {
        this.since_id = since_id;
        return this;
    }

    public FriendsTimeLineMsgDao setMax_id(String max_id) {
        this.max_id = max_id;
        return this;
    }

    public FriendsTimeLineMsgDao setCount(String count) {
        this.count = count;
        return this;
    }

    public FriendsTimeLineMsgDao setPage(String page) {
        this.page = page;
        return this;
    }

    public FriendsTimeLineMsgDao setBase_app(String base_app) {
        this.base_app = base_app;
        return this;
    }

    public FriendsTimeLineMsgDao setFeature(String feature) {
        this.feature = feature;
        return this;
    }

    public FriendsTimeLineMsgDao setTrim_user(String trim_user) {
        this.trim_user = trim_user;
        return this;
    }
}
