package com.ysy.ysywb.weibo;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/7
 * Time: 15:28
 */
public class WeiboParameters {

    private Bundle mParameters = new Bundle();
    private List<String> mKeys = new ArrayList<String>();


    public WeiboParameters(){

    }


    public void add(String key, String value){
        if(this.mKeys.contains(key)){
            this.mParameters.putString(key, value);
        }else{
            this.mKeys.add(key);
            this.mParameters.putString(key, value);
        }
    }


    public void remove(String key){
        mKeys.remove(key);
        this.mParameters.remove(key);
    }

    public void remove(int i){
        String key = this.mKeys.get(i);
        this.mParameters.remove(key);
        mKeys.remove(key);
    }


    public int getLocation(String key){
        if(this.mKeys.contains(key)){
            return this.mKeys.indexOf(key);
        }
        return -1;
    }

    public String getKey(int location){
        if(location >= 0 && location < this.mKeys.size()){
            return this.mKeys.get(location);
        }
        return "";
    }


    public String getValue(String key){
        String rlt = this.mParameters.getString(key);
        return rlt;
    }

    public String getValue(int location){
        String key = this.mKeys.get(location);
        String rlt = this.mParameters.getString(key);
        return rlt;
    }


    public int size(){
        return mKeys.size();
    }

    public void addAll(WeiboParameters parameters){
        for(int i = 0; i < parameters.size(); i++){
            this.add(parameters.getKey(i), parameters.getValue(i));
        }

    }

    public void clear(){
        this.mKeys.clear();
        this.mParameters.clear();
    }
}
