package com.demo.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 时间工具类
 * 
 * @author zhanglei
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYYMM = "yyyyMM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public final static Integer dateNum = 20;

    public final static Integer dayNumOfWeek = 7;
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        if (date == null) {
            return null;
        }
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据出生日期计算年龄
     * @param birthday
     * @return
     */
    public static int getAgeByBirthday(LocalDate birthday){
        return (int)ChronoUnit.YEARS.between(birthday, LocalDate.now());
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDaysBetweenDate(Date before, Date after) {
        return getMillisecBetweenDate(before, after) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取两个日期之间的毫秒数
     *
     * @param before
     * @param after
     * @return
     */
    public static long getMillisecBetweenDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return afterTime - beforeTime;
    }

    /**
     * @author zhanglei
     * 根据传入时间，获取count年后的日期时间
     * @param localDateTime
     * @param count
     * @param days
     * @return 在count年后的时间加传入的天数
     */
    public static LocalDateTime getAfterYearByCount(LocalDateTime localDateTime, Long count, int days) {
        if(localDateTime == null){
            LocalDate dateTime = LocalDate.now();
            return dateTime.plusYears(count).plusDays(days).atTime(23,59,59);
        } else {
            return localDateTime.toLocalDate().plusYears(1L).plusDays(days).atTime(23,59,59);
        }
    }

    /**
     * @author zhanglei
     * @Description:    获取本周的开始日期/结束日期
     * @Param: [today, isFirst: true 表示开始日期，false表示结束日期]
     */
    public static LocalDate getStartOrEndDayOfWeek(LocalDate today, Boolean isFirst){
        if (today == null) {
            today = LocalDate.now();
        }
        DayOfWeek week = today.getDayOfWeek();
        int value = week.getValue();
        if (isFirst) {
            today = today.minusDays(value - 1);
        } else {
            today = today.plusDays(7 - value);
        }
        return LocalDate.parse(today.toString());
    }

    /**
     * @author zhanglei
     * @Description:    获取当前日期号数，形如：当前日期为：2020-01-16，则拿到16的值并返回
     * @return
     */
    public static int getCurrentDateNum(){
        return Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("dd")));
    }

    /**
     * @Description:获取本月的开始日期/结束日期
     * @Param: [today, isFirst: true 表示开始日期，false表示结束日期]
     */
    public static LocalDate getStartOrEndDayOfMonth(LocalDate today, Boolean isFirst) {
        //本月的第一天
        if(isFirst){
            return LocalDate.of(today.getYear(), today.getMonth(), 1);
        }
        // 本月最后一天
        return today.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * @Description: 获取本月的开始时间/结束时间
     * @Param: [today, isFirst: true 表示开始时间，false表示结束时间]
     */
    public static LocalDateTime getMonthStartOrEndDateTime(LocalDate dateTime, Boolean isFirst) {
        //本月的第一天
        if(isFirst){
            return dateTime.atTime(00, 00, 00).with(TemporalAdjusters.firstDayOfMonth());
        }
        // 本月最后一天
        return dateTime.atTime(23, 59, 59).with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * @author zhanglei
     * @Description: 获取传入日期所在月的所有日期集合
     * @return
     */
    public static List<LocalDate> getMonthDatesByLocalDate(LocalDate localDate){
        LocalDate start = getStartOrEndDayOfMonth(localDate, true);
        LocalDate end = getStartOrEndDayOfMonth(localDate, false);
        return Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());
    }


    // 获得某天最大时间 2017-10-15 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获得某天最小时间 2017-10-15 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取指定日期的最大时间
     * @param localDate
     * @return
     */
    public static LocalDateTime getEndTimeByLocalDate(LocalDate localDate){
        return localDate.atTime(23, 59, 59);
    }

    /**
     * 获取指定日期的最小时间
     * @param localDate
     * @return
     */
    public static LocalDateTime getStartTimeByLocalDate(LocalDate localDate){
        return localDate.atTime(00, 00, 00);
    }

    /**
     * 获取传入日期与传入日期所在周最后一天 时间差：分
     * @param localDateTime
     * @return
     */
    public static long getTimeDiff(LocalDateTime localDateTime){
        // 当前周最后一天时间
        LocalDateTime weekLastTime = getStartOrEndDayOfWeek(localDateTime.toLocalDate(), false).atTime(23, 59, 59);
        System.out.println("传入日期所在周最后一天时间：" + weekLastTime);
        // 时间差：分
        Duration duration = Duration.between(localDateTime, weekLastTime);
        long minutes = duration.toMinutes();
        return minutes;
    }

        /**
         * 测试方法
         * @param args
         */
    public static void main(String[] args) {
        LocalDate playerDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("1992-12-06"));
        //System.out.println(getAgeByBirthday(playerDate));

        //System.out.println(getStartOrEndDayOfMonth(LocalDate.now(), true));

        //System.out.println(getStartOrEndDayOfMonth(LocalDate.now(), false));

    }
}
