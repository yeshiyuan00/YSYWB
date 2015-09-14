package com.ysy.ysywb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 14:46
 */
public class CommentListBean implements Serializable {
    private List<CommentBean> comments;
    private String previous_cursor;
    private String next_cursor;
    private String total_number;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
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
