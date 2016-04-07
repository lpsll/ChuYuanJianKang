package com.htlc.cyjk.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sks on 2016/2/1.
 */
public class DateFormat {
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
    public static String getTimeByDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    public static long getDays(String start,String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long total = 0;
        try {
            total = format.parse(end).getTime()-format.parse(start).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long days = total/(3600000*24);
        return days;
    }

}
