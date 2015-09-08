package com.ysy.ysywb.dao;

import com.ysy.ysywb.support.utils.GlobalContext;
import com.ysy.ysywb.weibo.Token;
import com.ysy.ysywb.weibo.Utility;
import com.ysy.ysywb.weibo.WeiboException;
import com.ysy.ysywb.weibo.WeiboParameters;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 14:14
 */
public class FriendsTimeLineMsg implements TimeLineMsg {
    @Override
    public String getMsgs() {
        Token token = new Token();
        token.setToken(GlobalContext.getInstance().getToken());
        token.setExpiresIn(GlobalContext.getInstance().getExpires());

        String url = URLHelper.getHomeLine();

        WeiboParameters bundle = new WeiboParameters();
        bundle.add("access_token", GlobalContext.getInstance().getToken());

        try {
            String str = Utility.openUrl(GlobalContext.getInstance(), url, "GET", bundle, token);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
        return "";
    }
}
