package com.ysy.ysywb.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ysy.ysywb.R;
import com.ysy.ysywb.dao.TimeLineMentionsMsg;
import com.ysy.ysywb.support.utils.GlobalContext;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 14:39
 */
public class HomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timeline);

        Intent intent = getIntent();

        String token = intent.getStringExtra("token");
        String expires = intent.getStringExtra("expires");

        String username = intent.getStringExtra("username");

        if (TextUtils.isEmpty(username))
            setTitle(username);

        GlobalContext.getInstance().setToken(token);
        GlobalContext.getInstance().setExpires(expires);

        //((TextView) findViewById(R.id.tvResult)).setText(token);

        new AsyncTask<Void,String,String>(){
            @Override
            protected String doInBackground(Void... params) {
                return new TimeLineMentionsMsg().getMsgs();
            }

            @Override
            protected void onPostExecute(String o) {
                Log.e("dddd", "1" + o);
                //((TextView) findViewById(R.id.tvResult)).setText(o);
                super.onPostExecute(o);
            }
        }.execute();
    }
}
