package com.ysy.ysywb.support.database.table;

/**
 * User: ysy
 * Date: 2015/9/17
 * Time: 9:49
 */
public class RepostsTable {
    public static final String TABLE_NAME = "reposts_table";
    //support multi user,so primary key can't be message id
    public static final String ID = "_id";
    //support mulit user
    public static final String ACCOUNTID = "accountid";
    //message id
    public static final String MBLOGID = "mblogid";

    public static final String JSONDATA = "json";
}
