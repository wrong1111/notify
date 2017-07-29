package com.game.utils.common.date;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期时间操作类
 */
public class UtilDateTime {

	/**
	 * 1分钟60
	 */
	static long MIN_SEC_NUMBER = 60L;
    /**
     * 1小时3600秒
     */
    static long HOUR_SEC_NUMBER = 3600L;

    /**
     * 1 天 24 * 3600 秒
     */
    static long DAY_SEC_NUMBER = HOUR_SEC_NUMBER * 24;

    /**
     * 1 天 30 * 24 * 3600 秒
     */
    static long MONTH_SEC_NUMBER = DAY_SEC_NUMBER * 30;

    /**
     * 1 年 365 * 24 * 3600 秒
     */
    static long YEAR_SEC_NUMBER = DAY_SEC_NUMBER * 365;

    public static final String LONGFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * <code>DEFAULTFORMAT</code>Date长日期格式
     */
    public static final String DEFAULTFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * <code>SIMPLEFORMATSTRING</code>Date长日期格式(无秒)
     */
    public static final String SIMPLEFORMATSTRING = "yyyy-MM-dd HH:mm";

    /**
     * <code>SHORTFORMAT</code>Date短日期格式
     */
    public static final String SHORTFORMAT = "yyyy-MM-dd";


    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYMMDD = "yyMMdd";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYMM = "yyMM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YY_MM_DD = "yy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YY_MM = "yy-MM";


    /**
     * 得到指定日期
     *
     * @param strDate 日期
     * @param pattern 日期格式
     * @return 日期
     */
    public static java.sql.Date toSqlDateByPattern(String strDate, String pattern) {
        try {
            //得到日期
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return new java.sql.Date((formatter.parse(strDate)).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new java.sql.Date(0);
    }

    public static String getCurrentDate(String pattern) {
        //得到日期
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(System.currentTimeMillis());
    }

    /**
     * 得到指定日期
     *
     * @param strDate 日期
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date toDateTimeByPattern(String strDate, String pattern) {
        try {
            //得到日期
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return new Date((formatter.parse(strDate)).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new java.sql.Date(0);
    }

    /**
     * 根据输入的日期长度自动转换成相应长度的Date型日期
     *
     * @param dDate String
     * @return Date
     */
    public static java.sql.Date toSqlDataTime(String dDate) {
        String dateFormat = SHORTFORMAT;

        if (dDate.length() > 10 && dDate.length() <= 16) {
            dateFormat = SIMPLEFORMATSTRING;
        } else if (dDate.length() > 16 && dDate.length() <= 19) {
            dateFormat = DEFAULTFORMAT;
        } else if (dDate.length() > 19) {
            dateFormat = LONGFORMAT;
        }
        java.sql.Date d1 = toSqlDateByPattern(dDate, dateFormat);

        return d1;
    }

    /**
     * 根据输入的日期长度自动转换成相应长度的Date型日期
     *
     * @param dDate String
     * @return Date
     */
    public static Date toDataTime(String dDate) {
        String dateFormat = SHORTFORMAT;
        if (StringUtils.isEmpty(dDate)) return null;

        if (dDate.length() > 10 && dDate.length() <= 16) {
            dateFormat = SIMPLEFORMATSTRING;
        } else if (dDate.length() > 16 && dDate.length() <= 19) {
            dateFormat = DEFAULTFORMAT;
        } else if (dDate.length() > 19) {
            dateFormat = LONGFORMAT;
        }
        Date d1 = toDateTimeByPattern(dDate, dateFormat);

        return d1;
    }

    /**
     * 得到格式化的日期字符串
     *
     * @param d
     * @return 日期
     */
    public static String toSqlDateString(java.sql.Date d, String pattern) {

        String rs = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            rs = formatter.format(d);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return isFirstStartDate(rs) ? "" : rs;

    }

    //检测日期是否是1970-01-01或1899-12-30

    private static boolean isFirstStartDate(String strDate) {
        if (strDate.compareTo("1970-01-01") == 0 ||
                strDate.compareTo("1899-12-30") == 0 || strDate.compareTo("1969-12-31") == 0) {
            return true;
        }
        return false;
    }

    /**
     * Return a Timestamp for right now
     *
     * @return Timestamp for right now
     */
    public static java.sql.Timestamp nowTimestamp() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    /**
     * Return a Date for right now
     *
     * @return Date for right now
     */
    public static Date nowDate() {
        return new Date();
    }

    public static java.sql.Date nowSqlDate() {
        return new java.sql.Date(nowDate().getTime());
    }

    public static java.sql.Timestamp getDayStart(java.sql.Timestamp stamp) {
        return getDayStart(stamp, 0);
    }

    public static java.sql.Timestamp getDayStart(java.sql.Timestamp stamp,
                                                 int daysLater) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH),
                tempCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return new java.sql.Timestamp(tempCal.getTime().getTime());
    }

    public static java.sql.Timestamp getNextDayStart(java.sql.Timestamp stamp) {
        return getDayStart(stamp, 1);
    }

    public static java.sql.Timestamp getDayEnd(java.sql.Timestamp stamp) {
        return getDayEnd(stamp, 0);
    }

    public static java.sql.Timestamp getDayEnd(java.sql.Timestamp stamp,
                                               int daysLater) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH),
                tempCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return new java.sql.Timestamp(tempCal.getTime().getTime());
    }

    /**
     * Converts a date String into a java.sql.Date
     *
     * @param date The date String: MM/DD/YYYY
     * @return A java.sql.Date made from the date String
     */
    public static java.sql.Date toSqlDate(String date) {
        Date newDate = toDate(date, "00:00:00");

        if (newDate != null)
            return new java.sql.Date(newDate.getTime());
        else
            return null;
    }

    /**
     * Makes a java.sql.Date from separate Strings for month, day, year
     *
     * @param monthStr The month String
     * @param dayStr   The day String
     * @param yearStr  The year String
     * @return A java.sql.Date made from separate Strings for month, day, year
     */
    public static java.sql.Date toSqlDate(String monthStr, String dayStr,
                                          String yearStr) {
        Date newDate = toDate(monthStr, dayStr, yearStr, "0", "0",
                "0");

        if (newDate != null)
            return new java.sql.Date(newDate.getTime());
        else
            return null;
    }

    /**
     * Makes a java.sql.Date from separate ints for month, day, year
     *
     * @param month The month int
     * @param day   The day int
     * @param year  The year int
     * @return A java.sql.Date made from separate ints for month, day, year
     */
    public static java.sql.Date toSqlDate(int month, int day, int year) {
        Date newDate = toDate(month, day, year, 0, 0, 0);

        if (newDate != null)
            return new java.sql.Date(newDate.getTime());
        else
            return null;
    }

    /**
     * Converts a time String into a java.sql.Time
     *
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A java.sql.Time made from the time String
     */
    public static java.sql.Time toSqlTime(String time) {
        Date newDate = toDate("1/1/1970", time);

        if (newDate != null)
            return new java.sql.Time(newDate.getTime());
        else
            return null;
    }

    /**
     * Makes a java.sql.Time from separate Strings for hour, minute, and second.
     *
     * @param hourStr   The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A java.sql.Time made from separate Strings for hour, minute, and second.
     */
    public static java.sql.Time toSqlTime(String hourStr, String minuteStr,
                                          String secondStr) {
        Date newDate = toDate("0", "0", "0", hourStr, minuteStr,
                secondStr);

        if (newDate != null)
            return new java.sql.Time(newDate.getTime());
        else
            return null;
    }

    /**
     * Makes a java.sql.Time from separate ints for hour, minute, and second.
     *
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A java.sql.Time made from separate ints for hour, minute, and second.
     */
    public static java.sql.Time toSqlTime(int hour, int minute, int second) {
        Date newDate = toDate(0, 0, 0, hour, minute, second);

        if (newDate != null)
            return new java.sql.Time(newDate.getTime());
        else
            return null;
    }

    /**
     * Converts a date and time String into a Timestamp
     *
     * @param dateTime A combined data and time string in the format "MM/DD/YYYY HH:MM:SS", the seconds are optional
     * @return The corresponding Timestamp
     */
    public static java.sql.Timestamp toTimestamp(String dateTime) {
        Date newDate = toDate(dateTime);

        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    /**
     * Converts a date String and a time String into a Timestamp
     *
     * @param date The date String: MM/DD/YYYY
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A Timestamp made from the date and time Strings
     */
    public static java.sql.Timestamp toTimestamp(String date, String time) {
        Date newDate = toDate(date, time);

        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    /**
     * Makes a Timestamp from separate Strings for month, day, year, hour, minute, and second.
     *
     * @param monthStr  The month String
     * @param dayStr    The day String
     * @param yearStr   The year String
     * @param hourStr   The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A Timestamp made from separate Strings for month, day, year, hour, minute, and second.
     */
    public static java.sql.Timestamp toTimestamp(String monthStr,
                                                 String dayStr, String yearStr, String hourStr, String minuteStr,
                                                 String secondStr) {
        Date newDate = toDate(monthStr, dayStr, yearStr, hourStr,
                minuteStr, secondStr);

        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    /**
     * Makes a Timestamp from separate ints for month, day, year, hour, minute, and second.
     *
     * @param month  The month int
     * @param day    The day int
     * @param year   The year int
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A Timestamp made from separate ints for month, day, year, hour, minute, and second.
     */
    public static java.sql.Timestamp toTimestamp(int month, int day, int year,
                                                 int hour, int minute, int second) {
        Date newDate = toDate(month, day, year, hour, minute, second);

        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    /**
     * Converts a date and time String into a Date
     *
     * @param dateTime A combined data and time string in the format "MM/DD/YYYY HH:MM:SS", the seconds are optional
     * @return The corresponding Date
     */
    public static Date toDate(String dateTime) {
        // dateTime must have one space between the date and time...
        String date = dateTime.substring(0, dateTime.indexOf(" "));
        String time = dateTime.substring(dateTime.indexOf(" ") + 1);

        return toDate(date, time);
    }

    /**
     * Converts a date String and a time String into a Date
     *
     * @param date The date String: MM/DD/YYYY
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A Date made from the date and time Strings
     */
    public static Date toDate(String date, String time) {
        if (date == null || time == null)
            return null;
        String month;
        String day;
        String year;
        String hour;
        String minute;
        String second;

        int dateSlash1 = date.indexOf("/");
        int dateSlash2 = date.lastIndexOf("/");

        if (dateSlash1 <= 0 || dateSlash1 == dateSlash2)
            return null;
        int timeColon1 = time.indexOf(":");
        int timeColon2 = time.lastIndexOf(":");

        if (timeColon1 <= 0)
            return null;
        month = date.substring(0, dateSlash1);
        day = date.substring(dateSlash1 + 1, dateSlash2);
        year = date.substring(dateSlash2 + 1);
        hour = time.substring(0, timeColon1);

        if (timeColon1 == timeColon2) {
            minute = time.substring(timeColon1 + 1);
            second = "0";
        } else {
            minute = time.substring(timeColon1 + 1, timeColon2);
            second = time.substring(timeColon2 + 1);
        }

        return toDate(month, day, year, hour, minute, second);
    }

    /**
     * Makes a Date from separate Strings for month, day, year, hour, minute, and second.
     *
     * @param monthStr  The month String
     * @param dayStr    The day String
     * @param yearStr   The year String
     * @param hourStr   The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A Date made from separate Strings for month, day, year, hour, minute, and second.
     */
    public static Date toDate(String monthStr, String dayStr,
                                        String yearStr, String hourStr, String minuteStr, String secondStr) {
        int month, day, year, hour, minute, second;

        try {
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            year = Integer.parseInt(yearStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        } catch (Exception e) {
            return null;
        }
        return toDate(month, day, year, hour, minute, second);
    }

    /**
     * Makes a Date from separate ints for month, day, year, hour, minute, and second.
     *
     * @param month  The month int
     * @param day    The day int
     * @param year   The year int
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A Date made from separate ints for month, day, year, hour, minute, and second.
     */
    public static Date toDate(int month, int day, int year, int hour,
                                        int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.set(year, month - 1, day, hour, minute, second);
        } catch (Exception e) {
            return null;
        }
        return new Date(calendar.getTime().getTime());
    }

    /**
     * Makes a date String in the format MM/DD/YYYY from a Date
     *
     * @param date The Date
     * @return A date String in the format MM/DD/YYYY
     */
    public static String toDateString(Date date) {
        if (date == null)
            return "";
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String monthStr;
        String dayStr;
        String yearStr;

        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        yearStr = "" + year;
        return monthStr + "/" + dayStr + "/" + yearStr;
    }

    /**
     * 得到格式化的日期字符串
     *
     * @param d
     * @return 日期
     */
    public static String toDateString(Date d, String pattern) {

        String rs = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            rs = formatter.format(d);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return isFirstStartDate(rs) ? "" : rs;

    }

    /**
     * Makes a time String in the format HH:MM:SS from a Date. If the seconds are 0, then the output is in HH:MM.
     *
     * @param date The Date
     * @return A time String in the format HH:MM:SS or HH:MM
     */
    public static String toTimeString(Date date) {
        if (date == null)
            return "";
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return (toTimeString(calendar.get(Calendar.HOUR_OF_DAY), calendar
                .get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
    }

    /**
     * Makes a time String in the format HH:MM:SS from a separate ints for hour, minute, and second. If the seconds are 0, then the output is in HH:MM.
     *
     * @param hour   The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A time String in the format HH:MM:SS or HH:MM
     */
    public static String toTimeString(int hour, int minute, int second) {
        String hourStr;
        String minuteStr;
        String secondStr;

        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }
        if (second == 0)
            return hourStr + ":" + minuteStr;
        else
            return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    /**
     * Makes a combined data and time string in the format "MM/DD/YYYY HH:MM:SS" from a Date. If the seconds are 0 they are left off.
     *
     * @param date The Date
     * @return A combined data and time string in the format "MM/DD/YYYY HH:MM:SS" where the seconds are left off if they are 0.
     */
    public static String toDateTimeString(Date date) {
        if (date == null)
            return "";
        String dateString = toDateString(date);
        String timeString = toTimeString(date);

        if (dateString != null && timeString != null)
            return dateString + " " + timeString;
        else
            return "";
    }

    /**
     * Makes a Timestamp for the beginning of the month
     *
     * @return A Timestamp of the beginning of the month
     */
    public static java.sql.Timestamp monthBegin() {
        Calendar mth = Calendar.getInstance();

        mth.set(Calendar.DAY_OF_MONTH, 1);
        mth.set(Calendar.HOUR_OF_DAY, 0);
        mth.set(Calendar.MINUTE, 0);
        mth.set(Calendar.SECOND, 0);
        mth.set(Calendar.AM_PM, Calendar.AM);
        return new java.sql.Timestamp(mth.getTime().getTime());
    }

    /**
     * datetime is the String of System.currentTimeMillis()?1?7
     */
    public static String getDateTimeDisp(String datetime) {
        if ((datetime == null) || (datetime.equals("")))
            return "";

        DateFormat formatter = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.MEDIUM);
        long datel = Long.parseLong(datetime);
        return formatter.format(new Date(datel));

    }


    /**
     * 1小时=3600*1000毫秒
     * <code>HOUR_MS_UNIT</code>
     */
    final static long HOUR_MS_UNIT = 3600 * 1000;

    /**
     * 计算时间差
     *
     * @param maxTime
     * @param minTime
     * @return 小时
     */
    public static float reckonDifference(Date maxTime, Date minTime) {
        float fatHour = 0;
        long lngMaxTime = (maxTime == null) ? 0 : maxTime.getTime();
        long lngMinTime = (minTime == null) ? 0 : minTime.getTime();

        float lngDifference = lngMaxTime - lngMinTime;
        lngDifference = Math.abs(lngDifference);

        fatHour = lngDifference / HOUR_MS_UNIT;

        return fatHour;
    }
    
    /**
     * 计算时间差
     * @param maxTime
     * @param minTime
     * @return 毫秒数
     */
    public static long reckonDifference2(Date maxTime, Date minTime) {
        long lngMaxTime = (maxTime == null) ? 0 : maxTime.getTime();
        long lngMinTime = (minTime == null) ? 0 : minTime.getTime();

        long lngDifference = lngMaxTime - lngMinTime;
        lngDifference = Math.abs(lngDifference);
        return lngDifference;
    }

    public static String getCurrDateStr(String format) {
        String result = "";

        if (StringUtils.isEmpty(format)) {
            return result;
        }

        Date d = new Date();
        DateFormat df = new SimpleDateFormat(format);
        result = df.format(d);
        return result;

    }

    /**
     * 指定一个日期加上n年后得到一个新日期
     *
     * @param sDate   指定日期
     * @param addYear 年数,负数为减去n年
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date getNextDateAddYear(Date sDate, int addYear) {
        int year = sDate.getYear();
        //Log.debug("year:"+year);
        int month = sDate.getMonth();
        //Log.debug("month:"+month);
        int day = sDate.getDate();
        //Log.debug("day:"+day);
        int hour = sDate.getHours();
        int minutes = sDate.getMinutes();
        int second = sDate.getSeconds();

        year = year + 1900 + addYear;
        month = month + 1;
        return toDate(month, day, year, hour, minutes, second);
    }

    /**
     * 指定一个日期加上n月后得到一个新日期
     *
     * @param sDate
     * @param addMonth 月数,负数为减去n月
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date getNextDateAddMonth(Date sDate, int addMonth) {
        int year = sDate.getYear();
        //Log.debug("year:"+year);
        int month = sDate.getMonth();
        //Log.debug("month:"+month);
        int day = sDate.getDate();
        //Log.debug("day:"+day);
        int hour = sDate.getHours();
        int minutes = sDate.getMinutes();
        int second = sDate.getSeconds();

        //增加或减少多少年
        int addYear = addMonth / 12;
        int addMonthByYear = addMonth % 12;

        year = year + 1900 + addYear;
        month = month + 1 + addMonthByYear;
        return toDate(month, day, year, hour, minutes, second);
    }

    /**
     * 指定一个日期加上n天后得到一个新日期
     *
     * @param sDate  天数,负数为减去n天
     * @param addDay
     * @return
     */
    public static Date getNextDateAddDay(Date sDate, int addDay) {
        Long lngTime = sDate.getTime();
        //间隔的毫秒数
        Long addDayMSec = addDay * DAY_SEC_NUMBER * 1000;
        return new Date(lngTime + addDayMSec);
    }


    /**
     * 指定一个日期加上n小时后得到一个新日期
     *
     * @param sDate   小时数,负数为减去n小时
     * @param addHour
     * @return
     */
    public static Date getNextDateAddHour(Date sDate, int addHour) {
        Long lngTime = sDate.getTime();
        //间隔的毫秒数
        Long addHourMSec = addHour * HOUR_SEC_NUMBER * 1000;
        return new Date(lngTime + addHourMSec);
    }
    
    /**
     * 指定一个日期加上n分钟后得到一个新日期
     *
     * @param sDate   小时数,负数为减去n分钟
     * @param addMin
     * @return
     */
    public static Date getNextDateAddMin(Date sDate, int addMin) {
        Long lngTime = sDate.getTime();
        //间隔的毫秒数
        Long addMinMSec = addMin * MIN_SEC_NUMBER * 1000;
        return new Date(lngTime + addMinMSec);
    }

    /**
     * @param date
     * @return 返回"yyyy-MM-dd HH:mm:ss"格式日期
     */
    public static String toDefaultDateTimeString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULTFORMAT);
        return format.format(date);
    }

    /**
     * @param 
     * @return 返回"yyyy-MM-dd HH:mm:ss"格式日期
     */
    public static String getCurrentTime() {
        return toDefaultDateTimeString(new Date());
    }

    public static String getMondayOfWeek() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd");
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return df.format(c.getTime());

        
    }


    public static String getSundayOfWeek() {
         Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd");
         c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, 1);
        return df.format(c.getTime());
    }

    // 获取当月第一天

    public static String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }
    // 计算当月最后一天,返回字符串

    public static String getLastDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }


    public static int getCurrDateInt(){
        Long date=new Date().getTime()/1000;
        return date.intValue();
    }
    public static void main(String str[]) {
//        System.out.println(UtilDateTime.getCurrentTime());
//        System.out.println(getFirstDayOfMonth());
//        System.out.println(getLastDayOfMonth());
//        System.out.println(getMondayOfWeek());
//        System.out.println(getSundayOfWeek());
        System.out.println(new Date().getTime());
        System.out.println(new Date(new Date().getTime()));
    }
}
