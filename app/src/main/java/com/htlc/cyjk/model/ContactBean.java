package com.htlc.cyjk.model;

import com.htlc.cyjk.app.util.LogUtil;

/**
 * Created by sks on 2016/1/29.
 */
public class ContactBean implements Comparable<ContactBean>{
    public String userid;
    public String name;
    public String head;
    public String photo;
    @Override
    public int compareTo(ContactBean another) {
//        LogUtil.e(this,);
        return this.head.compareTo(another.head);
    }

}
