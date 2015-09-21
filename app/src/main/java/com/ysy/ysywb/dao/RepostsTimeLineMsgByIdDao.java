package com.ysy.ysywb.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.RepostListBean;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.utils.ActivityUtils;
import com.ysy.ysywb.support.utils.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * User: qii
 * Date: 12-8-13
 * Time: 下午9:59
 */
public class RepostsTimeLineMsgByIdDao {

    public RepostListBean getGSONMsgList() {

        String url = URLHelper.getRepostListById();

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("filter_by_author", filter_by_author);


        String jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);


        Gson gson = new Gson();

        RepostListBean value = null;
        try {
            value = gson.fromJson(jsonData, RepostListBean.class);
        } catch (JsonSyntaxException e) {
            ActivityUtils.showTips("发生错误，请重刷");
            AppLogger.e(e.getMessage().toString());
        }

        return value;
    }


    public RepostsTimeLineMsgByIdDao(String token, String id) {
        this.access_token = token;
        this.id = id;
    }

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setFilter_by_author(String filter_by_author) {
        this.filter_by_author = filter_by_author;
    }

    private String access_token;
    private String id;
    private String since_id;
    private String max_id;
    private String count;
    private String page;
    private String filter_by_author;
}
