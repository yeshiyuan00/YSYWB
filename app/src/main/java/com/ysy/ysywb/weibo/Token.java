package com.ysy.ysywb.weibo;

import javax.crypto.spec.SecretKeySpec;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 15:24
 */
public class Token {
    // mToken 可能是access token， 可能是oauth_token
    private String mToken = "";
    private String mRefreshToken = "";
    private long mExpiresIn = 0;

    private String mOauth_verifier = "";
    protected String mOauth_Token_Secret = "";
    protected String[] responseStr = null;
    protected SecretKeySpec mSecretKeySpec;

    public Token() {

    }

    public String getToken() {
        return this.mToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    public long getExpiresIn() {
        return mExpiresIn;
    }

    public void setExpiresIn(long mExpiresIn) {
        this.mExpiresIn = mExpiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        if (expiresIn != null && !expiresIn.equals("0")) {
            setExpiresIn(System.currentTimeMillis() + Integer.parseInt(expiresIn) * 1000);
        }
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public void setVerifier(String verifier) {
        mOauth_verifier = verifier;
    }

    public String getVerifier() {
        return mOauth_verifier;
    }

    public String getSecret() {
        return mOauth_Token_Secret;
    }

    public Token(String rltString) {
        responseStr = rltString.split("&");
        mOauth_Token_Secret = getParameter("oauth_token_secret");
        mToken = getParameter("oauth_token");
    }

    public Token(String token, String secret) {
        mToken = token;
        mOauth_Token_Secret = secret;
    }

    public String getParameter(String parameter) {
        String value = null;
        for (String str : responseStr) {
            if (str.startsWith(parameter + '=')) {
                value = str.split("=")[1].trim();
                break;
            }
        }
        return value;
    }

    protected void setSecretKeySpec(SecretKeySpec secretKeySpec) {
        this.mSecretKeySpec = secretKeySpec;
    }

    protected SecretKeySpec getSecretKeySpec() {
        return mSecretKeySpec;
    }
}
