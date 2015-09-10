package com.ysy.ysywb.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.WeiboUser;
import com.ysy.ysywb.support.http.HttpMethod;
import com.ysy.ysywb.support.http.HttpUtility;
import com.ysy.ysywb.support.http.URLManager;

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
    public static WeiboUser getOAuthUserInfo(String token) {

        String uidJson = getOAuthUserUID(token);
        String uid = "";

        try {
            JSONObject jsonObject = new JSONObject(uidJson);
            uid = jsonObject.optString("uid");
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", uid);
        map.put("access_token", token);

        String url = URLManager.getRealUrl("usershow");
        String result = HttpUtility.getInstance().execute(HttpMethod.Get, url, map);

        Gson gson = new Gson();


        WeiboUser user = new WeiboUser();
        try {
            user = gson.fromJson(result, WeiboUser.class);
        } catch (JsonSyntaxException e) {
            Log.e("gson", "------------------------------");
            Log.e("gson", result);
        }

        return user;


    }

    private static String getOAuthUserUID(String token) {

        String url = URLManager.getRealUrl("uid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", token);
        return HttpUtility.getInstance().execute(HttpMethod.Get, url, map);
    }
}
