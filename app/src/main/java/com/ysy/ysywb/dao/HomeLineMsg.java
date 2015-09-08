package com.ysy.ysywb.dao;

import com.ysy.ysywb.support.utils.GlobalContext;
import com.ysy.ysywb.weibo.Token;
import com.ysy.ysywb.weibo.Utility;
import com.ysy.ysywb.weibo.WeiboException;
import com.ysy.ysywb.weibo.WeiboParameters;

import java.util.Collections;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 14:07
 */
public class HomeLineMsg {

    public List getMsgs() {
        String token = GlobalContext.getInstance().getToken();

        String url = URLHelper.getHomeLine();

        return Collections.emptyList();
    }

    public static String getMsgstr() {

        Token token = new Token();
        token.setToken(GlobalContext.getInstance().getToken());
        token.setExpiresIn(GlobalContext.getInstance().getExpires());

        String url = URLHelper.getHomeLine();
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("access_token", GlobalContext.getInstance().getToken());
        try {
            String str = Utility.openUrl(GlobalContext.getInstance(), url, "GET", bundle, token);
            return str;
        } catch (WeiboException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }
}
