package com.ysy.ysywb.support.database.table;

/**
 * User: ysy
 * Date: 2015/9/10
 * Time: 8:55
 */
public class HomeTable {
    public static final String TABLE_NAME = "home_table";
    //support multi user,so primary key can't be message id
    public static final String ID = "_id";
    //support mulit user
    public static final String ACCOUNTID="accountid";
    public static final String MBLOGID = "mblogid";
    //message author avatar url
    public static final String AVATAR="avatar";
    public static final String FEEDID = "feedid";
    public static final String MBLOGIDNUM = "mblogidnum";
    public static final String GID = "gid";
    public static final String GSID = "gsid";
    public static final String UID = "uid";
    public static final String NICK = "nick";
    public static final String PORTRAIT = "portrait";
    public static final String VIP = "vip";
    public static final String CONTENT = "content";
    public static final String RTROOTUID = "rtrootuid";
    public static final String RTAVATAR = "rtavatar";
    public static final String RTCONTENT = "rtcontent";
    public static final String RTPIC = "rtpic";
    public static final String RTID="rtid";
    public static final String RTROTNICK = "rtrootnick";
    public static final String RTROOTVIP = "rtrootvip";
    public static final String RTREASON = "rtreason";
    public static final String TIME = "time";
    public static final String PIC = "pic";
    public static final String SRC = "src";
}
