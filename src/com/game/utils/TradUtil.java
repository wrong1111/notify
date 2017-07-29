package com.game.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 交易工具类
  * @ClassName: TradUtil
  * @Description: TODO
  * @date 2015年11月26日 上午10:42:14
 */
public class TradUtil {
	
	/**
	 * 返回 tradType+issueno+6位随机数+n
	 * @param tradType 类型
	 * @param issueno 期号
	 * @param 
	 * @return
	 */
	public static String getOrderNo(String tradType,String issueno,String n){
		return tradType+""+issueno+TradUtil.getRandom(4)+n;
	}
	
	/**
	 *  获取交易编号 
	  * @Title: getTradingNo
	  * @param  tradType 交易类型  长度不能超过4个字节
	  * @return String   交易编号
	  * @throws
	 */
	public static String getTradingNo(String tradType) {
		StringBuffer sBuffer = new StringBuffer(26);
		if(StringUtils.isBlank(tradType)){
			tradType = "";
		}
		if(tradType.length()>4){
			throw new ArrayIndexOutOfBoundsException();
		}
		sBuffer.append(tradType).append(new SimpleDateFormat("yyMMddHHmmss").format(new Date()))
				.append(TradUtil.getRandom(6));
		return sBuffer.toString();
	}
	
	/**
	 * 生成len 位随机整数
	 * 最小1位，最大30位
	  * @Title: getSexRandom
	  * @return int    
	  * @throws
	 */
	public static String getRandom(int len){
		if (len<1) {
			len = 1;
		}
		if(len>30){
			len = 30;
		}
		double f = (Math.random()*9+1);
		long p= pow2(BigInteger.valueOf(10), len-1).longValue();
		BigDecimal big = BigDecimal.valueOf(f).multiply(BigDecimal.valueOf(p));
		return String.valueOf(big).replaceAll("[-|.]", "").substring(0, len);
	}	
	public static Random random = new Random();
	/**
	 * 生成len 位随机整数
	 *  包括数字字母
	  * @Title: getSexRandom
	  * @return int    
	  * @throws
	 */
	public static String getRandomStr(int len){
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < len; i++) {
			boolean isChar = (random.nextInt(2) % 2 == 0);// 输出字母还是数字
			if (isChar) { // 字符串
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				ret.append((char) (choice + random.nextInt(26)));
			} else { // 数字
				ret.append(Integer.toString(random.nextInt(10)));
			}
		}
		return ret.toString();
	}		
	public static BigInteger pow2(BigInteger x,int n){  
	    if(n==0)  
	        return BigInteger.valueOf(1);  
	    else {  
	        if(n%2==0)  
	            return pow2(x.multiply(x), n/2);  
	        else   
	            return pow2(x.multiply(x), (n-1)/2).multiply(x);  
	          
	    }  
	      
	} 
	
	/**
	 * 验证是否是金额
	  * @Title: isMoney
	  * @param  str
	  * @param @return 
	  * @return boolean    
	  * @throws
	 */
	
	public static boolean isMoney(String str) {
		Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}

	}
	
	/**
	 * 
	 * @param type
	 *       类型订单
	 * @param num
	 *       生成第几条订单
	 * @return
	 */
	public static String getOrderNo(String type, int num) {
		if (num > 999 && num <= 0) {
			throw new IllegalArgumentException("num can not be greater than 999 or less than 0");
		}
		StringBuffer zero = new StringBuffer();
		StringBuffer orderno = new StringBuffer(18);
		String day = DateUtils.simpleDateFormat4.format(new Date());
		LocalDateTime time = LocalDateTime.now();
		String second = String.valueOf(time.getHour() * 60 * 60 + time.getMinute() * 60 + time.getSecond());
		if (second.length() < 6) {
			int count = 6 - second.length();
			for (int i = 0; i < count; i++) {
				zero.append("0");
			}
		}
		orderno.append(type).append(day).append(second);
		if (num < 10) {
			orderno.append("00" + String.valueOf(num));
		} else if (9 < num && num < 100) {
			orderno.append("0" + String.valueOf(num));
		} else {
			orderno.append(String.valueOf(num));
		}

		return orderno.toString();
	}
	
	/**
	 * <pre>
	 * @param cashmoney 可提现金
	 * @param frozenmoney 不可提现金
	 * @param money  金额
	 * @return  BigDecimal[2]  
	 *         bigDecimal[0]  可提现金
	 *         bigDecimal[1]  不可提
	 * </pre>
	 */
	public static BigDecimal[] splitMoney(BigDecimal cashmoney, BigDecimal frozenmoney, BigDecimal money) {
		BigDecimal bigDecimal[] = new BigDecimal[] { BigDecimal.valueOf(0.00), BigDecimal.valueOf(0.00) };
		if (money.compareTo(cashmoney.add(frozenmoney))>0) {
			throw new IllegalArgumentException();
		}
		if (frozenmoney.compareTo(money)>=0) {
			bigDecimal[1]=money;
		}else{
			bigDecimal[0]=money.subtract(frozenmoney);
			bigDecimal[1]=frozenmoney;
		}
		return bigDecimal;
	}
}
