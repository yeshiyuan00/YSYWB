package com.ysy.ysywb.dao;

import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private String getMsgs() {
        String msg = "";

        String url = URLHelper.getMentionsTimeLine();

        Map<String, String> map = new HashMap<String, String>();

        msg = HttpUtility.getInstance().execute(HttpMethod.Get, url, map);

        return msg;
    }

    public List<Map<String, String>> getMsgList() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String msg = getMsgs();

        try {
            JSONObject jsonObject = new JSONObject(msg);
            JSONArray statuses = jsonObject.getJSONArray("statuses");
            int length = statuses.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = statuses.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", object.optString("id"));
                map.put("text", object.optString("text"));
                Iterator iterator = object.keys();
                String key;
                while (iterator.hasNext()) {
                    key = (String) iterator.next();
                    Object value = object.opt(key);
                    if (value instanceof String) {
                        map.put(key, value.toString());
                    } else if (value instanceof JSONObject) {
                        map.put("screen_name", ((JSONObject) value).optString("screen_name"));
                    }
                }
                list.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return list;
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
