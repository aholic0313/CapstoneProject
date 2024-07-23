package com.codegeneration.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String _YYYYMMDD = "yyyy/MM/dd";
    public static final String YYYYMMDD = "yyyyMMdd";


    /**
     * 将时间转换成字符串
     *
     * @param date    时间
     * @param pattern 格式
     * @return 字符串
     */
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 将字符串转换成时间
     *
     * @param date    时间
     * @param pattern 格式
     * @return 时间
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
