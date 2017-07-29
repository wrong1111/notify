package com.game.utils.wx;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;





import org.apache.log4j.Logger;

import com.game.utils.PropertiesUtil;
import com.game.utils.StringUtil;
import com.game.utils.TradUtil;
import com.game.utils.common.date.DateUtil;
import com.game.utils.encription.Md5Util;

/**
 * @author wyong 微信支付工具类。
 */
 
public class WXUtil {
	static Logger log = Logger.getLogger(WXUtil.class);
	
	/**
	 * @param test
	 * @param money
	 * @param remark
	 * @param sucessurl
	 * @param failurl
	 * @param orderno
	 * @param tradeno
	 * @return
	 * 
	 * 公众号id	appId	是	String(16)	wx8888888888888888	商户注册具有支付权限的公众号成功后即可获得
		时间戳	timeStamp	是	String(32)	1414561699	当前的时间，其他详见时间戳规则
	随机字符串	nonceStr	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
	订单详情扩展字符串	package	是	String(128)	prepay_id=123456789	统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
	签名方式	signType	是	String(32)	MD5	签名算法，暂支持MD5
	签名	paySign	是	String(64)	C380BEC2BFD727A4B6845133519F3AD6
	
		paytype=NATIVE，此参数必传。此id为二维码中包含的商品ID，
		trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
	 */
	public static Map<String, String> generaMap(String test, String money,
			String remark, String sucessurl, String failurl, String orderno,
			String tradeno,String ip,String paytype,String proid,String openid) {
		String notify = PropertiesUtil.getValue("wx.notify.url");
		TreeMap <String, String> returnMap = new TreeMap<String, String>();
		returnMap.put("appid",PropertiesUtil.getValue("wx.appid"));
		returnMap.put("mch_id",PropertiesUtil.getValue("wx.mch_id"));
		returnMap.put("device_info","WEB");
		returnMap.put("nonce_str",TradUtil.getRandom(16)+TradUtil.getRandom(16));
		returnMap.put("body","充值-"+tradeno);
		returnMap.put("attach",orderno);
		returnMap.put("out_trade_no",tradeno);
		returnMap.put("fee_type","CNY");
		String tradeAmount = String.valueOf((BigDecimal.valueOf(Double.valueOf(money)).multiply(new BigDecimal(100))).intValue());
		returnMap.put("total_fee",tradeAmount);
		returnMap.put("spbill_create_ip",ip);
		returnMap.put("time_start",DateUtil.date2String(new Date(), new SimpleDateFormat("yyyyMMddHHmmss")));
		returnMap.put("time_expire",DateUtil.date2String(DateUtil.addHours(new Date(), 1), new SimpleDateFormat("yyyyMMddHHmmss")));
		returnMap.put("notify_url",notify);
		returnMap.put("trade_type",paytype);
		returnMap.put("product_id",proid);
		returnMap.put("openid",openid);
		String keys  = PropertiesUtil.getValue("wx.appsecret");
		//	mdstr
		String md5str = StringUtil.buildstr(returnMap)+keys;
		log.info("微信支付预下单>>"+md5str);
		String md5key = Md5Util.md5(md5str);
		log.info("微信支付预下单-md5key>>"+md5key);
		returnMap.put("sign", md5key);
		
		HashMap<String,String> rmap  =new HashMap<String,String>();
		rmap.put("serverurl",PropertiesUtil.getValue("wx.order.url"));
		rmap.put("data",StringUtil.buildXmlstring(returnMap));
		rmap.put("tradeno",tradeno);
		return rmap;
	}
	
}
