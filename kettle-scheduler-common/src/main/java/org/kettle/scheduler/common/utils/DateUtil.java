package org.kettle.scheduler.common.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 * <pre>
 *     继承apache.commons.lang3的DateUtils工具,并实现自己的一些逻辑处理
 * </pre>
 *
 * @author lyf
 */
public class DateUtil extends DateUtils {
    /**
     * 时区
     */
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+8");
    /**
     * 语言
     */
    public static final Locale LOCALE = Locale.SIMPLIFIED_CHINESE;
    /**
     * 日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式
     */
    public static final String TIME_PATTERN = "HH:mm:ss";
    /**
     * 日期时间格式
     */
    public static final String DATE_TIME = DATE_PATTERN + " " + TIME_PATTERN;
    /**
     * 一天的毫秒数
     */
    public static final long DAY_MILLISECOND = 24 * 60 * 60 * 1000;

    /**
     * 按照自定义格式把字符串转换成日期
     */
    public static Date getDate(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("日期转换异常");
        }
    }

    /**
     * 按照自定义格式把日期转换成字符串
     */
    public static String formatDateToStr(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 比较两个日期大小
     *
     * @param first  日期1
     * @param second 日期2
     * @return 等于=0, 大于=大于0的值 小于:小于0的值
     */
    public static int compareDate(Date first, Date second) {
        Calendar cal1 = Calendar.getInstance(TIME_ZONE, LOCALE);
        cal1.setTime(first);
        Calendar cal2 = Calendar.getInstance(TIME_ZONE, LOCALE);
        cal2.setTime(second);

        return cal1.compareTo(cal2);
    }

    /**
     * 取得两个日期之间的天数
     */
    public static long getDaysOfBetweenDate(Date first, Date second) {
        return Math.abs(first.getTime() - second.getTime()) / DAY_MILLISECOND;
    }

    /**
     * 获得当前时间毫秒数
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获得当前的时间戳
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(currentTimeMillis());
    }

    /**
     * 获得指定时间的时间戳
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 当前日期
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * 当前日期字符串 yyyy-MM-dd
     */
    public static String currentDateStr() {
        return formatDateToStr(currentDate(), DATE_PATTERN);
    }

    /**
     * 指定日期字符串 yyyy-MM-dd
     */
    public static String getDateStr(Date date) {
        return formatDateToStr(date, DATE_PATTERN);
    }

    /**
     * 指定日期字符串 yyyy-MM-dd
     */
    public static String getDateStr(Timestamp timestamp) {
        return formatDateToStr(timestamp, DATE_PATTERN);
    }

    /**
     * 当前时间字符串 HH:mm:ss
     */
    public static String currentTimeStr() {
        return formatDateToStr(currentDate(), TIME_PATTERN);
    }

    /**
     * 指定时间字符串 HH:mm:ss
     */
    public static String getTimeStr(Date date) {
        return formatDateToStr(date, TIME_PATTERN);
    }

    /**
     * 指定时间字符串 HH:mm:ss
     */
    public static String getTimeStr(Timestamp timestamp) {
        return formatDateToStr(timestamp, TIME_PATTERN);
    }

    /**
     * 当前日期时间字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String currentDateTimeStr() {
        return formatDateToStr(currentDate(), DATE_TIME);
    }

    /**
     * 指定日期时间字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeStr(Date date) {
        return formatDateToStr(date, DATE_TIME);
    }

    /**
     * 指定日期时间字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeStr(Timestamp timestamp) {
        return formatDateToStr(timestamp, DATE_TIME);
    }

    /**
     * 得到今天的起始时间
     */
    public static Date getTodayStartTime() {
        return getStartTimeOfDay(currentDate());
    }

    /**
     * 得到今天的结束时间
     */
    public static Date getTodayEndTime() {
        return getEndTimeOfDay(currentDate());
    }

    /**
     * 得到指定那天的起始时间点
     */
    public static Date getStartTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance(TIME_ZONE, LOCALE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 得到指定那天的终止时间点
     */
    public static Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance(TIME_ZONE, LOCALE);
        calendar.setTime(getStartTimeOfDay(date));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    /**
     * 指定日期所在周的开始日期
     */
    public static Date getStartDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.get(Calendar.WEEK_OF_YEAR);
        int firstDay = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDay);
        return getStartTimeOfDay(calendar.getTime());
    }

    /**
     * 指定日期所在周的结束日期
     */
    public static Date getEndDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.get(Calendar.WEEK_OF_YEAR);
        int firstDay = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, 8 - firstDay);
        return getEndTimeOfDay(calendar.getTime());
    }

    /**
     * 得到指定月的开始时间
     */
    public static Date getStartDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance(TIME_ZONE, LOCALE);
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        return getStartTimeOfDay(calendar.getTime());
    }

    /**
     * 得到指定月的终止时间
     */
    public static Date getEndDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance(TIME_ZONE, LOCALE);
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return getEndTimeOfDay(calendar.getTime());
    }

    /**
     * 指定日期所在年的开始日期
     */
    public static Date getStartDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        return getStartTimeOfDay(calendar.getTime());
    }

    /**
     * 指定日期所在年的结束日期
     */
    public static Date getEndDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 31);
        return getEndTimeOfDay(calendar.getTime());
    }
}
