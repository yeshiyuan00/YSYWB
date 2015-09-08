package com.ysy.ysywb.dao;

import com.ysy.ysywb.support.utils.GlobalContext;
import com.ysy.ysywb.weibo.Token;
import com.ysy.ysywb.weibo.Utility;
import com.ysy.ysywb.weibo.WeiboException;
import com.ysy.ysywb.weibo.WeiboParameters;

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
 * Time: 14:47
 */
public class MentionsTimeLineMsg implements TimeLineMsg {
    @Override
    public String getMsgs() {
        String msg = "";
        Token token = new Token();
        token.setToken(GlobalContext.getInstance().getToken());
        token.setExpiresIn(GlobalContext.getInstance().getExpires());

        String url = URLHelper.getMentionsTimeLine();
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("access_token", GlobalContext.getInstance().getToken());
        try {
            msg = Utility.openUrl(GlobalContext.getInstance(), url, "GET", bundle, token);
        } catch (WeiboException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
}
