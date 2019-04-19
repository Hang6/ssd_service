package com.hang.ssd.utils;

import java.text.SimpleDateFormat;

/**
 * 时间转换工具
 * @author yinhang
 */
public class TimeUtils {
    private static final String DATE = "yyyy-MM-dd HH:mm:ss";

    public static String timeStamp2Date(String timeStamp){
        long time = Long.parseLong(timeStamp) * 1000;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
        String date = dateFormat.format(time);
        return date;
    }
}
