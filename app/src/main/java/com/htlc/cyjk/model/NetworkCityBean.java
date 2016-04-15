package com.htlc.cyjk.model;

/**
 * Created by sks on 2016/4/15.
 */
public class NetworkCityBean {
    public String id;
    public String cityname;
    public String type;
    public String pid;
    public String flag;

    @Override
    public String toString() {
        return "NetworkCityBean{" +
                "id='" + id + '\'' +
                ", cityname='" + cityname + '\'' +
                ", type='" + type + '\'' +
                ", pid='" + pid + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
