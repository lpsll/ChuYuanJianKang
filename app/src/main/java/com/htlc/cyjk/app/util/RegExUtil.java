package com.htlc.cyjk.app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sks on 2016/4/19.
 */
public class RegExUtil {
    public static boolean matcherPhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile("1[3|4|5|7|8]\\d{9}");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
