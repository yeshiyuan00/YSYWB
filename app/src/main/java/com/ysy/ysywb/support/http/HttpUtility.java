package com.ysy.ysywb.support.http;


import com.ysy.ysywb.support.utils.GlobalContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.StatusLine;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.client.protocol.ClientContext;
import ch.boye.httpclientandroidlib.client.utils.URIBuilder;
import ch.boye.httpclientandroidlib.impl.client.BasicCookieStore;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.params.BasicHttpParams;
import ch.boye.httpclientandroidlib.params.CoreProtocolPNames;
import ch.boye.httpclientandroidlib.params.HttpParams;
import ch.boye.httpclientandroidlib.protocol.BasicHttpContext;
import ch.boye.httpclientandroidlib.protocol.HttpContext;
import ch.boye.httpclientandroidlib.util.EntityUtils;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 8:40
 */
public class HttpUtility {
    private static HttpUtility httpUtility = new HttpUtility();
    private HttpClient httpClient = null;
    private HttpGet httpGet = new HttpGet();
    private HttpPost httpPost = new HttpPost();

    private HttpUtility() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpClient = new DefaultHttpClient(params);
    }

    public static HttpUtility getInstance() {
        return httpUtility;
    }

    public String execute(HttpMethod httpMethod, String url, Map<String, String> param) {
        switch (httpMethod) {
            case Post:
                return doPost(url, param);

            case Get:
                try {
                    return doGet(url, param);
                } catch (Exception e) {

                }
        }
        return "";
    }


    private String doPost(String url, Map<String, String> param) {
        return "";
    }

    private String doGet(String url, Map<String, String> param) throws URISyntaxException, IOException {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();

        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter("access_token", GlobalContext.getInstance().getToken());
        Set<String> keys = param.keySet();
        for (String key : keys) {
            uriBuilder.addParameter(key, param.get(key));
        }

        httpGet.setURI(uriBuilder.build());
        ch.boye.httpclientandroidlib.client.CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpResponse response = httpClient.execute(httpGet, localContext);

        return dealWithResponse(response);
    }

    private String dealWithResponse(HttpResponse httpResponse) {
        StatusLine status = httpResponse.getStatusLine();
        int statusCode = status.getStatusCode();
        String result = "";

        if (statusCode != 200) {
            return dealWithError(httpResponse);
        }

        return readResult(httpResponse);
    }

    private String readResult(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        String result = "";

        try {
            result = EntityUtils.toString(entity);
        } catch (IOException ignored) {

        }

        return result;
    }

    private String dealWithError(HttpResponse httpResponse) {

        StatusLine status = httpResponse.getStatusLine();
        int statusCode = status.getStatusCode();

        String result = "";

        if (statusCode != 200) {
            result = readResult(httpResponse);
            String err = null;
            int errCode = 0;

            try {
                JSONObject json = new JSONObject(result);
                err = json.getString("error");
                errCode = json.getInt("error_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void dealCookie() {
//        List<Cookie> cookies = cookieStore.getCookies();
//              for (int i = 0; i < cookies.size(); i++) {
//                  System.out.println("Local cookie: " + cookies.get(i));
//              }
    }
}
