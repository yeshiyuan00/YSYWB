package com.ysy.ysywb.dao;

import com.ysy.ysywb.support.http.URLManager;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 14:09
 */
public class URLHelper {
    public static String getHomeLine() {
        return URLManager.getRealUrl("hometimeline");
    }

    public static String getMentionsTimeLine() {

        return URLManager.getRealUrl("mentions");

    }

    public static String getuserTimeLine() {

        return URLManager.getRealUrl("user_timeline");

    }

    public static String getFriendsTimeLine() {

        return URLManager.getRealUrl("friendstimeline");

    }

    public static String getMyCommentsTimeLine() {

        return URLManager.getRealUrl("commentstimeline");

    }

    public static String getCommentListByMsgId(){
        return URLManager.getRealUrl("commentstimelinebymsgid");
    }

    public static String getTags(){
        return  URLManager.getRealUrl("tags");
    }
}
