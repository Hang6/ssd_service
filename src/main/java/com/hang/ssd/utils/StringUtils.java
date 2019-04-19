package com.hang.ssd.utils;

public class StringUtils {
    public static boolean isEmpty(String str){
        return  (null == str || "".equals(str) || str.length() <= 0);
    }

    public static boolean isNotEmpty(String message){
        return !isEmpty(message);
    }

    public static Integer toInteger(StringBuilder str){
        return Integer.parseInt(str.toString());
    }
}
