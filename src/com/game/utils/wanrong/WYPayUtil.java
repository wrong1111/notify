package com.game.utils.wanrong;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.utils.Posturl;
import com.game.utils.PropertiesUtil;
import com.game.utils.StringUtil;
import com.game.utils.TradUtil;
import com.game.utils.rl.SignUtil;
/**
 * @author wrong1111
 * 前海万融支付
 * 
 * d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPTVBWDhERkY=&commodityName=支付WR170227122904649018&
 * imgUrl=http://113.107.235.97:9080/payment-gate-web/gateway/api/getQRCodeImage?d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPTVBWDhERkY=
 * &merNo=310440300001134&notifyUrl=http://admin.xyungame.net/notify/wanrong&orderDate=20170227&orderNo=WR170227122904649018&productId=0108&requestNo=RQ170227122905900194&respCode=0000&respDesc=交易成功&returnUrl=http://admin.xyungame.net/notify/wanrong&transAmt=100&transId=10&version=V1.0&signature=KffmINzjSD0iz3+/C8MgSftCeQuW1jsNcRdg2Z2FdOAKBkOUAnQsM4F4aWO/D4up6FTd1vIIrjtxCijXlcpOSi5eyp+93H2svwnyF5KqLh1zxjruPSVEOznEPs0+Q2AcMtUI5UojExrjsO0wwJBGpPzWlBqofl1IhauAssSDECT1WISGujSbkzCeCINIIk4DzY/SqtNIJP1RPaeks0Q4mG8JZNYZhvgH+uo4lhmk82feGzrUAvph3lhJCpLndKBc08gC8w4IHxOsDPNvQy0oqvNw9kNMn2mc5Fggew3Tse5QsoURwR2Jp8R8qASuaIffGxKhyjIe1sIDyZ8iO9rv3Q==
 */
public class WYPayUtil  {

	static Logger log  =Logger.getLogger(WYPayUtil.class);
	
	//respCode=0000&transactionId=4004482001201702271491728017&orderDate=20170227&respDesc=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F&transAmt=100&productId=0108&orderNo=WR170227103310624655&timeEnd=20170227103503
	//&bankType=CFT&transId=10&signature=SsHaK%2F0CsSnzrGh6oQ3Y5ANm86lH1Lox11odMRZxDocym0oJzpVTkdeUdHV5Dq%2BeXBlFeM14P60m9fts2j5ruGntLDW5S6Bm1HbbutAKKgP0B%2FJr774Of61PKP3g9u09bIzfB9myptbVp9mp%2BtoK1JpC0h0SLCFG7hW8osWj4rDoyrhV9Zok0nZhKgcyuCH20seGs08DoBAYxcR2tGTsaj6HAPbmXZYQS0Od9Mk1BRHQuOPY9hwkAqzzO%2BD%2BryWR15fYwTC8jGh1yYU3w9J09z%2FiyU%2FBcfntGMwHP8kgHMR6RHYV8h%2Fxi7GW7eTnWrRvDBijlTc5r6DzedqvMjyTYg%3D%3D&merNo=310440300001134&orderId=200000211990
	public static Map<String,Object> parseString(String responseStr){
		Map<String,Object> datamap = new TreeMap<String,Object>();
		String[] str = StringUtils.splitPreserveAllTokens(responseStr,"&");
		for(String s : str){
			String[] tmps  = StringUtils.splitPreserveAllTokens(s,"=");
			if(tmps !=null && tmps.length ==2){
				datamap.put(tmps[0], tmps[1]);
			}else if(tmps!=null && tmps.length >=2){
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
	public static String postData(Map<String,Object> datamap,String posturl) throws Exception{
		return Posturl.postRequest(posturl,datamap,null,30000);
	}
	
	public static String test(BigDecimal paymoney,String tradeno,String productid,String tranid){
		try {
		 DefaultHttpClient httpClient = new SSLClient();
	        HttpPost postMethod = new HttpPost(PropertiesUtil.getValue("wanrong.url"));
	        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	        nvps.add(new BasicNameValuePair("requestNo", StringUtil.getTradingNo("RQ")));
	        nvps.add(new BasicNameValuePair("version", "V1.0"));
	        nvps.add(new BasicNameValuePair("productId", productid));
	        nvps.add(new BasicNameValuePair("transId", tranid));
	        nvps.add(new BasicNameValuePair("merNo", PropertiesUtil.getValue("wanrong.merNo")));
	        nvps.add(new BasicNameValuePair("orderDate", new SimpleDateFormat("yyyyMMdd").format(new Date())));
	        nvps.add(new BasicNameValuePair("orderNo", tradeno));
	        nvps.add(new BasicNameValuePair("returnUrl", PropertiesUtil.getValue("wanrong.returnurl")));
	        nvps.add(new BasicNameValuePair("notifyUrl", PropertiesUtil.getValue("wanrong.notifyurl")));
	        nvps.add(new BasicNameValuePair("transAmt", String.valueOf(paymoney.intValue()*100)));
	        nvps.add(new BasicNameValuePair("commodityName", "支付"+tradeno));
	        nvps.add(new BasicNameValuePair("signature", SignUtils.signData(nvps,PropertiesUtil.getValue("wanrong.public_key_path"))));
	        
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
         HttpResponse resp = httpClient.execute(postMethod);
         String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
         int statusCode = resp.getStatusLine().getStatusCode();
         if(log.isInfoEnabled()){
         	log.error(str);
         }
         System.out.println(str);
         if (200 == statusCode) {
             boolean signFlag = SignUtils.verferSignData(str,PropertiesUtil.getValue("wanrong.public_key_path"));
             if (!signFlag) {
                 System.out.println("验签失败");
                 return "";
             }
//           
             System.out.println("验签成功");
         }
         log.error("返回错误码:" + statusCode);
         } catch (Exception e) {
        	 // TODO Auto-generated catch block
        	 e.printStackTrace();
         }
		return "";
	}
	/**
	 * 对接的民生银行的，润联支付
	 * @param paymoney 
	 * @param tradeno
	 * @param 0104-微信扫码支付 0109-支付宝扫码支付
	 * @param tranid 10 扫码支付
	 * @return
	 */
	public static Map<String,Object> createRL(String orgNo,String merNo,String returnurl,String notifyurl,String privateKeyPath,BigDecimal paymoney,String tradeno,String productid,String tranid){
		try {
			TreeMap<String,Object> datamap  = new TreeMap<String,Object>();
			datamap.put("requestNo",StringUtil.getTradingNo("RQ"));
			datamap.put("version","V2.0");
			datamap.put("productId",productid);
			datamap.put("transId",tranid);
			datamap.put("orderNo", tradeno);
			datamap.put("orgNo",orgNo);
			datamap.put("accessObject", "AT0102");//机构接入
			datamap.put("orderDate",new SimpleDateFormat("yyyyMMdd").format(new Date()));
			datamap.put("returnUrl",returnurl);
			datamap.put("notifyUrl",notifyurl);
			datamap.put("transAmt",String.valueOf(paymoney.intValue()*100));//String.valueOf(paymoney.intValue()*100)
			datamap.put("commodityName", "支付"+tradeno);
			datamap.put("subMerNo",merNo);//报备商户
			if("0109".equals(productid)){//支付宝扫码支付需要填
				datamap.put("storeId", "1001");
				datamap.put("terminalId","1001");
			}
			String sign = SignUtil.signData(datamap,privateKeyPath);
			 datamap.put("signature",sign);
			 return datamap;
			
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		  return null;
	}
	/**
	 * 对接的浦发银行，--万融支付
	 * @param paymoney 
	 * @param tradeno
	 * @param 微信扫码 0108,支付宝扫码 0119
	 * @param tranid 10 扫码支付
	 * @return
	 */
	public static Map<String,Object> createWeixin(String merNo,String subMchId,String returnurl,String notifyurl,String privateKeyPath,BigDecimal paymoney,String tradeno,String productid,String tranid){
		try {
			Map<String,Object> datamap  = new TreeMap<String,Object>();
			datamap.put("requestNo",StringUtil.getTradingNo("RQ"));
			datamap.put("version","V1.1");
			datamap.put("productId",productid);
			datamap.put("transId",tranid);
			datamap.put("orderNo", tradeno);
			datamap.put("merNo",merNo);
			datamap.put("orderDate",new SimpleDateFormat("yyyyMMdd").format(new Date()));
			datamap.put("returnUrl",returnurl);
			datamap.put("notifyUrl",notifyurl);
			datamap.put("transAmt",String.valueOf(paymoney.intValue()));
			datamap.put("commodityName", "支付"+tradeno);
			datamap.put("subMchId",subMchId);
			String sign =  SignUtils.signData(datamap,privateKeyPath);
			datamap.put("signature",sign);
			 return datamap;
			
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		  return null;
	}
	public static void main(String[] args) throws Exception{
//		String s = "respCode=0000&transactionId=4004482001201702271491728017&orderDate=20170227&respDesc=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F&transAmt=100&productId=0108&orderNo=WR170227103310624655&timeEnd=20170227103503&bankType=CFT&transId=10&signature=SsHaK%2F0CsSnzrGh6oQ3Y5ANm86lH1Lox11odMRZxDocym0oJzpVTkdeUdHV5Dq%2BeXBlFeM14P60m9fts2j5ruGntLDW5S6Bm1HbbutAKKgP0B%2FJr774Of61PKP3g9u09bIzfB9myptbVp9mp%2BtoK1JpC0h0SLCFG7hW8osWj4rDoyrhV9Zok0nZhKgcyuCH20seGs08DoBAYxcR2tGTsaj6HAPbmXZYQS0Od9Mk1BRHQuOPY9hwkAqzzO%2BD%2BryWR15fYwTC8jGh1yYU3w9J09z%2FiyU%2FBcfntGMwHP8kgHMR6RHYV8h%2Fxi7GW7eTnWrRvDBijlTc5r6DzedqvMjyTYg%3D%3D&merNo=310440300001134&orderId=200000211990";
//		try {
//			s = URLDecoder.decode(s, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(WYPayUtil.parseString(s)); 0108
//		Map<String,Object> data = WYPayUtil.createWeixin(BigDecimal.valueOf(1), StringUtil.getTradingNo("WR"),"0108","10");
//		String urls = PropertiesUtil.getValue("wanrong.url");
//		String result =  Posturl.postRequest(urls,data,null,30000);
//		try {
//			result = URLDecoder.decode(result, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	//	String result="codeUrl=d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXlsQ3NBYk8=&commodityName=支付WR170227211118271316&imgUrl=http://113.107.235.97:9080/payment-gate-web/gateway/api/getQRCodeImage?d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXlsQ3NBYk8=&merNo=310440300001134&notifyUrl=http://admin.xyungame.net/notify/wanrong&orderDate=20170227&orderNo=WR170227211118271316&productId=0108&requestNo=RQ170227211118545103&respCode=0000&respDesc=交易成功&returnUrl=http://admin.xyungame.net/notify/wanrong&transAmt=100&transId=10&version=V1.0&signature=jGs4n0HvaCTNS5/8RFz9F0XhC6PBMJ5EmTPmLq7QUvEjiAud/GH73raHXwOKPN1/lu6pVQoY3nxW384ekUtfTuZQRGVmdarh+rx9m7op1wtXNTR6cy5MkIXJRv9NarK94KG1NYwleAMy+U6R8hALl/HgLzy3Aafn+bJkndOAYdJi8kd0oRstxS/xzSB6l/65DWxLGlEnHBGZCVb0d8tIEOM3HP2qcICF+oUQNbOLhazNHLVa048liPgMq93vuEs1lbSXGn6WBYXVfVsBBJVyF3TH/HTxHlnc9FwD9ap7/OWDMiWLv08Ezf/oeDflsWPMgjO6oMQh01/4xTGBgAVz7g==";
//		System.out.println("result-->"+result);
	//	TreeMap<String,Object>	data = (TreeMap<String, Object>) StringUtil.parseString(result);
//		Object signature = data.get("signature");
//		data.remove("signature");
	//	String s = StringUtil.buildstr((TreeMap)data);
//		 boolean flag = RSAUtil.verifyByKeyPath(s, signature.toString(), PropertiesUtil.getValue("wanrong.public_key_path"), "UTF-8");
//		if(SignUtils.verferSignData(s,PropertiesUtil.getValue("wanrong.public_key_path"))){
//			if(log.isInfoEnabled()){
//				log.info("recharge->返回数据["+data+"]");
//			}
//		}else{
//			log.error("recharge-->校验未通过["+result+"]");
//		}
		/*
		Map<String,Object> d = new HashMap<String,Object>();
		if(data !=null && "0000".equals(data.get("respCode").toString())){
			String codeurl = data.get("codeUrl").toString();
			codeurl = new String(Base64Util.decode(codeurl),"UTF-8");
			String file = Constants.getConfigkey("common.file.path");
			String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("upload.image.base64.domain"),
					file,StringUtil.mkdirect("/qrcode"),
					codeurl, file+"/logo.jpg",StringUtil.mkfilename("jpg"));
			if(log.isInfoEnabled()){
				log.info("recharge->img["+path_+"]");
			}
			d.put("img",path_);
			String imgurl = data.get("imgUrl").toString();
			d.put("imgurl", imgurl);
			if(StringUtils.isNotBlank(imgurl)){
				String[] s = StringUtils.splitPreserveAllTokens(imgurl,"?");
				if(s!=null && s.length ==2 && StringUtils.isNotBlank(s[1])){
					s[1] = new String(Base64Util.decode(s[1]),"UTF-8");
				}
				d.put("imgurl",s[0]+"?"+s[1]);
			}
			d.put("returncode", "1");
		}
		if(log.isInfoEnabled()){
			log.info("recharge->["+d+"]");
		}*/
		
		String merNo = 	PropertiesUtil.getValue("wrbs.merNo");
		String returnurl=PropertiesUtil.getValue("wrbs.returnurl");
		String notifyurl = PropertiesUtil.getValue("wrbs.notifyurl");
		String privateKeyPath ="D:/work/project/game28/webproject/src/zhongma_prv.pem";// PropertiesUtil.getValue("wrbs.private_key_path");
		String publicKeyPath = "D:/work/project/game28/webproject/src/zhongma_pub.pem";//PropertiesUtil.getValue("wrbs.public_key_path");
		String posturl =  PropertiesUtil.getValue("wrbs.payurl");
		String orgNo=PropertiesUtil.getValue("wrbs.orgNo");;
		System.out.println(privateKeyPath+","+publicKeyPath);
		
		 Map<String,Object> data = WYPayUtil.createRL(orgNo,merNo,returnurl,notifyurl,privateKeyPath,new BigDecimal ("10"),TradUtil.getTradingNo("TS"), "0104", "10");
		 System.out.println("参数-->"+JSON.toJSONString(data));
			String result = WYPayUtil.postData(data,posturl);
			if(StringUtils.isBlank(result)){
				System.out.println("result空--------");
			}else{
				System.out.println(result.length()+"result-->"+result);
				if(result.length()<50){
					System.out.println("result异常..");
					return;
				}
				JSONObject jsonObject = JSON.parseObject(result);
				TreeMap<String, String> treeMap = new TreeMap<>();
				for (Entry<String, Object> entry : jsonObject.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue().toString();
					treeMap.put(key, value);
				}

				if(SignUtil.verferSignData(treeMap,publicKeyPath)){
					if(log.isInfoEnabled()){
						log.info("recharge->返回数据["+data+"]");
					}
				}else{
					log.error("recharge-->校验未通过["+result+"]");
				}
			}
	}
	
}
