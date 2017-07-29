package com.game.utils.common.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class DateUtil extends org.apache.commons.lang3.time.DateUtils{
	protected final static Log log = LogFactory.getLog(DateUtil.class);
	public final static String[] ChineseWeek = { "日", "一", "二", "三", "四", "五", "六","日" };
	
	/**
	 * new SimpleDateFormat("yyyy-MM-dd");
	 * */
	public final static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); 
	/**
	 * new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 * */
	public final static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	/**
	 * new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * */
	public final static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * new SimpleDateFormat("yyyyMMdd");
	 * */
	public final static SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyyMMdd");
	/**
	 * new SimpleDateFormat("yyyyMMddHHmmss")
	 * */
	public final static SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * new SimpleDateFormat("yyyyMMddHHmm")
	 * */
	public final static SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("yyyyMMddHHmm");
	
	/**
	 * new SimpleDateFormat("HH:mm:ss")
	 * */
	public final static SimpleDateFormat simpleDateFormat7 = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 *  new SimpleDateFormat("yyyy年MM月dd日")
	 * */
	public final static SimpleDateFormat simpleDateFormat8 = new SimpleDateFormat("yyyy年MM月dd日");
	
	/**
	 *  new SimpleDateFormat("yyMMddHHmmss")
	 * */
	public final static SimpleDateFormat simpleDateFormat9 = new SimpleDateFormat("yyMMddHHmmss");
	public static final SimpleDateFormat ymdhms=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	public final static SimpleDateFormat simpleDateFormat10 = new SimpleDateFormat("MM-dd HH:mm");

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
	
	public static Date stringToDate(String source){
		SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy" ,Locale.US);
		 try {
			return sdf.parse(source);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return simpleDateFormat2.format(d1).equals(simpleDateFormat2.format(d2));
	}
	/**
	 * 英文操作系统下 强制得到中文yyyy-MM-dd 星期
	 *	以上午10点为分界 
	 * */
	public static String getChineseYMDWeek(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		int hour=c.get(Calendar.HOUR_OF_DAY);
		if(hour<11)
			c.add(Calendar.DAY_OF_MONTH, -1);
		return simpleDateFormat8.format(c.getTime())+" 星期"+ChineseWeek[c.get(Calendar.DAY_OF_WEEK)-1];
	}
	/**
	 * 英文操作系统下 强制得到中文yyyy-MM-dd 星期
	 *	以上午10点为分界 
	 * */
	public static String getChineseYMDWeeklq(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		int hour=c.get(Calendar.HOUR_OF_DAY);
		if(hour<12)
			c.add(Calendar.DAY_OF_MONTH, -1);
		return simpleDateFormat8.format(c.getTime())+" 星期"+ChineseWeek[c.get(Calendar.DAY_OF_WEEK)-1];
	}
	/**
	 * 英文操作系统下 强制得到中文yyyy-MM-dd 星期
	 *	标准
	 * */
	public static String getChineseYMDWeek_(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);		
		return simpleDateFormat1.format(c.getTime())+" 星期"+ChineseWeek[c.get(Calendar.DAY_OF_WEEK)-1];
	}
	
	/**
	 * 英文操作系统下 强制得到中文yyyy-MM-dd 星期
	 *	标准
	 * */
	public static String getChineseYMDWeek_s(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);		
		return simpleDateFormat8.format(c.getTime())+" 星期"+ChineseWeek[c.get(Calendar.DAY_OF_WEEK)-1];
	}
	
	public static String getChineseYMDWeek_HMS(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);		
		return simpleDateFormat8.format(c.getTime())+" 星期"+ChineseWeek[c.get(Calendar.DAY_OF_WEEK)-1]+" "+simpleDateFormat7.format(c.getTime());
	}
	
	public static String dateadd(Date d,int field,int num,String format){
		SimpleDateFormat dateFormat =null;
		if(format==null)
			dateFormat=simpleDateFormat1;
		else
			 dateFormat=new SimpleDateFormat(format);
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		c.add(field, num);
		return dateFormat.format(c.getTime());
	}
	
	/**
	 * 根据日期,取今天的0:0:0 和明天的0:0:0 用到between 查询
	 * @param d
	 * @return
	 */
	public static Date[] getDateBetween(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Calendar c1=(Calendar)c.clone();
		c1.add(Calendar.DAY_OF_MONTH, 1);
		return new Date[]{c.getTime(),c1.getTime()};
	}
	
	/**
	 * 根据日期和参数i,取此日期的0:0:0 和此日期i天后的0:0:0 用到between 查询
	 * @param d
	 * @param i
	 * @return
	 */
	public static Date[] getDateBetween(Date d,int i){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Calendar c1=(Calendar)c.clone();
		c1.add(Calendar.DAY_OF_MONTH, i);
		return new Date[]{c.getTime(),c1.getTime()};
	}
	  /**
     * 获取当前日期且转换为long
     * 
     * 
     * @param format
     *            格式如：(yyyyMMdd、yyyyMMddHHmmss)
     * @return String
     */
    public static long getDateToNum(Date date,String format) {
        return Integer.parseInt(DateUtil.getDateToStr(date,format));
    }
    /**
	 * 获取某一天
	 * 
	 * @param days
	 *            天数
	 * @return Date
	 */
	public static Date getDate(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	  * Date转化为字符串
	  * @param Date 日期
	  * @return String  yyyy-MM-dd HH:mm:ss
	  */
	 public static String getDateToStr(Date date) {
	     if (date == null) {
	         return null;
	     } else {
	         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	public static Timestamp getCurTimesTamp(){
	    return new Timestamp(System.currentTimeMillis());
	}
}
