package com.qx.interactive.answer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 * Created by yangle on 2016/12/2.
 */
public class TimeUtils {

    public static String dateFormat_day = "HH:mm";
    public static String dateFormat_month = "MM-dd";

    /**
     * 时间转换成字符串,默认为"yyyy-MM-dd HH:mm:ss"
     *
     * @param time 时间
     */
    public static String dateToString(long time) {
        return dateToString(time, "yyyy.MM.dd HH:mm");
    }

    /**
     * 时间转换成字符串,指定格式
     *
     * @param time   时间
     * @param format 时间格式
     */
    public static String dateToString(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 把  “"2013/03/04"”  时间转为long时间戳
     * @param sData
     * @return
     */
    public static long dataToLong(String sData){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        long timestamp = 0;
        try {
            date = df.parse(sData);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            timestamp = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 获取从此刻开始  往前第几天的日期
     * @return
     */
    public static String getStatetime(int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, - day);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    public static String getStatetime2(int day){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, - day);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    public static int betweenDays(Calendar beginDate, Calendar endDate) {
        return (int) Math.abs((beginDate.getTimeInMillis() - endDate.getTimeInMillis()) / (1000 * 60 * 60 * 24));
    }

}
