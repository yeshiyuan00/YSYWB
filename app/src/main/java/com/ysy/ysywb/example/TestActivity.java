package com.ysy.ysywb.example;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ysy.ysywb.R;
import com.ysy.ysywb.weibo.Utility;
import com.ysy.ysywb.weibo.Weibo;
import com.ysy.ysywb.weibo.WeiboException;
import com.ysy.ysywb.weibo.WeiboParameters;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 15:13
 */
public class TestActivity extends Activity {
    TextView mResult;
    Weibo mWeibo = Weibo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.timeline);

        mResult = (TextView) this.findViewById(R.id.tvResult);
        Button getShare = (Button) this.findViewById(R.id.btnShare);
        getShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                File file = Environment.getExternalStorageDirectory();
//                String sdPath = file.getAbsolutePath();
//                // 请保证SD卡根目录下有这张图片文件
//                String picPath = sdPath + "/" + "abc.jpg";
//                File picFile = new File(picPath);
//                if (!picFile.exists()) {
//                    Toast.makeText(TestActivity.this, "图片" + picPath + "不存在！", Toast.LENGTH_SHORT)
//                            .show();
//                    picPath = null;
//                }
//                try {
//                    share2weibo("abc", picPath);
//                    Intent i = new Intent(TestActivity.this, ShareActivity.class);
//                    TestActivity.this.startActivity(i);
//
//                } catch (WeiboException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } finally {
//
//                }

                try {
                    update(mWeibo, "wo", null, "ni", null, null);
                } catch (WeiboException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }

        });

        new AsyncTask<Void, String, String>() {


            @Override
            protected String doInBackground(Void... params) {
                try {
                    return getPublicTimeline(mWeibo);
                } catch (IOException e) {


                } catch (WeiboException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            protected void onPostExecute(String o) {
                Log.e("dddd", "1" + o);
                mResult.setText(o);
                super.onPostExecute(o);
            }
        }.execute();
    }



    private String getPublicTimeline(Weibo weibo) throws MalformedURLException, IOException,
            WeiboException {
        String url = Weibo.SERVER + "statuses/public_timeline.json";
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("source", Weibo.getAppKey());
        String rlt = weibo.request(this, url, bundle, "GET", mWeibo.getAccessToken());
        return rlt;
    }

    private String upload(Weibo weibo, String source, String file, String status, String lon,
                          String lat) throws WeiboException {
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("source", source);
        bundle.add("pic", file);
        bundle.add("status", status);
        if (!TextUtils.isEmpty(lon)) {
            bundle.add("lon", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            bundle.add("lat", lat);
        }
        String rlt = "";
        String url = Weibo.SERVER + "statuses/upload.json";
        try {
            rlt = weibo
                    .request(this, url, bundle, Utility.HTTPMETHOD_POST, mWeibo.getAccessToken());
        } catch (WeiboException e) {
            throw new WeiboException(e);
        }
        return rlt;
    }

    private String update(Weibo weibo, String source, String status, String lon, String lat, Object o)
            throws WeiboException {
        WeiboParameters bundle = new WeiboParameters();
        bundle.add("source", source);
        bundle.add("status", status);
        if (!TextUtils.isEmpty(lon)) {
            bundle.add("lon", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            bundle.add("lat", lat);
        }
        String rlt = "";
        String url = Weibo.SERVER + "statuses/update.json";
        rlt = weibo.request(this, url, bundle, Utility.HTTPMETHOD_POST, mWeibo.getAccessToken());
        return rlt;
    }
}
