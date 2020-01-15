package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateFormatTool {

    /**
     * 静态常量
     */
    public static final String DATE_PATTON_1 = "yyyy-MM-dd";
    public static final String DATE_PATTON_2 = "yyyy/MM/dd";
    public static final String DATE_PATTON_3 = "yyyyMMdd";
    public static final String TIME_PATTON_1 = "HH:MM:SS";
    public static final String DATE_TIME_PATTON_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_PATTON_2 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_TIME_PATTON_3 = "yyyyMMddHHmmss";
    public static final String DATE_TIME_PATTON_4 = "yyyy-MM-ddHH:mm:ss";

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;


    //锁对象
    private static final Object lockObj = new Object();
    //存放不同的日期模板格式的sdf的Map
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    /**
     * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 如果新的线程中没有SimpleDateFormat，才会new一个
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

}
