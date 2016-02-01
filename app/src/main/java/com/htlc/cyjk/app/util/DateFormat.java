package com.htlc.cyjk.app.util;

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
}
