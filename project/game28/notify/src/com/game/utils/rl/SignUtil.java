
package com.game.utils.rl;


import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class SignUtil {

	public static String signData(TreeMap<String, Object> treeMap, String privateKeyFile) throws Exception {
		TreeMap<String, Object> tMap = new TreeMap<String, Object>();
		for (Entry<String, Object> entry : treeMap.entrySet()) {
			if (entry.getValue()!=null && StringUtils.isNotBlank(entry.getValue().toString())) {
				tMap.put(entry.getKey(), entry.getValue());
			}
		}
		StringBuffer buf = new StringBuffer();
		for (String key : tMap.keySet()) {
			buf.append(key).append("=").append((String) tMap.get(key)).append("&");
		}
		String signatureStr = buf.substring(0, buf.length() - 1);
		String signData = RSACoder.signMS(signatureStr, privateKeyFile);
		return signData;
	}
	public static boolean verferSignData(TreeMap<String, String> treeMap, String publicKeyFile) throws Exception {
		if (treeMap != null && treeMap.size() > 0) {
			StringBuffer buf = new StringBuffer();
			String signature = "";
			for (String key : treeMap.keySet()) {
				if ("signature".equals(key)) {
					signature = treeMap.get(key).replace(" ", "+");
				} else if(StringUtils.isNoneBlank(treeMap.get(key))){
					buf.append(key).append("=").append((String) treeMap.get(key)).append("&");
				}else{
					 
				}
			}
			String signatureStr = buf.substring(0, buf.length() - 1);
			return RSACoder.verifyMS(signatureStr, signature, publicKeyFile);
		}
		return false;
	}
}
