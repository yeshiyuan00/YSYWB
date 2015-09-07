package com.ysy.ysywb.weibo;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 15:24
 */
public class AccessToken extends Token {


    public AccessToken(String rlt){
        super(rlt);
    }

    public AccessToken(String token , String secret){
        super(token, secret);
    }
}
