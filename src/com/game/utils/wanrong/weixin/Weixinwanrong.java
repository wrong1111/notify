package com.game.utils.wanrong.weixin;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.utils.Posturl;
import com.game.utils.TradUtil;

public class Weixinwanrong {

	
	 /**
	  * 除了sign以外的支付字符封装。
	 * @param orderno 订单号
	 * @param mount 金额，分为单位，正整数
	 * @param keycode 加密串
	 * @param notifyurl 成功通知地址》
	 * @return
	 * 
	 */
	public static Map<String,Object> buildParam(String orderno,String mount,String keycode,String notifyurl,String channelType){
		 Map<String,Object> param  = new TreeMap<String,Object>();
		 param.put("channelType", channelType);//浦发
		 param.put("keyCode", keycode);
		 param.put("notifyUrl", notifyurl);
		 param.put("orderDate", String.valueOf(System.currentTimeMillis()));
		 param.put("orderDesc","支付-"+orderno );
		 param.put("orderId", orderno);
		 param.put("returnUrl", notifyurl);//目前返回地址未启用
		 param.put("serviceType", "0");//公从号支付 0
		 param.put("totalAmount", mount);
		 return param;
	 }
	public static void main(String[] args) {
		String tradeno=TradUtil.getTradingNo("WX");
		String amount="100";
		String notifyurl="http://admin.xyungame.net/notify/weixinwr";
		String posturl ="http://www.crowdfun.cn/api/order/create";
		String usercode="d870f4c0653ac260";
		Map<String,Object> data = buildParam(tradeno,amount,usercode,notifyurl,"0");
		try {
			System.out.println(JSON.toJSON(data));
			String sign = RequestSign.createSignStr(JSONObject.parseObject(JSON.toJSONString(data)), "sign");
			System.out.println(sign);
			data.put("sign",sign);
			String result = Posturl.postRequestJson(posturl, data, "", "utf-8");
			System.out.println("result->"+result);
			if(StringUtils.isNotBlank(result)){
				Map<String,Object> map = JSONObject.parseObject(result);
				String code = map.get("code").toString();
				String success = map.get("success").toString();
				String signs = map.get("sign").toString();
				JSONObject obj =(JSONObject) map.get("data");
				if("0000".equals(code) && success.equals("true") && RequestSign.checkSign(obj.toJSONString(),signs, "sign")){
					System.out.println("ok");
					String orderId = obj.getString("orderId") ;
					String payurl =  obj.getString("payUrl");
					String payImg =  obj.getString("payImg");
					String mount = obj.getString("totalAmount");
				}else{
					System.out.println("false");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
}
