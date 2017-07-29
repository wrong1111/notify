package com.game.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日期工具类，提供常用的静态方法
 * 
 * @author Administrator
 * 
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	protected final static Log log = LogFactory.getLog(DateUtils.class);
	public final static String[] ChineseWeek = { "日", "一", "二", "三", "四", "五",
			"六" };

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * new SimpleDateFormat("yyyy-MM-dd");
	 * */
	public final static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(
			"yyyy-MM-dd");;
	/**
	 * new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 * */
	public final static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");;
	/**
	 * new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * */
	public final static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");;
	/**
	 * new SimpleDateFormat("yyyyMMdd");
	 * */
	public final static SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat(
			"yyyyMMdd");;
	/**
	 * new SimpleDateFormat("yyyyMMddHHmmss")
	 * */
	public final static SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat(
			"yyyyMMddHHmmss");;

	/**
	 * new SimpleDateFormat("yyyyMMddHHmm")
	 * */
	public final static SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat(
			"yyyyMMddHHmm");

	/**
	 * 将日期转换为字符串时间
	 * 
	 * @param date
	 *            //日期
	 * @param dateFormat
	 *            //格式
	 * @return
	 */
	public static String date2String(Date date, SimpleDateFormat dateFormat) {
		return dateFormat.format(date);
	}

	public static String date2String(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static Date string2Date(String source, SimpleDateFormat dateFormat) {
		try {
			return dateFormat.parse(source);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public static Date string2Date(String source, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(source);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public static Date format(Date date, SimpleDateFormat dateFormat) {
		try {
			return dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return date;
	}

	public static Date format(Date date, String f) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(f);
			return dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return date;
	}

	public static boolean isSameMinute(Date d1, Date d2) {
		return simpleDateFormat2.format(d1)
				.equals(simpleDateFormat2.format(d2));
	}

	/**
	 * 英文操作系统下 强制得到中文yyyy-MM-dd 星期 以上午10点为分界
	 * */
	public static String getChineseYMDWeek(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour < 10)
			c.add(Calendar.DAY_OF_MONTH, -1);
		return simpleDateFormat1.format(c.getTime()) + " 星期"
				+ ChineseWeek[c.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 英文操作系统下 强制得到中文yyyy-MM-dd 星期 标准
	 * */
	public static String getChineseYMDWeek_(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return simpleDateFormat1.format(c.getTime()) + " 星期"
				+ ChineseWeek[c.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public static String dateadd(Date d, int field, int num, String format) {
		SimpleDateFormat dateFormat = null;
		if (format == null)
			dateFormat = simpleDateFormat1;
		else
			dateFormat = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(field, num);
		return dateFormat.format(c.getTime());
	}

	/**
	 * @author wyong
	 * @version 1.0
	 * @2011-5-17
	 * @description 计算二个时间相差天数 同一天算做 0 .
	 * @path ecp888_46-->com.ecp888.utils.impl-->DateUtils.java
	 * @param othertime
	 * @return int
	 */
	public static int deduction(long othertime) {
		long nowtime = System.currentTimeMillis();
		long d = Math.abs(nowtime - othertime);
		int day = (int) (d / 1000 / 60 / 60 / 24);
		return day;
	}

	/**
	 * 得到最近一个月的日期时间数组串,前面小时间,后面大时间
	 * 
	 * @return ['2011-06-06 12:10:10','2011-07-06 12:10:10']
	 */
	public static String[] getNearMonthDateTimeStr() {
		Calendar cal = Calendar.getInstance();
		String begin = simpleDateFormat3.format(cal.getTime());
		cal.add(Calendar.MONTH, -1);
		String end = simpleDateFormat3.format(cal.getTime());
		return new String[] { end, begin };
	}

	/**
	 * 得到最近一个月的日期数组串,前面小时间,后面大时间
	 * 
	 * @return ['2011-06-06','2011-07-06']
	 */
	public static String[] getNearMonthDateStr() {
		Calendar cal = Calendar.getInstance();
		String begin = simpleDateFormat1.format(cal.getTime());
		cal.add(Calendar.MONTH, -1);
		String end = simpleDateFormat1.format(cal.getTime());
		return new String[] { end, begin };
	}

	/**
	 * 得到最近一周的日期时间数组串,前面小时间,后面大时间
	 * 
	 * @return ['2011-06-29 09:33:12','2011-07-06 09:33:12']
	 */
	public static String[] getNearWeekDateTimeStr() {
		Calendar cal = Calendar.getInstance();
		String begin = simpleDateFormat3.format(cal.getTime());
		cal.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
		String end = simpleDateFormat3.format(cal.getTime());
		return new String[] { end, begin };
	}

	/**
	 * 得到最近三天的日期时间数组串,前面小时间,后面大时间
	 * 
	 * @return ['2011-07-03 09:33:12','2011-07-06 09:33:12']
	 */
	public static String[] getNearThreeDayDateTimeStr() {
		Calendar cal = Calendar.getInstance();
		String begin = simpleDateFormat3.format(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, -3);
		String end = simpleDateFormat3.format(cal.getTime());
		return new String[] { end, begin };
	}

	/**
	 * 得到今明后天
	 * 
	 * @param format
	 * @return
	 */
	public static String[] getThreeday(String format) {
		String[] theDate = new String[3];
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			Date inputDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);

			cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear + 0);
			theDate[0] = dateFormat.format(cal.getTime());

			cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear + 1);
			theDate[1] = dateFormat.format(cal.getTime());

			cal.set(Calendar.DAY_OF_YEAR, inputDayOfYear + 2);
			theDate[2] = dateFormat.format(cal.getTime());

		} catch (Exception e) {
		}
		return theDate;
	}

	public static void main(String[] args) {
		String[] a = DateUtils.getThreeday("yyyyMMdd");
		for (String s : a) {
			System.out.println(s);
		}

	}

	/**
	 * 得到今天的日期时间数组串,前面小时间,后面大时间
	 * 
	 * @return ['2011-07-03 00:00:00','2011-07-03 23:59:59']
	 */
	public static String[] getNearTodayDateTimeStr() {
		Calendar cal = Calendar.getInstance();
		String today = simpleDateFormat1.format(cal.getTime());
		return new String[] { today + " 00:00:00", today + " 23:59:59" };
	}

	/**
	 * 得到昨天的日期时间数组串,前面小时间,后面大时间
	 * 
	 * @return ['2011-07-03 00:00:00','2011-07-03 23:59:59']
	 */
	public static String[] getNearYesterdayDateTimeStr() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String today = simpleDateFormat1.format(cal.getTime());
		return new String[] { today + " 00:00:00", today + " 23:59:59" };
	}

	/**
	 * 取得该月的第一天
	 * 
	 * @param date
	 *            传入null时，默认取当前时间
	 * @return
	 * @author feng.li 2011-07-05
	 */
	public static Date getCurrentMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();

		if (null != date) {
			cal.setTime(date);
		}
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	/**
	 * 取得下月的第一天
	 * 
	 * @param date
	 *            传入null时，默认取当前时间
	 * @return
	 * @author feng.li 2011-07-05
	 */
	public static Date getNextMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();

		if (null != date) {
			cal.setTime(date);
		}
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	/**
	 * 取得在当天0时0分0秒的基础增加或减少N天的时间
	 * 
	 * @param add
	 *            增加或减少的偏移量
	 * @return
	 */
	public static Date getSetTime(int add) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.DAY_OF_MONTH, add);

		return calendar.getTime();
	}

	public static Timestamp stringToTimestamp(String date) {
		Timestamp ts = null;
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ts = new Timestamp(format.parse(date).getTime());
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return ts;
	}

	/**
	 * Date转化为字符串
	 * 
	 * @param Date
	 *            日期
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateToStr(Date date) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
			return sdf.format(date);
		}
	}

	/**
	 * Date转化为字符串
	 * 
	 * @param Date
	 *            日期
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String getDateToStr(Date date, String format) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}
	}
	
	/**String 转化date 验证
	 * <pre>
	 * @param date
	 *         时间
	 * @return
	 * </pre>
	 */
	public static boolean isDateMatch(String date) {
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(date);
        return m.matches();
    }
	/**
	 * 把时间根据时、分、秒转换为时间段
	 * @param StrDate
	 */
	public static String getTimes(String StrDate){
		String resultTimes = "";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    java.util.Date now;
	    
	    try {
	    	now = new Date();
	    	java.util.Date date=df.parse(StrDate);
	    	long times = now.getTime()-date.getTime();
	    	long day  =  times/(24*60*60*1000);
	    	long hour = (times/(60*60*1000)-day*24);
	    	long min  = ((times/(60*1000))-day*24*60-hour*60);
	    	long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);
	        
	    	StringBuffer sb = new StringBuffer();
	    	//sb.append("发表于：");
	    	if(hour>0 ){
	    		sb.append(hour+"小时前");
	    	} else if(min>0){
	    		sb.append(min+"分钟前");
	    	} else{
	    		sb.append(sec+"秒前");
	    	}
	    		
	    	resultTimes = sb.toString();
	    } catch (ParseException e) {
	    	e.printStackTrace();
	    }
	    
	    return resultTimes;
	}
}
