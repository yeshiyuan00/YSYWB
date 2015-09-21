package com.ysy.ysywb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/16
 * Time: 16:33
 */
public class RepostListBean implements Serializable {
    private List<WeiboMsgBean> reposts = new ArrayList<WeiboMsgBean>();
    private String previous_cursor = "";
    private String next_cursor = "0";
    private String total_number = "";

    public List<WeiboMsgBean> getReposts() {
        return reposts;
    }

    public void setReposts(List<WeiboMsgBean> reposts) {
        this.reposts = reposts;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }
}
