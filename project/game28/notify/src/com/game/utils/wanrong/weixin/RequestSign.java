package com.game.utils.wanrong.weixin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.game.utils.StringUtil;

public class RequestSign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4790951813642887257L;

	/**
	 *
	 * createSignStr: 创建加密的字符串. <br/>
	 *
	 * @version 1.0
	 * @param jsonObj
	 * @param signField
	 * @return
	 * @throws Exception
	 * @since JDK1.7
	 *
	 */
	public static String createSignStr(JSONObject jsonObj, String signField) throws Exception {
		try {
			jsonObj.remove(signField);
			int signNo = Integer.parseInt(StringUtil.random(3));
			String signStr = getJson2SignStr(jsonObj, signNo, "sign");
			BaopayMd5 baopayMd5 = new BaopayMd5();
			return baopayMd5.getMD5ofStr(signStr) + signNo;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getJson2SignStr(JSONObject jsonObj, int signNo, String signField) throws Exception {
		try {
			if(StringUtils.isNotBlank(signField)){
				jsonObj.remove(signField);
			}
			List<String> lstr = new ArrayList<>();
			Iterator<String> iterator = jsonObj.keySet().iterator();
			while (iterator.hasNext()) {
				lstr.add(iterator.next());
			}
			// 调用数组的静态排序方法sort,且不区分大小写
			String[] toBeStored = lstr.toArray(new String[lstr.size()]);
			Arrays.sort(toBeStored, String.CASE_INSENSITIVE_ORDER);
			String signStr = "{";
			for (String istr : toBeStored) {
				signStr = signStr + istr + ":" + jsonObj.getString(istr);
			}
			signStr = signStr + "}";
			signStr = signStr.replace("\n", "");
			signStr = signStr.replace("'", "");
			signStr = signStr.replace("\"", "");
			signStr = signStr.replace(",", "");
			signStr = signStr.replace(" ", "");
			signStr = signStr.substring(signStr.length() / signNo);
			return signStr;
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean checkSign(String jsonStr,String signjostr, String signField) throws Exception {
		try {
			JSONObject jsonObj = JSONObject.parseObject(jsonStr);
			int signNo = Integer.parseInt(signjostr.substring(32));
			String signStr = getJson2SignStr(jsonObj, signNo, "");
			BaopayMd5 baopayMd5 = new BaopayMd5();
			String sign = baopayMd5.getMD5ofStr(signStr) + signNo;
			if (sign.equals(signjostr)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		}
	}

}