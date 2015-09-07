package com.ysy.ysywb.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.weibo.AccessToken;
import com.ysy.ysywb.weibo.DialogError;
import com.ysy.ysywb.weibo.Weibo;
import com.ysy.ysywb.weibo.WeiboDialogListener;
import com.ysy.ysywb.weibo.WeiboException;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 14:53
 */
public class AuthorizeActivity extends Activity {

    private Button mLogin;
    private TextView mToken;

    private static final String CONSUMER_KEY = "1065511513";// 替换为开发者的appkey，例如"1646212960";
    private static final String CONSUMER_SECRET = "df428e88aae8bd31f20481d149c856ed";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mToken = (TextView) this.findViewById(R.id.tvToken);
        mLogin = (Button) this.findViewById(R.id.btnLogin);
        mLogin.setText("oauth!");
        mToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mLogin) {
                    Weibo weibo = Weibo.getInstance();
                    weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

                    // Oauth2.0
                    // 隐式授权认证方式
                    weibo.setRedirectUrl("https://api.weibo.com/oauth2/default.html");
                    // 此处回调页内容应该替换为与appkey对应的应用回调页
                    // 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
                    // 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
                    // 应用回调页不可为空

                    AuthDialogListener authDialogListener = new AuthDialogListener();

                    weibo.authorize(AuthorizeActivity.this, authDialogListener);
                }

                //                String token = "2.00vv3LGCZNmGKB73b944cfdeLB3rrD";
//                String expires_in ="85098";
//                mToken.setText("access_token : " + token + "  expires_in: "
//                        + expires_in);
//                AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
//                accessToken.setExpiresIn(expires_in);
//                Weibo.getInstance().setAccessToken(accessToken);
//                Intent intent = new Intent();
//                intent.setClass(AuthorizeActivity.this, TestActivity.class);
//                startActivity(intent);
            }
        });
    }

    class AuthDialogListener implements WeiboDialogListener {

        @Override
        public void onComplete(Bundle values) {
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            mToken.setText("access_token : " + token + "  expires_in: "
                    + expires_in);
            AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
            accessToken.setExpiresIn(expires_in);
            Weibo.getInstance().setAccessToken(accessToken);
            Intent intent = new Intent();
            intent.setClass(AuthorizeActivity.this, TestActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(DialogError e) {
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }
}
