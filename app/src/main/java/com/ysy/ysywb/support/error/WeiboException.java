package com.ysy.ysywb.support.error;

import android.text.TextUtils;

import com.ysy.ysywb.support.utils.GlobalContext;

/**
 * User: ysy
 * Date: 2015/10/20
 * Time: 14:42
 */
public class WeiboException extends Exception {
    private String error;
    private int error_code;
    private String request;

    public String getError() {

        String name = "code" + error_code;
        int i = GlobalContext.getInstance().getResources()
                .getIdentifier(name, "string", GlobalContext.getInstance().getPackageName());
        String result = GlobalContext.getInstance().getString(i);
        if (!TextUtils.isEmpty(result)) {
            return result;
        }
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public void setError_code(int error_code) {
        this.error_code = error_code;
    }


}
