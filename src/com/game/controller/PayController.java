package com.game.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.entity.EcsOrderInfo;
import com.game.entity.TPayRecord;
import com.game.entity.TSysConfig;
import com.game.pojo.PayVo;
import com.game.service.OrderService2;
import com.game.service.PayService;
import com.game.service.SysService;
import com.game.utils.Constants;
import com.game.utils.MemcacheUtil;
import com.game.utils.Posturl;
import com.game.utils.PropertiesUtil;
import com.game.utils.common.BaseAction;
import com.game.utils.encription.Base64Util;
import com.game.utils.wanrong.SignUtils;
import com.game.utils.wanrong.WYPayUtil;
import com.game.utils.wanrong.weixin.RequestSign;
import com.game.utils.wanrong.weixin.Weixinwanrong;

/**
 * @author wrong1111 支付交换中心
 */

@Controller
@RequestMapping("/pay")
public class PayController extends BaseAction {

	Logger logger = Logger.getLogger(getClass());
	@Autowired
	SysService sysService;

	@Autowired
	PayService payService;

	@Autowired
	OrderService2 orderService2;

	@RequestMapping("/{orderno}/order")
	public String order(@PathVariable(value="orderno") String orderno,Model model) {
		EcsOrderInfo orderinfo = orderService2.findByOrdersn(orderno);
		if(orderinfo!=null) {
			model.addAttribute("status", "0");
			model.addAttribute("desc", "成功");
			model.addAttribute("money", orderinfo.getOrder_amount().toPlainString());
			model.addAttribute("img", "");
		}else {
			model.addAttribute("status", "-1");
			model.addAttribute("desc", "["+orderno+"]此订单不存在");
			model.addAttribute("img", "-1");
			model.addAttribute("money", "");
		}
		model.addAttribute("orderno", orderno);
		return "/pay/shop";
	}
	/**
	 * @return 请求支付 参数必须要有
	 *  money 金额 
	 *  channel 支付方式
	 *   orderno 订单号 
	 *   noticeurl 异步通知地址
	 *   memno 通讯账号
	 */
	@RequestMapping("/recharge")
	@ResponseBody
	public Object recharge(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = super.SUCESS();
		// 通过解析request 获取参数 data,partnerid,service,time,version,key
		final Map<String, String> requestdata = super.parseRequest(request);
		//result.put("partnerid", requestdata.get("partnerid"));
		// 验证参数是否合法。 0#cefp
		logger.error(requestdata);
		// 0#开头的为成功标识可以进行后续业务逻辑处理了。
		try {
			 
				String paradata = requestdata.get("data");
				Map<String, Object> jsondata = JSON.parseObject(paradata, HashMap.class);
				
				String memno = requestdata.get("partnerid");
				result.put("parnterid", memno);
				if (jsondata.get("money") == null || StringUtils.isBlank(jsondata.get("money").toString())) {
					result.put("status", "9002");
					result.put("msg", "money" + Constants.parametermap.get("9002"));
					return callback2(requestdata.get("callback"), result, request, response);
				}

				if (jsondata.get("channel") == null || StringUtils.isBlank(jsondata.get("channel").toString())) {
					result.put("status", "9002");
					result.put("msg", "channel" + Constants.parametermap.get("9002"));
					return callback2(requestdata.get("callback"), result, request, response);
				}

				if (jsondata.get("orderno") == null || StringUtils.isBlank(jsondata.get("orderno").toString())) {
					result.put("status", "9002");
					result.put("msg", "orderno" + Constants.parametermap.get("9002"));
					return callback2(requestdata.get("callback"), result, request, response);
				}

				if(jsondata.get("orderno").toString().length() >20) {
					result.put("status", "9008");
					result.put("msg", "orderno" + Constants.parametermap.get("9008")+"["+20+"]");
					return callback2(requestdata.get("callback"), result, request, response);
				}
				if (jsondata.get("noticeurl") == null || StringUtils.isBlank(jsondata.get("noticeurl").toString())) {
					result.put("status", "9002");
					result.put("msg", "noticeurl" + Constants.parametermap.get("9002"));
					return callback2(requestdata.get("callback"), result, request, response);
				}

				String money = jsondata.get("money").toString();
				if (BigDecimal.ZERO.compareTo(new BigDecimal(money)) >= 0) {
					result.put("status", "9002");
					result.put("msg", "money必须大于0的正整数");
					return callback2(requestdata.get("callback"), result, request, response);
				}
//				if (new Integer(money) % 100 != 0) {
//					result.put("status", "9002");
//					result.put("msg", "money必须大于0的正整数");
//					return callback2(requestdata.get("callback"), result, request, response);
//				}

				String channel = jsondata.get("channel").toString();
				if (!("1".equals(channel) || "2".equals(channel))) {
					result.put("status", "-1");
					result.put("msg", "channel选择项为[1,2]");
					return callback2(requestdata.get("callback"), result, request, response);
				}

				BigDecimal max = new BigDecimal(PropertiesUtil.getValue("pay.order.maxmoney"));
				 
				if (max.compareTo(new BigDecimal(money)) < 0 ) {
					result.put("status", "1031");
					result.put("msg",
							StringUtils.replaceOnce(Constants.getParamterkey("1031"), "X", max.toPlainString()));
					return callback2(requestdata.get("callback"), result, request, response);
				}
				
				String playpay = getPayName(channel);
				if(StringUtils.isBlank(playpay)) {
					result.put("status", "1035");
					result.put("msg",Constants.parametermap.get("1035"));
					return callback2(requestdata.get("callback"), result, request, response);
				}
				if(playpay.startsWith("0#")) {
					
					int payRandomFlag = 0 ;
					TSysConfig config = sysService.findByKey("pay.channel.selector");
					if(config!=null && StringUtils.isNotBlank(config.getKeyvalue())){
						payRandomFlag = Integer.valueOf(config.getKeyvalue()).intValue();
					}
					log.error("[recharge],pay.channel.selector=>"+payRandomFlag+",config.value=>"+config.getKeyvalue());
					
					String[] str = StringUtils.splitPreserveAllTokens(playpay,"#");
					playpay = str[1];
					// 生成支付信息
					PayVo payvo  = new PayVo();
					payvo.setChannel(channel);
					payvo.setPaychannel(channel);
					payvo.setMemno(memno);
					payvo.setCreatetime(new Date());
					payvo.setMoney(new BigDecimal(money));//分为单位
					payvo.setNoticeurl(jsondata.get("noticeurl").toString());
					payvo.setRequestip(super.getIpAddr(request));
					payvo.setOrderno(memno+"-"+jsondata.get("orderno").toString());
					TPayRecord record = payService.createTPayRecord(change(payvo));
					   
					int day = Calendar.getInstance(Locale.CHINESE).get(Calendar.DAY_OF_MONTH);
					int c = 1 ;
					while (c <= 2 ){//重试三次
						try{
							String tradeno = payvo.getOrderno();
							Map<String,Object> data = null;
							if(playpay.startsWith("微") && playpay.length() == 5){
								log.error("--recharege--playpay-->>"+playpay);
								String prefx = playpay.substring(1);
								String productid = "0108";
								payvo.setPaychannel(prefx+"_WXF");
								
//								if(payRandomFlag == 1 || (payRandomFlag == 2 && day%2 == 0 ) ){
//									String notifyurl = PropertiesUtil.getValue("wr."+prefx+".notifyurl");
//									String merNo = 	PropertiesUtil.getValue("wr."+prefx+".merNo");
//									String returnurl=PropertiesUtil.getValue("wr."+prefx+".returnurl");
//									String privateKeyPath = PropertiesUtil.getValue("wr."+prefx+".private_key_path");
//									String publicKeyPath = PropertiesUtil.getValue("wr."+prefx+".public_key_path");
//									String posturl =  PropertiesUtil.getValue("wr."+prefx+".url");
//									String subMchId =PropertiesUtil.getValue("wr."+prefx+".wx.subMchId");
//									 data =  requestWR(posturl,merNo,subMchId,returnurl,notifyurl,privateKeyPath,publicKeyPath,base.getMoney().toPlainString(), tradeno, productid, "10", c);
//								}else {
								String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
								String posturl = PropertiesUtil.getValue("wr.wx.payurl");
								String usercode = PropertiesUtil.getValue("wr."+prefx+".usercode");
								if(payRandomFlag == 3){//连接除浦发以外的通道
									data = requestWXWR(posturl,tradeno,String.valueOf(payvo.getMoney().intValue()),usercode,notifyurl,"2");							
								}else{ 
									//连接浦发
									data = requestWXWR(posturl,tradeno,String.valueOf(payvo.getMoney().intValue()),usercode,notifyurl,"0");
								} 
							}else if(playpay.startsWith("支") && playpay.length() == 5){
								log.error("--recharege--playpay-->>"+playpay);
								String prefx = playpay.substring(1);
								String productid = "0119";//支付宝扫码
								payvo.setPaychannel(prefx+"_ZFB");
								if(payRandomFlag == 1 || (payRandomFlag == 2 && day%2 == 0 ) ){
									String merNo = 	PropertiesUtil.getValue("wr."+prefx+".merNo");
									String returnurl=PropertiesUtil.getValue("wr."+prefx+".returnurl");
									String notifyurl = PropertiesUtil.getValue("wr."+prefx+".notifyurl");
									String privateKeyPath = PropertiesUtil.getValue("wr."+prefx+".private_key_path");
									String publicKeyPath = PropertiesUtil.getValue("wr."+prefx+".public_key_path");
									String posturl =  PropertiesUtil.getValue("wr."+prefx+".url");
									String subMchId =PropertiesUtil.getValue("wr."+prefx+".ali.subMchId");
									data =  requestWR(posturl,merNo,subMchId,returnurl,notifyurl,privateKeyPath,publicKeyPath,payvo.getMoney().toPlainString(), tradeno, productid, "10", c);
								}else if(payRandomFlag == 3){
									String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
									String posturl = PropertiesUtil.getValue("wr.wx.payurl");
									String usercode = PropertiesUtil.getValue("wr."+prefx+".usercode");
									data = requestWXWR(posturl,tradeno,String.valueOf(payvo.getMoney().intValue()),usercode,notifyurl,"2");
								}else{
									String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
									String posturl = PropertiesUtil.getValue("wr.wx.payurl");
									String usercode = PropertiesUtil.getValue("wr."+prefx+".usercode");
									data = requestWXWR(posturl,tradeno,String.valueOf(payvo.getMoney().intValue()),usercode,notifyurl,"0");
								}
							} else{
								result.put("status","1035");
								result.put("msg",Constants.getParamterkey("1035"));
								return callback2(requestdata.get("callback"), result, request, response);
							}
							c++;
							if(data == null || data.isEmpty()){
								log.error("recharge["+playpay+"]业务不支持");
								continue;
							}
							log.error("recharge-->"+JSON.toJSON(data));
							if(data.get("code")!=null && "9999".equals(data.get("code").toString())){
								result.put("status","-12");
								result.put("msg","此业务上游不支持");
								Object postdata = data.remove("postdata");
								record.setPaystr(postdata.toString());
								 record.setPayresult("FAIL");
								 record.setChannel(payvo.getPaychannel());
								 payService.updateTPayRecord(record);
								return callback2(requestdata.get("callback"), result, request, response);
							}else if(data.get("code")!=null) {
								result.put("status",data.get("code"));
								result.put("msg","此业务上游不支持["+data.get("msg")+"]");
								Object postdata = data.remove("postdata");
								record.setPaystr(postdata.toString());
								 record.setPayresult("FAIL");
								 record.setChannel(payvo.getPaychannel());
								 payService.updateTPayRecord(record);
								return callback2(requestdata.get("callback"), result, request, response);
							}else{
								data.put("m",payvo.getMoney().toPlainString());
								Object postdata = data.remove("postdata");
							    log.info("[recharge]->"+data);
							    record = new TPayRecord();
							    record.setOrderno(payvo.getOrderno());
							    record.setQrcode(data.get("imgurl").toString());
							    record.setQrcodeurl(data.get("img").toString());
							    record.setPaystr(postdata.toString());
							    record.setPayresult("SUCCESS");
							    record.setChannel(payvo.getPaychannel());
							    payService.updateTPayRecord(record);
								result.put("data",JSON.toJSON(data));
								return callback2(requestdata.get("callback"), result, request, response);
								 
							}
						}catch(Exception e){
							log.error("recharge["+playpay+"]充值请求失败->"+e.getMessage(),e);
							result.put("msg",e.getMessage());
							c++;
						}
						if(c>=3){
							result.put("status","-11");
							return callback2(requestdata.get("callback"), result, request, response);
						}
					}
					result.put("status","-11");
					result.put("msg","网络交互异常，未握手通信");
					return callback2(requestdata.get("callback"), result, request, response);					
				}else {
					String[] str = StringUtils.splitPreserveAllTokens(playpay,"#");
					result.put("status", str[0]);
					result.put("msg", str[1]);
				}
		} catch (Exception e) {
			result.put("status", "9999");
			result.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return callback2(requestdata.get("callback"), result, request, response);
	}
	
	
	private Map<String,Object> requestWR(String posturl,String merNo,String subMchId,String returnurl,String notifyurl,String privateKeyPath,String publicKeyPath,String money,String tradeno,String productid,String tranid,int count) throws Exception{
		Map<String,Object> d = new HashMap<String,Object>();
		try{
		Map<String,Object> data = WYPayUtil.createWeixin(merNo,subMchId,returnurl,notifyurl,privateKeyPath,new BigDecimal (money), tradeno, productid, tranid);
		log.error("recharge-postdata->"+JSON.toJSONString(data));
		d.put("postdata", JSON.toJSON(data));
		String result = WYPayUtil.postData(data,posturl);
		log.info("recharge->返回数据["+result+"]");
		if(StringUtils.isBlank(result)) return null;
		if(SignUtils.verferSignData(result,publicKeyPath)){
			data = WYPayUtil.parseString(result);
		}else{
			log.error("recharge-->校验未通过["+result+"]");
		}
		if(data !=null && "0000".equals(data.get("respCode").toString())){
			d.put("orderno", data.get("orderNo"));
			String codeurl = data.get("codeUrl").toString();
			codeurl = new String(Base64Util.decode(codeurl),"UTF-8");
//			String file = Constants.getConfigkey("common.file.path");
//			String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("pay.imgage.domain"),
//					file,StringUtil.mkdirect("/pay/qrcode"),
//					codeurl, file+"/logo.jpg",StringUtil.mkfilename("jpg"));
//			log.error("recharge->img["+path_+"]merNO=["+merNo+"],codeurl["+codeurl+"]");
			d.put("img",codeurl);
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
		}else{
			log.error("recharge--result["+result+"]");
			d.put("code", data.get("respCode").toString());
			d.put("msg", data.get("respDesc").toString());
		}
		if(log.isInfoEnabled()){
			log.info("recharge->["+d+"]");
		}
		}catch(Exception e){
			log.error("recharge【万融在线充值异常】merNo="+merNo+e.getMessage(),e);
		}
		return d;		 
	}
	private Map<String,Object> requestWXWR(String posturl,String tradeno,String amount,String usercode,String notifyurl,String channelType){
		 Map<String,Object> d = new HashMap<String,Object>();
		 try{
			 Map<String,Object> param   = Weixinwanrong.buildParam(tradeno,amount,usercode,notifyurl,channelType);
			 String sign = RequestSign.createSignStr(JSONObject.parseObject(JSON.toJSONString(param)), "sign");
			 param.put("sign",sign);
			 log.error("recharge-weixin.postdata->"+JSON.toJSONString(param));
			 d.put("postdata", JSON.toJSON(param));
			 String result = Posturl.postRequestJson(posturl, param, "", "utf-8");
			 if(StringUtils.isNotBlank(result)){
				 log.error("recharge-weixin-result->["+result+"]");
				Map<String,Object> map = JSONObject.parseObject(result);
				String code = map.get("code").toString();
				String success = map.get("success").toString();
				String msg = map.get("msg").toString();
				if("0000".equals(code) && success.equals("true")){
					String signs = map.get("sign").toString();
					JSONObject obj =(JSONObject) map.get("data");
					if(RequestSign.checkSign(obj.toJSONString(),signs, "sign")){
						String orderId = obj.getString("orderId") ;
						String payurl =  obj.getString("payUrl");
						String payImg =  obj.getString("payImg");
						String mount = obj.getString("totalAmount");
						String keyCode = obj.getString("keyCode");
						d.put("orderno", orderId);
//						String file = Constants.getConfigkey("common.file.path");
//						String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("pay.imgage.domain"),
//								file,StringUtil.mkdirect("/pay/qrcode"),
//								payurl, file+"/logo.jpg",StringUtil.mkfilename("jpg"));
						log.error("recharge->img["+payurl+"],keyCode=["+keyCode+"],payurl["+payurl+"],payimg["+payImg+"],mount["+mount+"]分");
						d.put("img",payurl);
						d.put("imgurl", payImg);
						d.put("returncode", "1");
					}else{
						d.put("code",code);
						d.put("msg", msg);
						log.error("recharge-result->"+result);
					}
				}else{
					log.error("recharge-result->"+result);
					d.put("code",code);
					d.put("msg", msg);
				}
			}else{
				log.error("recharge-->没有获取返回数据");
			}
		 }catch(Exception e){
			 log.error("recharge->"+e.getMessage(),e);
		 }
		return d;
	}
	private String getPayName(String channel) {
		try {
			String playpay = "";
			/**
			 * 通过 关键值 alipay.compay.channel 控制支付宝,充值账号 weixin.compay.channel 控制微信，充值账号
			 * 每隔5min去获取数据，优先达到30笔的，轮换账号
			 **/
			int count = Integer.valueOf(PropertiesUtil.getValue("pay.change.company"));

			String memcachKey = "PAY_";
			if ("1".equals(channel)) {// 微信充值
				memcachKey = "weixin.compay.channel_" + memcachKey;
				TSysConfig conf1 = sysService.findByKey("weixin.compay.channel");
				if (conf1 != null && StringUtils.isNotBlank(conf1.getKeyvalue())) {
					String[] companys = StringUtils.splitPreserveAllTokens(conf1.getKeyvalue(), ",");
					log.error("recharge-play微信账号-当前配置账号[" + conf1.getKeyvalue() + "]");
					// 从缓存中获取对应的数据。用于多台接口服务器
					String[] curCompany = null;
					Object cacheCurCompany = MemcacheUtil.get(memcachKey);
					if (cacheCurCompany == null) {
						curCompany = StringUtils.splitPreserveAllTokens(Constants.WEIXIN_LAST_PAY_COMPANY_TIME, "_");
						log.error("recharge-play微信账号切换-当前本站账号[" + Constants.WEIXIN_LAST_PAY_COMPANY_TIME + "]");
					} else {
						curCompany = StringUtils.splitPreserveAllTokens(cacheCurCompany.toString(), "_");
						log.error("recharge-play微信账号切换-当前缓存账号[" + cacheCurCompany + "]");
						Constants.WEIXIN_LAST_PAY_COMPANY_TIME = cacheCurCompany.toString();
					}
					String curCompanyCode = "";
					if ((StringUtils.isNotBlank(curCompany[0]) && conf1.getKeyvalue().indexOf(curCompany[0]) == -1)
							|| Constants.WEIXIN_LAST_PAY_COMPANY_TIME.startsWith("_")
							|| StringUtils.isBlank(curCompanyCode)) {
						playpay = companys[0];
						Constants.WEIXIN_LAST_PAY_COMPANY_TIME = playpay + "_" + System.currentTimeMillis() + "_1";
						MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
					} else {
						curCompanyCode = curCompany[0].substring(1) + "_WXF";
						long curLong = System.currentTimeMillis();
						int curCount = Integer.valueOf(curCompany[2]);// 充值成功笔数，缓存的
						if ((curLong - Long.valueOf(curCompany[1]) > 5 * 60 * 1000) || curCount + 1 > count) {
							Map<String, String> result = payService
									.findLastPayCompanyCount(new Date(Long.valueOf(curCompany[1])), curCompanyCode);
							String val = result.get(curCompanyCode);
							if (StringUtils.isBlank(val)) {
								val = "1";
							}
							log.error("recharge-play微信账号切换-超时并且超过预定笔数,计数器笔数[" + curCount + "],实际笔数[" + val + "]");
							if (Integer.valueOf(val) < count) {
								playpay = curCompany[0];
								Constants.WEIXIN_LAST_PAY_COMPANY_TIME = playpay + "_" + curCompany[1] + "_" + val;
								MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
							} else {
								int pos = 0;
								for (String tmpString : companys) {
									if (tmpString.equals(curCompany[0])) {
										break;
									}
									pos++;
								}
								if (pos + 1 >= companys.length) {
									playpay = companys[0];
								} else {
									playpay = companys[pos + 1];
								}
								log.error("recharge-play微信账号切换-超时并且超过预定笔数，切换账号[" + playpay + "],pos[" + pos + 1 + "]");
								Constants.WEIXIN_LAST_PAY_COMPANY_TIME = playpay + "_" + System.currentTimeMillis()
										+ "_1";
								MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
							}
						} else {
							playpay = curCompany[0];
							Constants.WEIXIN_LAST_PAY_COMPANY_TIME = playpay + "_" + curCompany[1] + "_"
									+ (Integer.valueOf(curCompany[2]).intValue() + 1);
							MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
							log.error("recharge-play微信账号切换-当前未失效计数器[" + Constants.WEIXIN_LAST_PAY_COMPANY_TIME + "]");
						}

					}
				} else {
					return "1033#" + Constants.parametermap.get("1033");
				}

			} else if ("2".equals(channel)) {// 支付宝充值
				memcachKey = "alipay.compay.channel_" + memcachKey;
				TSysConfig conf1 = sysService.findByKey("alipay.compay.channel");
				if (conf1 != null && StringUtils.isNotBlank(conf1.getKeyvalue())) {
					log.error("recharge-play支付宝账号-当前配置账号[" + conf1.getKeyvalue() + "]");
					String[] companys = StringUtils.splitPreserveAllTokens(conf1.getKeyvalue(), ",");
					// 从缓存中获取对应的数据。用于多台接口服务器
					String[] curCompany = null;
					Object cacheCurCompany = MemcacheUtil.get(memcachKey);
					if (cacheCurCompany == null) {
						curCompany = StringUtils.splitPreserveAllTokens(Constants.ALIPAY_LAST_PAY_COMAPANY_TIME, "_");
						log.error("recharge-play支付宝账号切换-当前本站账号[" + Constants.ALIPAY_LAST_PAY_COMAPANY_TIME + "]");
					} else {
						curCompany = StringUtils.splitPreserveAllTokens(cacheCurCompany.toString(), "_");
						log.error("recharge-play支付宝账号切换-当前缓存账号[" + cacheCurCompany + "]");
						Constants.ALIPAY_LAST_PAY_COMAPANY_TIME = cacheCurCompany.toString();
					}
					String curCompanyCode = "";
					if ((StringUtils.isNotBlank(curCompany[0]) && conf1.getKeyvalue().indexOf(curCompany[0]) == -1)
							|| Constants.ALIPAY_LAST_PAY_COMAPANY_TIME.startsWith("_")
							|| StringUtils.isBlank(curCompanyCode)) {
						playpay = companys[0];
						Constants.ALIPAY_LAST_PAY_COMAPANY_TIME = playpay + "_" + System.currentTimeMillis() + "_1";
						MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
					} else {
						curCompanyCode  = curCompany[0].substring(1) + "_ZFB";
						long curLong = System.currentTimeMillis();
						int curCount = Integer.valueOf(curCompany[2]);// 充值成功笔数，缓存的
						if ((curLong - Long.valueOf(curCompany[1]) > 5 * 60 * 1000) || curCount + 1 > count) {
							Map<String, String> result = payService
									.findLastPayCompanyCount(new Date(Long.valueOf(curCompany[1])), curCompanyCode);
							String val = result.get(curCompanyCode);
							if (StringUtils.isBlank(val)) {
								val = "1";
							}
							log.error("recharge-play支付宝账号切换-超时并且超过预定笔数,计数器笔数[" + curCount + "],实际笔数[" + val + "]");
							if (Integer.valueOf(val) < count) {
								playpay = curCompany[0];
								Constants.ALIPAY_LAST_PAY_COMAPANY_TIME = playpay + "_" + curCompany[1] + "_" + val;
								MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
							} else {
								int pos = 0;
								for (String tmpString : companys) {
									if (tmpString.equals(curCompany[0])) {
										break;
									}
									pos++;
								}
								if (pos + 1 >= companys.length) {
									playpay = companys[0];
								} else {
									playpay = companys[pos + 1];
								}
								log.error("recharge-play支付宝账号切换-超时并且超过预定笔数，切换账号[" + playpay + "],pos[" + pos + 1 + "]");
								Constants.ALIPAY_LAST_PAY_COMAPANY_TIME = playpay + "_" + System.currentTimeMillis()
										+ "_1";
								MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
							}
						} else {
							playpay = curCompany[0];
							Constants.ALIPAY_LAST_PAY_COMAPANY_TIME = playpay + "_" + curCompany[1] + "_"
									+ (Integer.valueOf(curCompany[2]).intValue() + 1);
							MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
							log.error("recharge-play支付宝切换-当前未失效计数器[" + Constants.ALIPAY_LAST_PAY_COMAPANY_TIME + "]");
						}
					}
				} else {
					return "1033#" + Constants.getParamterkey("1033");
				}
			}
			log.error("recharge-play:" + playpay);
			if (StringUtils.isBlank(playpay)) {
				return "1035#" + Constants.getParamterkey("1035");
			}
			return "0#" + playpay;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "1035#" + Constants.getParamterkey("1035");
		}
	}
	
	private TPayRecord change(PayVo payvo) {
		TPayRecord record = new TPayRecord();
		record.setChannel(payvo.getPaychannel());
		record.setMemno(payvo.getMemno());
		record.setMoney(payvo.getMoney());
		record.setOrderno(payvo.getOrderno());
		record.setPaystr("");
		record.setNoticetimes(0);
		record.setPaytime(payvo.getCreatetime());
		record.setRquestid(payvo.getRequestip());
		record.setNoticeurl(payvo.getNoticeurl());
		return record;
	}
}
