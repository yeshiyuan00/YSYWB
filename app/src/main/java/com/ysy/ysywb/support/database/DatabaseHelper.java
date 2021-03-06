package com.ysy.ysywb.support.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ysy.ysywb.support.database.table.AccountTable;
import com.ysy.ysywb.support.database.table.CommentsTable;
import com.ysy.ysywb.support.database.table.GroupTable;
import com.ysy.ysywb.support.database.table.HomeTable;
import com.ysy.ysywb.support.database.table.RepostsTable;
import com.ysy.ysywb.support.utils.AppLogger;
import com.ysy.ysywb.support.utils.GlobalContext;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 17:20
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper singleton = null;
    private static final String DATABASE_NAME = "weibo.db";
    private static final int DATABASE_VERSION = 10;

    static final String CREATE_ACCOUNT_TABLE_SQL = "create table " + AccountTable.TABLE_NAME
            + "("
            + AccountTable.UID + " integer primary key autoincrement,"
            + AccountTable.OAUTH_TOKEN + " text,"
            + AccountTable.OAUTH_TOKEN_SECRET + " text,"
            + AccountTable.PORTRAIT + " text,"
            + AccountTable.USERNAME + " text,"
            + AccountTable.USERNICK + " text,"
            + AccountTable.AVATAR_URL + " text"
            + ");";

    static final String CREATE_GROUP_TABLE_SQL = "create table " + GroupTable.TABLE_NAME
            + "("
            + GroupTable.COUNT + " text,"
            + GroupTable.GID + " text,"
            + GroupTable.TITLE + " text,"
            + GroupTable.USER_ID + " text"
            + ");";

    static final String CREATE_HOME_TABLE_SQL = "create table " + HomeTable.TABLE_NAME
            + "("
            + HomeTable.ID + " integer primary key autoincrement,"
            + HomeTable.ACCOUNTID + " text,"
            + HomeTable.MBLOGID + " text,"
            + HomeTable.JSONDATA + " text"
            + ");";

    static final String CREATE_COMMENTS_TABLE_SQL = "create table " + CommentsTable.TABLE_NAME
            + "("
            + CommentsTable.ID + " integer primary key autoincrement,"
            + CommentsTable.ACCOUNTID + " text,"
            + CommentsTable.MBLOGID + " text,"
            + CommentsTable.JSONDATA + " text"
            + ");";


    static final String CREATE_REPOSTS_TABLE_SQL = "create table " + RepostsTable.TABLE_NAME
            + "("
            + RepostsTable.ID + " integer primary key autoincrement,"
            + RepostsTable.ACCOUNTID + " text,"
            + RepostsTable.MBLOGID + " text,"
            + RepostsTable.JSONDATA + " text"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //To change body of implemented methods use File | Settings | File Templates.
        db.execSQL(CREATE_ACCOUNT_TABLE_SQL);
        db.execSQL(CREATE_GROUP_TABLE_SQL);
        db.execSQL(CREATE_HOME_TABLE_SQL);
        db.execSQL(CREATE_COMMENTS_TABLE_SQL);
        db.execSQL(CREATE_REPOSTS_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
        AppLogger.d("Upgrading database from version "
                + oldVersion + " to " + newVersion + ",which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GroupTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HomeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommentsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RepostsTable.TABLE_NAME);
        onCreate(db);
    }

    public synchronized static DatabaseHelper getInstance() {
        if (singleton == null) {
            singleton = new DatabaseHelper(GlobalContext.getInstance());
        }
        return singleton;
    }
}
