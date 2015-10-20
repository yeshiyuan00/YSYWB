package com.ysy.ysywb.dao;

import com.ysy.ysywb.bean.TagBean;
import com.ysy.ysywb.support.error.WeiboException;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.utils.AppLogger;

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
 * Date: 2015/9/16
 * Time: 8:46
 */
public class FriendsTimeLineTagDao {
    private String getMsgListJson() {
        String url = URLHelper.getTags();
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("uid", uid);
        map.put("count", count);
        map.put("page", page);

        String jsonData = null;
        try {
            jsonData = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);
        } catch (WeiboException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return jsonData;
    }

    public List<TagBean> getGSONMsgList() {
        String json = getMsgListJson();
        List<TagBean> tagBeanList = new ArrayList<TagBean>();

        try {
            JSONArray array = new JSONArray(json);
            int size = array.length();
            for (int i = 0; i < size; i++) {
                TagBean bean = new TagBean();
                JSONObject jsonObject = array.getJSONObject(i);
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equalsIgnoreCase("weight")) {
                        String value = jsonObject.optString(key);
                        bean.setWeight(value);
                    } else {
                        String value = jsonObject.optString(key);
                        bean.setId(Integer.valueOf(key));
                        bean.setName(value);
                    }
                }
                tagBeanList.add(bean);
            }
        } catch (JSONException e) {
            AppLogger.e(e.getMessage());
        }

        return tagBeanList;
    }

    private String access_token;
    private String uid;
    private String count;
    private String page;

    public FriendsTimeLineTagDao(String access_token, String uid) {
        this.access_token = access_token;
        this.uid = uid;
    }


    public void setCount(String count) {
        this.count = count;
    }


    public void setPage(String page) {
        this.page = page;
    }
}
