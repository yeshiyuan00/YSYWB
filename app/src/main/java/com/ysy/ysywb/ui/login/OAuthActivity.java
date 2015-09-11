package com.ysy.ysywb.ui.login;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ysy.ysywb.R;
import com.ysy.ysywb.dao.OAuthDao;
import com.ysy.ysywb.bean.WeiboAccount;
import com.ysy.ysywb.bean.WeiboUser;
import com.ysy.ysywb.support.database.DatabaseManager;
import com.ysy.ysywb.weibo.Utility;
import com.ysy.ysywb.weibo.WeiboParameters;

/**
 * User: ysy
 * Date: 2015/9/8
 * Time: 9:34
 */
public class OAuthActivity extends Activity {

    public static String URL_OAUTH2_ACCESS_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";
    public static final String APP_KEY = "1065511513";// 替换为开发者的appkey，例如"1646212960";//468987987
    private static final String CONSUMER_SECRET = "df428e88aae8bd31f20481d149c856ed";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";
    //755e56b044e2a7517a748832ccb2709c
    private static final String DIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauthactivity_layout);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WeiboWebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(getWeiboOAuthUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.oauthactivity_menu, menu);
        return true;
    }

    public void refresh(MenuItem menu) {

        webView.loadUrl(getWeiboOAuthUrl());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private String getWeiboOAuthUrl() {
        WeiboParameters parameters = new WeiboParameters();
        parameters.add("client_id", APP_KEY);
        parameters.add("response_type", "token");
        parameters.add("redirect_uri", DIRECT_URL);
        parameters.add("display", "mobile");
        return URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters);

    }

    private class WeiboWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            if (url.startsWith(DIRECT_URL)) {
                handleRedirectUrl(view, url);
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }


    }

    private void handleRedirectUrl(WebView view, String url) {
        Bundle values = Utility.parseUrl(url);
        String error = values.getString("error");
        String error_code = values.getString("error_code");
        Intent intent = new Intent();
        intent.putExtras(values);
        if (error == null && error_code == null) {
            String access_token = values.getString("access_token");
            setResult(0, intent);
            new OAuthTask().execute(access_token);

        } else {
            Toast.makeText(OAuthActivity.this, getString(R.string.you_cancel_login), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            Toast.makeText(OAuthActivity.this, getString(R.string.you_cancel_login), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class OAuthTask extends AsyncTask<String, WeiboUser, Void> {
        ProgressFragment progressFragment = ProgressFragment.newInstance();

        @Override
        protected void onPreExecute() {
            progressFragment.setAsyncTask(this);
            progressFragment.show(getFragmentManager(),"");

        }

        @Override
        protected Void doInBackground(String... params) {
            String token = params[0];
            WeiboUser weiboUser = new OAuthDao(token).getOAuthUserInfo();
            WeiboAccount weiboAccount = new WeiboAccount();
            weiboAccount.setAccess_token(token);
            weiboAccount.setUsername(weiboUser.getName());
            weiboAccount.setUid(weiboUser.getId());
            weiboAccount.setUsernick(weiboUser.getScreen_name());

            long result = DatabaseManager.getInstance().addAccount(weiboAccount);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressFragment.dismissAllowingStateLoss();
            Toast.makeText(OAuthActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            finish();

        }
    }

    static class ProgressFragment extends DialogFragment {

        AsyncTask asyncTask = null;

        public static ProgressFragment newInstance() {
            ProgressFragment frag = new ProgressFragment();
            frag.setRetainInstance(true); //注意这句
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("授权中");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);


            return dialog;
        }

        @Override
        public void onCancel(DialogInterface dialog) {

            if (asyncTask != null) {
                asyncTask.cancel(true);
            }
            super.onCancel(dialog);
        }

        void setAsyncTask(AsyncTask task) {
            asyncTask = task;
        }
    }
}
