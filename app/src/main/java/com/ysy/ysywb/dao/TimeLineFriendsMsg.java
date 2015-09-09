package com.ysy.ysywb.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.domain.TimeLineMsgList;
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
 * Date: 2015/9/8
 * Time: 14:14
 */
public class TimeLineFriendsMsg {

    private String getMsgs() {
        String msg = "";
        String url = URLHelper.getfriendTimeLine();
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
            e.printStackTrace();
        }
        return list;
    }

    public TimeLineMsgList getGSONMsgList() {
        Gson gson = new Gson();

        TimeLineMsgList value = null;
        try {
            value = gson.fromJson(getMsgs(), TimeLineMsgList.class);

        } catch (JsonSyntaxException e) {
            Log.e("gson", "------------------------------");
            Log.e("gson", getMsgs());
        }
        return value;
    }

}
