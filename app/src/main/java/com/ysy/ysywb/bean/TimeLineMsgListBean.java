package com.ysy.ysywb.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/9
 * Time: 16:13
 */
public class TimeLineMsgListBean {
    public List<WeiboMsgBean> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<WeiboMsgBean> statuses) {
        this.statuses = statuses;
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

    private List<WeiboMsgBean> statuses=new ArrayList<WeiboMsgBean>();
    private String previous_cursor="";
    private String next_cursor="0";
    private String total_number="";
}
