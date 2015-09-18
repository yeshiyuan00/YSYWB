package com.ysy.ysywb.support.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ysy.ysywb.bean.AccountBean;
import com.ysy.ysywb.bean.CommentBean;
import com.ysy.ysywb.bean.CommentListBean;
import com.ysy.ysywb.bean.MessageListBean;
import com.ysy.ysywb.bean.UserBean;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.support.database.table.AccountTable;
import com.ysy.ysywb.support.database.table.CommentsTable;
import com.ysy.ysywb.support.database.table.HomeTable;
import com.ysy.ysywb.support.database.table.RepostsTable;
import com.ysy.ysywb.support.utils.AppLogger;
import com.ysy.ysywb.ui.login.OAuthActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 17:22
 */
public class DatabaseManager {
    private static DatabaseManager singleton = null;

    private SQLiteDatabase wsd = null;

    private SQLiteDatabase rsd = null;

    private DatabaseManager() {

    }

    public synchronized static DatabaseManager getInstance() {

        if (singleton == null) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
            SQLiteDatabase wsd = databaseHelper.getWritableDatabase();
            SQLiteDatabase rsd = databaseHelper.getReadableDatabase();

            singleton = new DatabaseManager();
            singleton.wsd = wsd;
            singleton.rsd = rsd;
        }

        return singleton;
    }

    public OAuthActivity.DBResult addOrUpdateAccount(AccountBean account) {
        ContentValues cv = new ContentValues();
        cv.put(AccountTable.UID, account.getUid());
        cv.put(AccountTable.OAUTH_TOKEN, account.getAccess_token());
        cv.put(AccountTable.USERNAME, account.getUsername());
        cv.put(AccountTable.USERNICK, account.getUsernick());
        cv.put(AccountTable.AVATAR_URL, account.getAvatar_url());

        Cursor c = rsd.query(AccountTable.TABLE_NAME, null, AccountTable.UID + "=?",
                new String[]{account.getUid()}, null, null, null);
        if (c != null && c.getCount() > 0) {
            String[] args = {account.getUid()};
            wsd.update(AccountTable.TABLE_NAME, cv, AccountTable.UID + "=?", args);
            return OAuthActivity.DBResult.update_successfully;
        } else {
            wsd.insert(AccountTable.TABLE_NAME,
                    AccountTable.UID, cv);
            return OAuthActivity.DBResult.add_successfuly;
        }

    }


    public List<AccountBean> getAccountList() {
        List<AccountBean> weiboAccountList = new ArrayList<AccountBean>();
        String sql = "select * from " + AccountTable.TABLE_NAME;
        Cursor c = rsd.rawQuery(sql, null);

        while (c.moveToNext()) {
            AccountBean account = new AccountBean();
            int colid = c.getColumnIndex(AccountTable.OAUTH_TOKEN);
            account.setAccess_token(c.getString(colid));
            colid = c.getColumnIndex(AccountTable.USERNICK);
            account.setUsernick(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.UID);
            account.setUid(c.getString(colid));

            colid = c.getColumnIndex(AccountTable.AVATAR_URL);
            account.setAvatar_url(c.getString(colid));
            weiboAccountList.add(account);
        }
        return weiboAccountList;
    }

    public List<AccountBean> removeAndGetNewAccountList(Set<String> checkedItemPosition) {
        String[] args = checkedItemPosition.toArray(new String[0]);
        String column = AccountTable.UID;
        long result = wsd.delete(AccountTable.TABLE_NAME, column + "=?", args);
        return getAccountList();
    }

    public void addHomeLineMsg(MessageListBean list, String accountId) {
        Gson gson = new Gson();
        List<WeiboMsgBean> msgList = list.getStatuses();
        for (WeiboMsgBean msg : msgList) {
            ContentValues cv = new ContentValues();
            cv.put(HomeTable.MBLOGID, msg.getId());
            cv.put(HomeTable.ACCOUNTID, accountId);
            String json = gson.toJson(msg);
            cv.put(HomeTable.JSONDATA, json);
            wsd.insert(HomeTable.TABLE_NAME, HomeTable.ID, cv);
        }

    }

    public void replaceHomeLineMsg(MessageListBean list, String accountId) {


        wsd.execSQL("DROP TABLE IF EXISTS " + HomeTable.TABLE_NAME);
        wsd.execSQL(DatabaseHelper.CREATE_HOME_TABLE_SQL);

        addHomeLineMsg(list, accountId);
    }

    public MessageListBean getHomeLineMsgList(String accountId) {
        Gson gson = new Gson();
        MessageListBean result = new MessageListBean();
        List<WeiboMsgBean> msgList = new ArrayList<WeiboMsgBean>();
        String sql = "select * from " + HomeTable.TABLE_NAME
                + " where " + HomeTable.ACCOUNTID + " = " + accountId +
                " order by " + HomeTable.MBLOGID + " desc";
        Cursor c = rsd.rawQuery(sql, null);
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(HomeTable.JSONDATA));

            try {
                WeiboMsgBean value = gson.fromJson(json, WeiboMsgBean.class);
                msgList.add(value);
            } catch (JsonSyntaxException e) {
                AppLogger.e(e.getMessage());
            }

        }

        result.setStatuses(msgList);
        return result;
    }

    public MessageListBean getRepostLineMsgList(String accountId) {
        Gson gson = new Gson();
        MessageListBean result = new MessageListBean();
        List<WeiboMsgBean> msgList = new ArrayList<WeiboMsgBean>();
        String sql = "select * from " + RepostsTable.TABLE_NAME + " where "
                + RepostsTable.ACCOUNTID + " = " + accountId + " order by "
                + RepostsTable.MBLOGID + " desc";

        Cursor c = rsd.rawQuery(sql, null);
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(RepostsTable.JSONDATA));
            try {
                WeiboMsgBean value = gson.fromJson(json, WeiboMsgBean.class);
                msgList.add(value);
            } catch (JsonSyntaxException e) {
                AppLogger.e(e.getMessage());
            }
        }
        result.setStatuses(msgList);
        return result;
    }

    public void addRepostLineMsg(MessageListBean list, String accountId) {
        Gson gson = new Gson();
        List<WeiboMsgBean> msgList = list.getStatuses();
        int size = msgList.size();
        for (int i = 0; i < size; i++) {
            WeiboMsgBean msg = msgList.get(i);
            UserBean user = msg.getUser();
            ContentValues cv = new ContentValues();
            cv.put(RepostsTable.MBLOGID, msg.getId());
            cv.put(RepostsTable.ACCOUNTID, accountId);
            String json = gson.toJson(msg);
            cv.put(RepostsTable.JSONDATA, json);
            wsd.insert(RepostsTable.TABLE_NAME, RepostsTable.ID, cv);
        }
    }

    public void replaceRepostLineMsg(MessageListBean list, String accountId) {

        //need modification
        wsd.execSQL("DROP TABLE IF EXISTS " + RepostsTable.TABLE_NAME);
        wsd.execSQL(DatabaseHelper.CREATE_REPOSTS_TABLE_SQL);

        addRepostLineMsg(list, accountId);
    }

    public void addCommentLineMsg(CommentListBean list, String accountId) {
        Gson gson = new Gson();

        List<CommentBean> msgList = list.getComments();
        int size = msgList.size();
        for (CommentBean msg : msgList) {
            ContentValues cv = new ContentValues();
            cv.put(CommentsTable.MBLOGID, msg.getId());
            cv.put(CommentsTable.ACCOUNTID, accountId);
            String json = gson.toJson(msg);
            cv.put(CommentsTable.JSONDATA, json);

            wsd.insert(CommentsTable.TABLE_NAME,
                    CommentsTable.ID, cv);
        }
    }

    public CommentListBean getCommentLineMsgList(String accountId) {
        CommentListBean result = new CommentListBean();
        List<CommentBean> msgList = new ArrayList<CommentBean>();

        String sql = "select * from " + CommentsTable.TABLE_NAME + " where "
                + CommentsTable.ACCOUNTID + "  = "
                + accountId + " order by " + CommentsTable.MBLOGID + " desc";

        Cursor c = rsd.rawQuery(sql, null);
        Gson gson = new Gson();
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(CommentsTable.JSONDATA));
            try {
                CommentBean value = gson.fromJson(json, CommentBean.class);
                msgList.add(value);
            } catch (JsonSyntaxException e) {
                AppLogger.e(e.getMessage());
            }

        }
        result.setComments(msgList);
        return result;
    }

    public void replaceCommentLineMsg(CommentListBean list, String accountId) {

        //need modification
        wsd.execSQL("DROP TABLE IF EXISTS " + CommentsTable.TABLE_NAME);
        wsd.execSQL(DatabaseHelper.CREATE_COMMENTS_TABLE_SQL);

        addCommentLineMsg(list, accountId);
    }
}
