package com.game.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

public class Md5Util {
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return str;
	}
	// 32位
	public static String md5_32(String text) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes("utf-8")); // 注意改接口是按照指定编码形式签名

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");
		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	// encodeHex
	public static char[] encodeHex(byte[] data) {
		int l = data.length;

		char[] out = new char[l << 1];

		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	// 16位
	public static String md5_16(String text) {
		String str_32 = md5_32(text);
		return str_32.substring(8, 24);
	}

	

	/* 小写的MD5 */
	public static String MD5lower(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static void a(String result){
		String[] str = StringUtils.splitPreserveAllTokens(result,",");
		//豹子，对子，顺子，三不同
		String okStr = "-1";
		Integer a = Integer.valueOf(str[0]);
		Integer b = Integer.valueOf(str[1]);
		Integer c = Integer.valueOf(str[2]);
		
		if(str[0].equals(str[1]) && str[0].equals(str[2]) && str[1].equals(str[2])){
			okStr = "豹子";
		}else if(!str[0].equals(str[1]) && !str[1].equals(str[2]) && !str[0].equals(str[2])){
			okStr = "三不同";
			if(b-a==1 && c-b ==1 && c-a ==2){
				okStr = "顺子";
			}
		}else{
			okStr = "对子";	
		}
		System.out.println(okStr);
	}
	// main
		public static void main(String[] args) {
			//returncode=1&orderid=C170206144318769510&paymoney=2.0000&sign=505107b55dbdc7601ec9cd1b44aeb0f8
			//String s="returncode=1&orderid=c170206144318769510&paymoney=2.0000&keyvalue=c173110c15cfd69b906ffec6d833d947";
			//System.out.println(md5(s));
			 Md5Util.a("6,4,5");
		}
}
