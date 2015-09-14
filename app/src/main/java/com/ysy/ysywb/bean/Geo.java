package com.ysy.ysywb.bean;

import java.io.Serializable;

/**
 * User: ysy
 * Date: 2015/9/14
 * Time: 8:27
 */
public class Geo implements Serializable {
    private String type;
    private String[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String[] coordinates) {
        this.coordinates = coordinates;
    }
}
