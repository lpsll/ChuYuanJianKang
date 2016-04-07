package com.htlc.cyjk.model;

public class DrugBean implements Comparable<DrugBean> {
    public String id;
    public String unit = "片";
    public String num = "1";
    public String medicine;
    public String group;
    public int position;

    @Override
    public int compareTo(DrugBean another) {
        if(this.group.compareTo(another.group)==0){
            return 1;
        }
        return this.group.compareTo(another.group);
    }

    @Override
    public String toString() {
        return "DrugBean{" +
                "id='" + id + '\'' +
                ", unit='" + unit + '\'' +
                ", num='" + num + '\'' +
                ", medicine='" + medicine + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}