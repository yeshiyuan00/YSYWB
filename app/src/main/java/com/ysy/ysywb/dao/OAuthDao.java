package com.ysy.ysywb.dao;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.WeiboUserBean;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.http.URLManager;
import com.ysy.ysywb.support.utils.AppLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ysy
 * Date: 2015/9/10
 * Time: 10:01
 */
public class OAuthDao {

    private String access_token;

    public OAuthDao(String access_token) {
        if (TextUtils.isEmpty(access_token))
            throw new IllegalArgumentException();
        this.access_token = access_token;
    }

    public WeiboUserBean getOAuthUserInfo() {

        String uidJson = getOAuthUserUIDJsonData();
        ;
        String uid = "";

        try {
            JSONObject jsonObject = new JSONObject(uidJson);
            uid = jsonObject.optString("uid");
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", uid);
        map.put("access_token", access_token);

        String url = URLManager.getRealUrl("usershow");
        String result = HttpUtility.getInstance().execute(HttpMethod.Get, url, map);

        Gson gson = new Gson();

        WeiboUserBean user = new WeiboUserBean();
        try {
            user = gson.fromJson(result, WeiboUserBean.class);
        } catch (JsonSyntaxException e) {
            AppLogger.e(result);
        }

        return user;

    }

    private String getOAuthUserUIDJsonData() {
        String url = URLManager.getRealUrl("uid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        return HttpUtility.getInstance().execute(HttpMethod.Get, url, map);
    }
}
