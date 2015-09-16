package com.ysy.ysywb.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.TimeLineMsgListBean;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.utils.ActivityUtils;
import com.ysy.ysywb.support.utils.AppLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/11
 * Time: 15:08
 */
public class MentionsTimeLineMsgDao {
    private String getMsgListJson() {

        String url = URLHelper.getMentionsTimeLine();

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("filter_by_author", filter_by_author);
        map.put("filter_by_source", filter_by_source);
        map.put("trim_user", trim_user);

        String jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);

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

        List<WeiboMsgBean> msgList = value.getStatuses();

        Iterator<WeiboMsgBean> iterator = msgList.iterator();

        while (iterator.hasNext()) {

            WeiboMsgBean msg = iterator.next();
            if (msg.getUser() == null) {
                iterator.remove();
            }
        }

        return value;

    }

    public String access_token;
    public String since_id;
    public String max_id;
    public String count;
    public String page;
    public String filter_by_author;
    public String filter_by_source;
    public String filter_by_type;
    public String trim_user;

    public MentionsTimeLineMsgDao(String access_token) {
        this.access_token = access_token;
    }

    public MentionsTimeLineMsgDao setSince_id(String since_id) {
        this.since_id = since_id;
        return this;
    }

    public MentionsTimeLineMsgDao setMax_id(String max_id) {
        this.max_id = max_id;
        return this;
    }

    public MentionsTimeLineMsgDao setCount(String count) {
        this.count = count;
        return this;
    }

    public MentionsTimeLineMsgDao setPage(String page) {
        this.page = page;
        return this;
    }

    public MentionsTimeLineMsgDao setFilter_by_author(String filter_by_author) {
        this.filter_by_author = filter_by_author;
        return this;
    }

    public MentionsTimeLineMsgDao setFilter_by_source(String filter_by_source) {
        this.filter_by_source = filter_by_source;
        return this;
    }

    public MentionsTimeLineMsgDao setFilter_by_type(String filter_by_type) {
        this.filter_by_type = filter_by_type;
        return this;
    }

    public MentionsTimeLineMsgDao setTrim_user(String trim_user) {
        this.trim_user = trim_user;
        return this;
    }

}
