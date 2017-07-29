package com.game.utils.wanrong;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;

import com.game.utils.PropertiesUtil;
import com.game.utils.StringUtil;

public class SignUtils {

	
	public static String signData(Map<String,Object> datamap,String privateKeyPath) throws Exception{
		StringBuffer buf = new StringBuffer();
        for (String key : datamap.keySet()) {
            buf.append(key).append("=").append((String) datamap.get(key)).append("&");
        }
        String signatureStr = buf.substring(0, buf.length() - 1);
        /*KeyInfo keyInfo = RSAUtil.getPFXPrivateKey(ConfigUtils.getProperty("private_key_pfx_path"),
                                                   ConfigUtils.getProperty("private_key_pwd"));
        String signData = RSAUtil.signByPrivate(signatureStr, keyInfo.getPrivateKey(), "UTF-8");*/
        String signData = RSAUtil.signByPrivate(signatureStr, RSAUtil.readFile(privateKeyPath, "UTF-8"), "UTF-8");
        System.out.println("请求数据：" + signatureStr + "&signature=" + signData);
        return signData;
	}
    public static String signData(List<BasicNameValuePair> nvps,String privateKeyPath) throws Exception {
        TreeMap<String, String> tempMap = new TreeMap<String, String>();
        for (BasicNameValuePair pair : nvps) {
            if (StringUtils.isNotBlank(pair.getValue())) {
                tempMap.put(pair.getName(), pair.getValue());
            }
        }
        StringBuffer buf = new StringBuffer();
        for (String key : tempMap.keySet()) {
            buf.append(key).append("=").append((String) tempMap.get(key)).append("&");
        }
        String signatureStr = buf.substring(0, buf.length() - 1);
        /*KeyInfo keyInfo = RSAUtil.getPFXPrivateKey(ConfigUtils.getProperty("private_key_pfx_path"),
                                                   ConfigUtils.getProperty("private_key_pwd"));
        String signData = RSAUtil.signByPrivate(signatureStr, keyInfo.getPrivateKey(), "UTF-8");*/
        String signData = RSAUtil.signByPrivate(signatureStr, RSAUtil.readFile(privateKeyPath, "UTF-8"), "UTF-8");
        System.out.println("请求数据：" + signatureStr + "&signature=" + signData);
        return signData;
    }

    public static boolean verferSignData(String str,String publiKeyPath) {
        System.out.println("响应数据：" + str);
        Map<String,String> result = new TreeMap<String,String>();
        //str= "codeUrl=d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXFnUnhRRmw=&commodityName=支付WR170227191957354017&imgUrl=http://113.107.235.97:9080/payment-gate-web/gateway/api/getQRCodeImage?d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXFnUnhRRmw=&merNo=310440300001134&notifyUrl=http://admin.xyungame.net/notify/wanrong&orderDate=20170227&orderNo=WR170227191957354017&productId=0108&requestNo=RQ170227191957604390&respCode=0000&respDesc=交易成功&returnUrl=http://admin.xyungame.net/notify/wanrong&transAmt=100&transId=10&version=V1.0&signature=WGjrk14SYek3SJ23czAhH8a0haXDBSsebg90zWPGgzKW8PmA+f9VyYnXLZgbvw/socJpa5u4GDWTiVI34RWpyoCNlPO6JAoAy3bb554foKjsoL3a9t6T6O5WFvTC1a6NIjKMz3l+l+QejpoyX/R2YhU9/jU3qShlIcWGPMvKaj/wgMkeCDGrpCEDapDv6wJev3p4iLf/lZYMJ9eeg39mbWfGJnLYM4pAewhFYGX9TEfR/7dZ139+F11TxGYP3itNf3DfgdmsmiWC36yPK21j4dR2NpDS7zzw8pz6XLw+t/TcNUbe546HaQQvptZ7BcoqhMa6zKvnbIriT2OiF57fig==";
        String data[] = StringUtils.splitPreserveAllTokens(str,"&");
        String signature = "";
        for (int i = 0; i < data.length; i++) {
            String[] tmp = StringUtils.splitPreserveAllTokens(data[i],"=");
            if(tmp.length ==2){
	            if ("signature".equals(tmp[0])) {
	                signature = tmp[1];
	            } else {
	            	result.put(tmp[0], tmp[1]);
	            }
            }else if(tmp.length >=3){
            	 String s = "=";
            	 int len = tmp.length-2;
            	 while(len -- >1){
            		 s = s+"=";
            	 }
            	 if ("signature".equals(tmp[0])) {
 	                signature = tmp[1]+s;
 	            } else {
 	                result.put(tmp[0],tmp[1]+s);
 	            }
            }
        }
        String signatureStr = StringUtil.buildstr(result);
        //codeUrl=d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXFnUnhRRmw=&commodityName=支付WR170227191957354017&imgUrl=http://113.107.235.97:9080/payment-gate-web/gateway/api/getQRCodeImage?d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXFnUnhRRmw=&merNo=310440300001134&notifyUrl=http://admin.xyungame.net/notify/wanrong&orderDate=20170227&orderNo=WR170227191957354017&productId=0108&requestNo=RQ170227191957604390&respCode=0000&respDesc=交易成功&returnUrl=http://admin.xyungame.net/notify/wanrong&transAmt=100&transId=10&version=V1.0
        System.out.println("验签数据：" + signatureStr);
        return RSAUtil.verifyByKeyPath(signatureStr, signature, publiKeyPath, "UTF-8");
    }
    public static boolean isWRBX(String returnurl){
    	 String merNo = "merNo="+PropertiesUtil.getValue("wanrong.merNo")+"&";
    	 if(returnurl.indexOf(merNo) > -1){
    		 return false;
    	 }
    	 return true;
    }
    
}
