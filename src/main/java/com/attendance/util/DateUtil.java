package com.attendance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author 梁宏博
 */
public class DateUtil {
    
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm:ss";
    
    /** 上班时间 09:00 */
    private static final int WORK_HOUR = 9;
    private static final int WORK_MINUTE = 0;
    
    /** 下班时间 18:00 */
    private static final int OFF_HOUR = 18;
    private static final int OFF_MINUTE = 0;
    
    /**
     * 格式化日期
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(FORMAT_DATE).format(date);
    }
    
    /**
     * 格式化日期时间
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(FORMAT_DATETIME).format(date);
    }
    
    /**
     * 解析日期字符串
     */
    public static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat(FORMAT_DATE).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 解析日期时间字符串
     */
    public static Date parseDateTime(String dateTimeStr) {
        try {
            return new SimpleDateFormat(FORMAT_DATETIME).parse(dateTimeStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 判断是否迟到（上班时间09:00之后）
     * @param clockInTime 打卡时间
     * @return 迟到分钟数，0表示未迟到
     */
    public static int getLateMinutes(Date clockInTime) {
        if (clockInTime == null) return 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(clockInTime);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        
        int clockMinutes = hour * 60 + minute;
        int workMinutes = WORK_HOUR * 60 + WORK_MINUTE;
        
        if (clockMinutes > workMinutes) {
            return clockMinutes - workMinutes;
        }
        return 0;
    }
    
    /**
     * 判断是否早退（下班时间18:00之前）
     * @param clockOutTime 下班打卡时间
     * @return 早退分钟数，0表示未早退
     */
    public static int getEarlyMinutes(Date clockOutTime) {
        if (clockOutTime == null) return 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(clockOutTime);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        
        int clockMinutes = hour * 60 + minute;
        int offMinutes = OFF_HOUR * 60 + OFF_MINUTE;
        
        if (clockMinutes < offMinutes) {
            return offMinutes - clockMinutes;
        }
        return 0;
    }
    
    /**
     * 计算两个日期之间的天数
     */
    public static int daysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
    
    /**
     * 判断是否为工作日（简化版：排除周六周日）
     */
    public static boolean isWorkday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }
}
