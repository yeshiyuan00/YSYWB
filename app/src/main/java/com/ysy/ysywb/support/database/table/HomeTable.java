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
    public static final String ACCOUNTID = "accountid";
    public static final String MBLOGID = "mblogid";

    public static final String JSONDATA = "json";
}
