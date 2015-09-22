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

        return URLManager.getRealUrl("mentionstimeline");

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

    public static String getCommentList() {
        return URLManager.getRealUrl("commentstimeline");
    }

    public static String getCommentListById() {
        return URLManager.getRealUrl("commentstimelinebymsgid");
    }

    public static String getRepostListById() {
        return URLManager.getRealUrl("repoststimelinebymsgid");
    }

    public static String getTags() {
        return URLManager.getRealUrl("tags");
    }

    public static String getStatuses_Show() {
        return URLManager.getRealUrl("statuses_show");
    }

    public static String new_Repost() {
        return URLManager.getRealUrl("repost");
    }

    public static String new_Comment() {
        return URLManager.getRealUrl("comment");
    }
}
