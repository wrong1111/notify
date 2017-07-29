package com.game.utils;

import com.game.utils.encription.Md5Util;

public class TokenUtil {
	/**
	 * 生成Token MD5
	 * USERID+USERNAME+PASSWORD+用户端的浏览器版本（REQUEST。GETHEAD（“HEADER”）+签名））+ 当前时间戳
	 * 生成32长度令牌。
	 * 
	 * @return
	 */
	public static String makeSafeTokenStr(String userId,String userName,String password,String clientVesion,String pzText){
		String strs = userId+""+userName+""+password+""+clientVesion+""+pzText+System.currentTimeMillis();
		strs = Md5Util.md5_32(strs.toLowerCase());
		return strs;
	}


	/**
	 * 将用户的其他信息存储到memcached中 如果为空则用0代替
	 * 
	 * @return
	 */
	public static String makeUserDetailInfoStr(String ip, String lstime,
			String score, String account, String channelNo, String source,
			String dj) {
		return ((ip == null || ip.equals("")) ? "" : ip)
				+ "^"
				+ ((lstime == null || lstime.equals("")) ? "" : lstime)
				+ "^"
				+ ((score == null || score.equals("")) ? "" : score)
				+ "^"
				+ ((account == null || account.equals("")) ? "" : account)
				+ "^"
				+ ((channelNo == null || channelNo.equals("")) ? "" : channelNo)
				+ "^" + ((source == null || source.equals("")) ? "" : source)
				+ "^" + ((dj == null || dj.equals("")) ? "0" : dj);
	}

}
