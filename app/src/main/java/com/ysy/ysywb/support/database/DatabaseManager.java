package com.ysy.ysywb.support.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ysy.ysywb.bean.MessageListBean;
import com.ysy.ysywb.bean.AccountBean;
import com.ysy.ysywb.bean.WeiboMsgBean;
import com.ysy.ysywb.bean.UserBean;
import com.ysy.ysywb.support.database.table.AccountTable;
import com.ysy.ysywb.support.database.table.HomeTable;
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
        List<WeiboMsgBean> msgList = list.getStatuses();
        int size = msgList.size();
        for (int i = 0; i < size; i++) {
            WeiboMsgBean msg = msgList.get(i);
            UserBean user = msg.getUser();
            ContentValues cv = new ContentValues();
            cv.put(HomeTable.MBLOGID, msg.getId());
            cv.put(HomeTable.ACCOUNTID, accountId);
            cv.put(HomeTable.NICK, user.getScreen_name());
            cv.put(HomeTable.UID, user.getId());
            cv.put(HomeTable.CONTENT, msg.getText());
            cv.put(HomeTable.TIME, msg.getCreated_at());
            cv.put(HomeTable.PIC, msg.getThumbnail_pic());
            cv.put(HomeTable.AVATAR, msg.getUser().getProfile_image_url());

            WeiboMsgBean rt = msg.getRetweeted_status();
            if (rt != null) {
                UserBean rtUser = rt.getUser();
                cv.put(HomeTable.RTAVATAR, rtUser.getProfile_image_url());
                cv.put(HomeTable.RTCONTENT, rt.getText());
                cv.put(HomeTable.RTID, rt.getId());
                cv.put(HomeTable.RTROTNICK, rtUser.getScreen_name());
                cv.put(HomeTable.RTROOTUID, rtUser.getId());
                if (!TextUtils.isEmpty(rt.getThumbnail_pic())) {
                    cv.put(HomeTable.RTPIC, rt.getThumbnail_pic());
                }
            }

            long result = wsd.insert(HomeTable.TABLE_NAME, HomeTable.MBLOGID, cv);
        }
    }

    public void replaceHomeLineMsg(MessageListBean list, String accountId) {


        wsd.execSQL("DROP TABLE IF EXISTS " + HomeTable.TABLE_NAME);
        wsd.execSQL(DatabaseHelper.CREATE_HOME_TABLE_SQL);

        addHomeLineMsg(list, accountId);
    }

    public MessageListBean getHomeLineMsgList(String accountId) {
        MessageListBean result = new MessageListBean();
        List<WeiboMsgBean> msgList = new ArrayList<WeiboMsgBean>();
        String sql = "select * from " + HomeTable.TABLE_NAME
                + " where" + HomeTable.ACCOUNTID + " = " + accountId +
                " order by " + HomeTable.MBLOGID + " desc";
        Cursor c = rsd.rawQuery(sql, null);
        while (c.moveToNext()) {
            WeiboMsgBean msg = new WeiboMsgBean();
            int colid = c.getColumnIndex(HomeTable.MBLOGID);
            msg.setId(c.getString(colid));

            colid = c.getColumnIndex(HomeTable.CONTENT);
            msg.setText(c.getString(colid));

            msg.setListviewItemShowTime(c.getString(c.getColumnIndex(HomeTable.TIME)));

            msg.setThumbnail_pic(c.getString(c.getColumnIndex(HomeTable.PIC)));

            UserBean user = new UserBean();

            user.setScreen_name(c.getString(c.getColumnIndex(HomeTable.NICK)));
            user.setProfile_image_url(c.getString(c.getColumnIndex(HomeTable.AVATAR)));

            msg.setUser(user);

            colid = c.getColumnIndex(HomeTable.RTCONTENT);
            String rtContent = c.getString(colid);
            if (!TextUtils.isEmpty((rtContent))) {
                WeiboMsgBean bean = new WeiboMsgBean();
                UserBean userBean = new UserBean();
                bean.setId(c.getString(c.getColumnIndex(HomeTable.RTID)));
                bean.setText(c.getString(c.getColumnIndex(HomeTable.RTCONTENT)));
                bean.setThumbnail_pic(c.getString(c.getColumnIndex(HomeTable.RTPIC)));
                userBean.setScreen_name(c.getString(c.getColumnIndex(HomeTable.RTROTNICK)));
                userBean.setId(c.getString(c.getColumnIndex(HomeTable.RTROOTUID)));

                bean.setUser(userBean);
                msg.setRetweeted_status(bean);
            }

            msgList.add(msg);

        }

        result.setStatuses(msgList);
        return result;
    }
}
