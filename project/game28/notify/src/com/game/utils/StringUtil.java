package com.game.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @Title: StringUtil.java
 * @Description:
 * @date 2012-1-7 上午06:34:47
 * @version V1.0
 * @Copyright Copyright (c) 2012
 */
public class StringUtil {
	 
	/**
	 * 长度为二位以下者，还回原串
	 * 长度为二位以上到四位者，保留前前一位与最后一位中间以***替换
	 * 长度为四位以上者到八位者，保留前二位与最最后二位，中间以***替换
	 * 长度为八位以上者，保留以前三位与后三位，中间以***替换。
	 * @param number
	 * @return
	 */
	public static String formatNumber(String number){
		if(StringUtils.isBlank(number)) return "";
		if(number.length()<=2) return number;
		if(number.length()<=4) return number.substring(0,1)+"**"+number.substring(number.length()-1);
		if(number.length()<8) return number.substring(0,2)+"**"+number.substring(number.length()-2);
		return number.substring(0,3)+"**"+number.substring(number.length()-3);
	}
	/**
	 * 名称格式化，名称隐藏 <s:property
	 * value="@com.ecp.common.helper.StringUtil@formatStr(name)"/>
	 * 
	 * @param name
	 * @param myname
	 *            没有传null
	 * @return 若：中文名称 (name为空：*** name为一位：name+*** name为两位或者以下：张***
	 *         name为两位以上：张***三 ) 
	 *         
	 *         若：英文名称 (name为空：*** name为一位：name+***
	 *         name为四位或者以下：长度减一+***+最后一位 name为四位以上：前四位+***+最后一位 )
	 */
	public static String formatStr(String name) {

		if (StringUtils.isBlank(name)) {
			return "**";
		}
		if (name.length() < 2) {
			return name + "**";
		}
		String lastname  = name.substring(name.length()-1);
		String regEx = "[\u4e00-\u9fa5]";
		Matcher matcher = Pattern.compile(regEx).matcher(name);
		if (matcher.find())
			name = name.length() <= 2 ? name.substring(0, name.length() - 1)
					: name.substring(0, 2);
		else
			name = name.length() <= 2 ? name.substring(0, name.length() - 1)
					: name.substring(0, 2);
		return name + "**"+lastname;
	}

	/**
	 * 保留四个字符+**+一位
	 * @param name
	 * @return
	 */
	public static String formatStr4(String name) {

		if (StringUtils.isBlank(name)) {
			return "**";
		}
		if (name.length() < 4) {
			return name + "**";
		}
		String lastname  = name.substring(name.length()-1);
		String regEx = "[\u4e00-\u9fa5]";
		Matcher matcher = Pattern.compile(regEx).matcher(name);
		if (matcher.find())
			name = name.length() <= 4 ? name.substring(0, name.length() - 1)
					: name.substring(0, 4);
		else
			name = name.length() <= 4 ? name.substring(0, name.length() - 1)
					: name.substring(0, 4);
		return name + "**"+lastname;
	}
	
	public static String getFormatStr(String value, Object... str) {
		return MessageFormat.format(value, str);
	}

	/**
	 * 
	 * @param key
	 *            a
	 * @param val
	 *            b
	 * @return "a":"b"
	 */
	public static String replaceStr(String key, Object val) {
		String str = new StringBuffer().append("\"").append(key).append("\"")
				.append(":").append("\"").append(String.valueOf(val))
				.append("\"").toString();
		return str;
	}

	/**
	 * 加双引号
	 * 
	 * @param val
	 *            a
	 * @return "a"
	 */
	public static String replaceStr(Object val) {
		String str = new StringBuffer().append("\"").append(val).append("\"")
				.toString();
		return str;
	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpFirstChar(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return null;
	}

	/**
	 * @param input
	 *            需要转换的 成半角的字符串
	 * @return 转化成半角后的字符串
	 */
	public static String ToDBC(String input) {
		if (input == null || input.equals(""))
			return input;
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	/**
	 * 中文字符换算成二个字节，返回字节长度.
	 * 
	 * @param value
	 * @return len 长度
	 */
	public static int strlen(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 是否以指定的符号替换 * 替换敏感字串
	 * 
	 * @param keyword
	 *            (如：ecp888,壹天讯,直通车,胡锦涛,江泽民,温家宝,朱镕基,习近平,销量)
	 * @param str
	 * @return
	 */
	public static String replaceKeyword(String keyword, String str) {
		if (StringUtils.isBlank(keyword))
			return str;
		String[] key = keyword.split(",");
		for (int i = 0; i < key.length; i++) {
			String keystr = key[i];
			if (str.indexOf(keystr) != -1) {
				str = str.replaceAll(keystr, "***");
			}
		}
		return str;
	}

	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * GBK格式转换成utf-8编码
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String gbToUtf8(String str)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String s = str.substring(i, i + 1);
			if (s.charAt(0) > 0x80) {
				byte[] bytes = s.getBytes("Unicode");
				String binaryStr = "";
				for (int j = 2; j < bytes.length; j += 2) {
					// the first byte
					String hexStr = getHexString(bytes[j + 1]);
					String binStr = getBinaryString(Integer.valueOf(hexStr, 16));
					binaryStr += binStr;
					// the second byte
					hexStr = getHexString(bytes[j]);
					binStr = getBinaryString(Integer.valueOf(hexStr, 16));
					binaryStr += binStr;
				}
				// convert unicode to utf-8
				String s1 = "1110" + binaryStr.substring(0, 4);
				String s2 = "10" + binaryStr.substring(4, 10);
				String s3 = "10" + binaryStr.substring(10, 16);
				byte[] bs = new byte[3];
				bs[0] = Integer.valueOf(s1, 2).byteValue();
				bs[1] = Integer.valueOf(s2, 2).byteValue();
				bs[2] = Integer.valueOf(s3, 2).byteValue();
				String ss = new String(bs, "UTF-8");
				sb.append(ss);
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	private static String getHexString(byte b) {
		String hexStr = Integer.toHexString(b);
		int m = hexStr.length();
		if (m < 2) {
			hexStr = "0" + hexStr;
		} else {
			hexStr = hexStr.substring(m - 2);
		}
		return hexStr;
	}

	private static String getBinaryString(int i) {
		String binaryStr = Integer.toBinaryString(i);
		int length = binaryStr.length();
		for (int l = 0; l < 8 - length; l++) {
			binaryStr = "0" + binaryStr;
		}
		return binaryStr;
	}

	/**
	 * UTF-8格式的字符串，转换成GB2312
	 * 
	 * @param str
	 * @return
	 */
	public static String utf8Togb2312(String str) {
		String utf8 = "";
		String unicode = "";
		String gbk = "";
		try {
			utf8 = new String(str.getBytes("UTF-8"));
			// System.out.println(utf8);
			unicode = new String(utf8.getBytes(), "UTF-8");
			// System.out.println(unicode);
			gbk = new String(unicode.getBytes("GBK"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return gbk;
	}

	/**
	 * 将 GB2312 编码格式的字符串转换为 UTF-8 格式的字符串：
	 * 
	 * @param str
	 * @return
	 */
	public static String gb2312ToUtf8(String str) {
		String urlEncode = "";
		try {
			urlEncode = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlEncode;
	}
	
	/**
	 * 生成XML格式字符串，空值不处理。
	 * @param keys
	 * @return
	 */
	public static String buildXmlstring(Map<String,String> keys){
		StringBuilder buildstr = new StringBuilder();
		if(keys == null || keys.size() <=0) return "";
		Iterator<String> itor = keys.keySet().iterator();
		while(itor.hasNext()){
			String key  =itor.next();
			if(StringUtils.isBlank(keys.get(key))) continue;
			buildstr.append("<").append(key).append(">").append(keys.get(key)).append("</").append(key).append(">");
		}
		return "<xml>"+buildstr.toString()+"</xml>";
	}
	/**
	 * @param keys 可排列的map
	 * 值为空的，不做处理。
	 * @return
	 * map 循环生成 字串。
	 */
	public static String buildstr(Map<String,String> keys){
		StringBuilder buildstr = new StringBuilder();
		if(keys == null || keys.size() <=0) return "";
		Iterator<String> itor = keys.keySet().iterator();
		while(itor.hasNext()){
			String key  =itor.next();
			if(StringUtils.isBlank(keys.get(key))) continue;
			buildstr.append(key).append("=").append(keys.get(key)).append("&");
		}
		
		if(buildstr.length()>1){
			buildstr.setLength(buildstr.length()-1);
		}
		return buildstr.toString();
	}
	
	
	/**
	 * 特殊符号验证
	 * <pre>
	 * @param keyword
	 * @return
	 * </pre>
	 */
	private final static  String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" ,"'","\"","~","`","#","@","^","&"}; 
	public static boolean escapeExprSpecialWord(String keyword) {  
	    if (StringUtils.isNotBlank(keyword)) {  
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                return true;  
	            }  
	        }  
	    }  
	    return false;  
	}  
	
	/**
	 * ' " 替换成 -
	 * <pre>
	 * @param string
	 * @return
	 * </pre>
	 */
	public static String replaceQuotes(String string){
		if (StringUtils.isBlank(string)) return "";
		return string.replaceAll("'", "-").replaceAll("\"","-" ).replaceAll("\\s+", "");
	}
	
	 /**
	  * 直接删除特殊emoji表情符号
	 * @param text
	 * @return
	 */
	public static String filterOffUtf8Mb4(String text) {
		 if(StringUtils.isBlank(text)) return "";
		 //msg.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
		 return  text.replaceAll("[^\\u0000-\\uFFFF]", "");   
    }
	public static boolean isDayformat(String day){
		//return day.matches("^((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)$");
		return day.matches("^\\d{4}-\\d{2}-\\d{2}$");
	}
	
	public static String random(int len){
		if(len <=0) return "";
		String[] str = new String[]{"0","1","2","3","4","5","6","7","8","9"};
		Random rd = new Random();
		StringBuilder sb = new StringBuilder();
		while(len >0){
			sb.append(str[rd.nextInt(str.length)]);
			len --;
		}
		return sb.toString();
	}
	public static String randomStr(int len){
		String[] str = new String[]{"A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z","2","3","4","5","6","7","8","9"};
		if(len<1) return "";
		Random rd = new Random();
		StringBuilder sb = new StringBuilder();
		while(len >0){
			sb.append(str[rd.nextInt(str.length)]);
			len --;
		}
		return sb.toString();
	}
	public static boolean  isfloat(String target){
		String s = "^\\d+(\\.\\d+)?$";
		return target.matches(s);
	}
	
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);

               }
             }
             return new String(c);
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
		String result = String.valueOf(Math.random());
		int c = (len+1)/16+1;
		while(c-- >1){
			result += String.valueOf(Math.random());
		}
		return result = result.replaceAll("[-|.]", "").substring(0, len);
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
		return randomStr(len);
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
	 * 解析请求参数url
	 * @param responseStr
	 * @return
	 */
	public static Map<String,Object> parseString(String responseStr){
		Map<String,Object> datamap = new TreeMap<String,Object>();
		String[] str = StringUtils.splitPreserveAllTokens(responseStr,"&");
		for(String s : str){
			String[] tmps  = StringUtils.splitPreserveAllTokens(s,"=");
			if(tmps !=null && tmps.length ==2){
				datamap.put(tmps[0], tmps[1]);
			}else if(tmps!=null && tmps.length>=3){
				String sa = "=";
				int len = tmps.length-2;
				while(len-->1){
					sa = sa+"=";
				}
				datamap.put(tmps[0], tmps[1]+sa);
			}
		}
		return datamap;
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
	public static String mkdirect(String path){
		return path+"/"+DateUtils.date2String(new Date(), new SimpleDateFormat("yyyy/MM/dd"))+"/";
	}
	public static String mkfilename(String suff){
		return TradUtil.getRandomStr(10)+"."+suff;
	}
	//中文转Unicode  
    public static String gbEncoding(final String gbString) {   //gbString = "测试"  
        char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]  
        String unicodeBytes = "";     
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {     
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串  
              if (hexB.length() <= 2) {     
                  hexB = "00" + hexB;     
             }     
             unicodeBytes = unicodeBytes + "\\u" + hexB;     
        }     
        System.out.println("unicodeBytes is: " + unicodeBytes);     
        return unicodeBytes;     
    }  
	//Unicode转中文  
    public static String decodeUnicode(final String dataStr) {     
       int start = 0;     
       int end = 0;     
       final StringBuffer buffer = new StringBuffer();     
       while (start > -1) {     
           end = dataStr.indexOf("\\u", start + 2);     
           String charStr = "";     
           if (end == -1) {     
               charStr = dataStr.substring(start + 2, dataStr.length());     
           } else {     
               charStr = dataStr.substring(start + 2, end);     
           }     
           char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
           buffer.append(new Character(letter).toString());     
           start = end;     
       }     
       return buffer.toString();     
    }  
    
    /** 
     * 将emoji表情替换成* 
     *  
     * @param source 
     * @return 过滤后的字符串 
     */  
    public static String filterEmoji(String source) {  
        if(StringUtils.isNotBlank(source)){  
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");  
        }else{  
            return source;  
        }  
    }  
	public static void main(String[] args) {
		String bb="This is a smiley \uD83C\uDFA6 face\uD860\uDD5D \uD860\uDE07 \uD860\uDEE2 \uD863\uDCCA \uD863\uDCCD \uD863\uDCD2 \uD867\uDD98 ";
		System.out.println(StringUtil.replaceQuotes(StringUtil.filterEmoji(bb)));
	}
	
}
