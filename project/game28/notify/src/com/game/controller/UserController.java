package com.game.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.entity.TSysConfig;
import com.game.pojo.MembaseVo;
import com.game.service.SysService;
import com.game.service.UserService2;
import com.game.utils.Constants;
import com.game.utils.MemcacheUtil;
import com.game.utils.Posturl;
import com.game.utils.PropertiesUtil;
import com.game.utils.QRCodeUtil;
import com.game.utils.StringUtil;
import com.game.utils.TradUtil;
import com.game.utils.common.BaseAction;
import com.game.utils.encription.Base64Util;
import com.game.utils.rl.SignUtil;
import com.game.utils.wanrong.SignUtils;
import com.game.utils.wanrong.WYPayUtil;
import com.game.utils.wanrong.weixin.RequestSign;
import com.game.utils.wanrong.weixin.Weixinwanrong;
import com.game.utils.wx.Card70Util;

@Controller
@RequestMapping("/interface/user")
public class UserController  extends BaseAction{
 
	@Autowired
	UserService2 userService;
	
 
	@Autowired
	SysService sysService;
	 
	/**
	 * @param request
	 * @param response
	 * @return
	 * 用户充值 directrecharge
	 */
	@RequestMapping("/recharge")
	@ResponseBody
	public Object recharge(HttpServletRequest request,HttpServletResponse response){
		//通过解析request 获取参数 data,partnerid,service,time,version,key
		final	Map<String,String> requestdata = super.parseRequest(request);
		Map<String,Object> resultmap = super.SUCESS();
		resultmap.put("partnerid",requestdata.get("partnerid"));
		//验证参数是否合法。 0#cefp 
		String resultstr = requestdata.get("status");
		//0#开头的为成功标识可以进行后续业务逻辑处理了。
		try {
			if(resultstr.startsWith("0#")){
				//把传入参数 转化成一个java对象。后续处理。
				MembaseVo base = JSON.parseObject(requestdata.get("data"), MembaseVo.class);
				base.setMd5str(requestdata.get("md5str"));
				if((StringUtils.isBlank(base.getToken()) || base.getToken().length()!=32)){
					resultmap.put("status","9009");
					resultmap.put("msg",Constants.getParamterkey("9009"));
					return callback2(requestdata.get("callback"), resultmap, request, response);
				}
				String userid = super.getUserid(base.getToken());
				if(StringUtils.isBlank(userid)){
					resultmap.put("status","9006");
					resultmap.put("msg",Constants.getParamterkey("9006"));
					return callback2(requestdata.get("callback"), resultmap, request, response);
				}
				base.setUserid(userid);
				if(StringUtils.isBlank(base.getType())){
					resultmap.put("status","9002");
					resultmap.put("msg","type"+Constants.getParamterkey("9002"));
					return callback2(requestdata.get("callback"), resultmap, request, response);
				}
				if(!base.getType().matches("^\\d$")){//1 微信，2支付宝
					resultmap.put("status","9009");
					resultmap.put("msg","type"+Constants.getParamterkey("9009"));
					return callback2(requestdata.get("callback"), resultmap, request, response);
				}
				TSysConfig config2 = sysService.findByKey("pay.support");
				if(config2!=null && StringUtils.isNotBlank(config2.getKeyvalue())){
					String[] paysuports = StringUtils.splitPreserveAllTokens(config2.getKeyvalue(),",");
					boolean flag = true;
					for(String s : paysuports){
						if((base.getType()+":1").equals(s)){
							flag = false;
							break;
						}
					}
					if(flag){
						resultmap.put("status","1033");
						resultmap.put("msg",Constants.getParamterkey("1033"));
						return callback2(requestdata.get("callback"), resultmap, request, response);
					}
				}
				if(base.getMoney()==null){
					resultmap.put("status","9002");
					resultmap.put("msg","money"+Constants.getParamterkey("9002"));
					return callback2(requestdata.get("callback"), resultmap, request, response);
				} 
				if( base.getMoney().compareTo(BigDecimal.valueOf(Constants.RECHARGE_LIMIT))<0){
					resultmap.put("status","1032");
					resultmap.put("msg",Constants.getParamterkey("1032")+"["+Constants.RECHARGE_LIMIT+"]");
					return callback2(requestdata.get("callback"), resultmap, request, response);
				}
				BigDecimal max = BigDecimal.valueOf(3000);
				TSysConfig config = sysService.findByKey("recharge.max.value");
				if(config!=null && StringUtils.isNotBlank(config.getKeyvalue())){
					max = new BigDecimal(config.getKeyvalue());
				}
				int payRandomFlag = 0 ;
				config = sysService.findByKey("pay.random.flag");
				if(config!=null && StringUtils.isNotBlank(config.getKeyvalue())){
					payRandomFlag = Integer.valueOf(config.getKeyvalue()).intValue();
				}
				log.error("[recharge],pay.random.flag=>"+payRandomFlag+",config.value=>"+config.getKeyvalue());
				if(base.getMoney().compareTo(max)>0){
					resultmap.put("status","1031");
					resultmap.put("msg",StringUtils.replaceOnce(Constants.getParamterkey("1031"),"X",config.getKeyvalue()));
					return callback2(requestdata.get("callback"), resultmap, request, response);
				}
				
				String playpay = "";
				int count = Integer.valueOf(PropertiesUtil.getValue("pay.change.company"));//每个充值账号，每次充值成功多少笔以后，切换充值账户.
				
				/**
				 * 通过 关键值 alipay.compay.channel 控制支付宝,充值账号
				 * weixin.compay.channel 控制微信，充值账号
				 * 每隔5min去获取数据，优先达到30笔的，轮换账号
				 * **/
				String memcachKey= PropertiesUtil.getValue("wr.jf.notifyurl");
				 if("1".equals(base.getType())){//微信充值
					 memcachKey = "weixin.compay.channel_"+ memcachKey;
					 TSysConfig conf1 = sysService.findByKey("weixin.compay.channel");
					 if(conf1!=null && StringUtils.isNotBlank(conf1.getKeyvalue())){
						 String[] companys = StringUtils.splitPreserveAllTokens(conf1.getKeyvalue(),",");
						log.error("recharge-play微信账号-当前配置账号["+conf1.getKeyvalue()+"]");
						 //从缓存中获取对应的数据。用于多台接口服务器
						 String[] curCompany = null;
						 Object cacheCurCompany = MemcacheUtil.get(memcachKey) ;
						 if(cacheCurCompany == null){
							 curCompany = StringUtils.splitPreserveAllTokens(Constants.WEIXIN_LAST_PAY_COMPANY_TIME,"_");
							log.error("recharge-play微信账号切换-当前本站账号["+Constants.WEIXIN_LAST_PAY_COMPANY_TIME+"]");
						 }else{
							 curCompany = StringUtils.splitPreserveAllTokens(cacheCurCompany.toString(),"_");
							 log.error("recharge-play微信账号切换-当前缓存账号["+cacheCurCompany+"]");
							 Constants.WEIXIN_LAST_PAY_COMPANY_TIME = cacheCurCompany.toString();
						 }
						 String curCompanyCode = Constants.PAY_COMPANY_NAME.get(curCompany[0]);
						 if(StringUtils.isBlank(curCompanyCode)){
							 curCompanyCode = curCompany[0].substring(1)+"_WXF";
						 }
						 if((StringUtils.isNotBlank(curCompany[0]) && conf1.getKeyvalue().indexOf(curCompany[0])==-1) || Constants.WEIXIN_LAST_PAY_COMPANY_TIME.startsWith("_") || StringUtils.isBlank(curCompanyCode)){
							 playpay = companys[0];
							 Constants.WEIXIN_LAST_PAY_COMPANY_TIME = playpay+"_"+System.currentTimeMillis()+"_1";
							 MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
						 }else{
							 long curLong = System.currentTimeMillis();
							 int curCount  = Integer.valueOf(curCompany[2]);//充值成功笔数，缓存的
							 if((curLong - Long.valueOf(curCompany[1]) > 5 * 60 *1000) || curCount+1 > count){
								 Map<String, String> result = userService.findLastPayCompanyCount(new Date(Long.valueOf(curCompany[1])),curCompanyCode);
								 String val = result.get(curCompanyCode);
								 if(StringUtils.isBlank(val)){
									 val = "1";
								 }
								 log.error("recharge-play微信账号切换-超时并且超过预定笔数,计数器笔数["+curCount+"],实际笔数["+val+"]");
								 if(Integer.valueOf(val) < count){
									 playpay = curCompany[0];
									 Constants.WEIXIN_LAST_PAY_COMPANY_TIME=playpay+"_"+curCompany[1]+"_"+val;
									 MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
								 }else{
									 int pos = 0 ;
									 for(String tmpString  : companys){
										 if(tmpString.equals(curCompany[0])){
											 break;
										 }
										 pos++;
									 }
									 if(pos+1>=companys.length){
										 playpay = companys[0];
									 }else{
										 playpay = companys[pos+1];
									 }
									 log.error("recharge-play微信账号切换-超时并且超过预定笔数，切换账号["+playpay+"],pos["+pos+1+"]");
									 Constants.WEIXIN_LAST_PAY_COMPANY_TIME=playpay+"_"+System.currentTimeMillis()+"_1";
									 MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
								 }
							 }else{
								 playpay = curCompany[0];
								 Constants.WEIXIN_LAST_PAY_COMPANY_TIME=playpay+"_"+curCompany[1]+"_"+(Integer.valueOf(curCompany[2]).intValue()+1);
								 MemcacheUtil.put(memcachKey, Constants.WEIXIN_LAST_PAY_COMPANY_TIME);
								 log.error("recharge-play微信账号切换-当前未失效计数器["+Constants.WEIXIN_LAST_PAY_COMPANY_TIME+"]");
							 }
							
						 }
					 }else{
						    resultmap.put("status","1033");
							resultmap.put("msg",Constants.getParamterkey("1033"));
							return callback2(requestdata.get("callback"), resultmap, request, response);
					 }
					 
				 } else if("2".equals(base.getType())){//支付宝充值
					 memcachKey = "alipay.compay.channel_"+memcachKey;
					 TSysConfig conf1 = sysService.findByKey("alipay.compay.channel");
					 if(conf1!=null && StringUtils.isNotBlank(conf1.getKeyvalue())){
						// if(log.isInfoEnabled()){
							 log.error("recharge-play支付宝账号-当前配置账号["+conf1.getKeyvalue()+"]");
						// }
						 String[] companys = StringUtils.splitPreserveAllTokens(conf1.getKeyvalue(),",");
						 //从缓存中获取对应的数据。用于多台接口服务器
						 String[] curCompany = null;
						 Object cacheCurCompany = MemcacheUtil.get(memcachKey) ;
						 if(cacheCurCompany == null){
							 curCompany = StringUtils.splitPreserveAllTokens(Constants.ALIPAY_LAST_PAY_COMAPANY_TIME,"_");
							// if(log.isInfoEnabled()){
								 log.error("recharge-play支付宝账号切换-当前本站账号["+Constants.ALIPAY_LAST_PAY_COMAPANY_TIME+"]");
							// }
						 }else{
							 curCompany = StringUtils.splitPreserveAllTokens(cacheCurCompany.toString(),"_");
							// if(log.isInfoEnabled()){
								 log.error("recharge-play支付宝账号切换-当前缓存账号["+cacheCurCompany+"]");
							// }
							 Constants.ALIPAY_LAST_PAY_COMAPANY_TIME = cacheCurCompany.toString();
						 }
						 String curCompanyCode = Constants.PAY_COMPANY_NAME.get(curCompany[0]);
						 if(StringUtils.isBlank(curCompanyCode)){
							 curCompanyCode = curCompany[0].substring(1)+"_ZFB";
						 }
						 
						 if((StringUtils.isNotBlank(curCompany[0]) && conf1.getKeyvalue().indexOf(curCompany[0])==-1) || Constants.ALIPAY_LAST_PAY_COMAPANY_TIME.startsWith("_") || StringUtils.isBlank(curCompanyCode)){
							 playpay = companys[0];
							 Constants.ALIPAY_LAST_PAY_COMAPANY_TIME = playpay+"_"+System.currentTimeMillis()+"_1";
							 MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
						 }else{
							 long curLong = System.currentTimeMillis();
							 int curCount  = Integer.valueOf(curCompany[2]);//充值成功笔数，缓存的
							 if((curLong - Long.valueOf(curCompany[1]) > 5 * 60 *1000) || curCount+1 > count){
								 Map<String, String> result = userService.findLastPayCompanyCount(new Date(Long.valueOf(curCompany[1])),curCompanyCode);
								 String val = result.get(curCompanyCode);
								 if(StringUtils.isBlank(val)){
									 val = "1";
								 }
								// if(log.isInfoEnabled()){
									 log.error("recharge-play支付宝账号切换-超时并且超过预定笔数,计数器笔数["+curCount+"],实际笔数["+val+"]");
								// }
								 if(Integer.valueOf(val) < count){
									 playpay = curCompany[0];
									 Constants.ALIPAY_LAST_PAY_COMAPANY_TIME=playpay+"_"+curCompany[1]+"_"+val;
									 MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
								 }else{
									 int pos = 0 ;
									 for(String tmpString  : companys){
										 if(tmpString.equals(curCompany[0])){
											 break;
										 }
										 pos++;
									 }
									 if(pos+1>=companys.length){
										 playpay = companys[0];
									 }else{
										 playpay = companys[pos+1];
									 }
									// if(log.isInfoEnabled()){
										 log.error("recharge-play支付宝账号切换-超时并且超过预定笔数，切换账号["+playpay+"],pos["+pos+1+"]");
									// }
									 Constants.ALIPAY_LAST_PAY_COMAPANY_TIME=playpay+"_"+System.currentTimeMillis()+"_1";
									 MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
								 }
							 }else{
								 playpay = curCompany[0];
								 Constants.ALIPAY_LAST_PAY_COMAPANY_TIME=playpay+"_"+curCompany[1]+"_"+(Integer.valueOf(curCompany[2]).intValue()+1);
								 MemcacheUtil.put(memcachKey, Constants.ALIPAY_LAST_PAY_COMAPANY_TIME);
								// if(log.isInfoEnabled()){
									 log.error("recharge-play支付宝切换-当前未失效计数器["+Constants.ALIPAY_LAST_PAY_COMAPANY_TIME+"]");
								// }
							 }
							
						 }
					 }else{
						    resultmap.put("status","1033");
							resultmap.put("msg",Constants.getParamterkey("1033"));
							return callback2(requestdata.get("callback"), resultmap, request, response);
					 }
					 
				 }
					log.error("recharge-play:"+playpay);
					if(StringUtils.isBlank(playpay)){
						resultmap.put("status","1035");
						resultmap.put("msg",Constants.getParamterkey("1035"));
						return callback2(requestdata.get("callback"), resultmap, request, response);
					}
					 
					int day = new Date().getDate();
					int c = 1 ;
					while (c <= 2 ){//重试三次
						try{
							String tradeno = TradUtil.getTradingNo("C");
							Map<String,Object> data = null;
							if(playpay.startsWith("微") && playpay.length() == 5){
								log.error("--recharege--playpay-->>"+playpay);
								String prefx = playpay.substring(1);
								String productid = "0119";//支付宝扫码
								base.setChannel(prefx+"_ZFB");
								if("1".equals(base.getType())){
									productid ="0108";//微信扫码
									base.setChannel(prefx+"_WXF");
								}
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
									data = requestWXWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"2");							
								}else{ 
									//连接浦发
									data = requestWXWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"0");
								} 
							}else if(playpay.startsWith("支") && playpay.length() == 5){
								log.error("--recharege--playpay-->>"+playpay);
								String prefx = playpay.substring(1);
								String productid = "0119";//支付宝扫码
								base.setChannel(prefx+"_ZFB");
								if("1".equals(base.getType())){
									productid ="0108";//微信扫码
									base.setChannel(prefx+"_WXF");
								}
								if(payRandomFlag == 1 || (payRandomFlag == 2 && day%2 == 0 ) ){
									String merNo = 	PropertiesUtil.getValue("wr."+prefx+".merNo");
									String returnurl=PropertiesUtil.getValue("wr."+prefx+".returnurl");
									String notifyurl = PropertiesUtil.getValue("wr."+prefx+".notifyurl");
									String privateKeyPath = PropertiesUtil.getValue("wr."+prefx+".private_key_path");
									String publicKeyPath = PropertiesUtil.getValue("wr."+prefx+".public_key_path");
									String posturl =  PropertiesUtil.getValue("wr."+prefx+".url");
									String subMchId =PropertiesUtil.getValue("wr."+prefx+".ali.subMchId");
									data =  requestWR(posturl,merNo,subMchId,returnurl,notifyurl,privateKeyPath,publicKeyPath,base.getMoney().toPlainString(), tradeno, productid, "10", c);
								}else if(payRandomFlag == 3){
									String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
									String posturl = PropertiesUtil.getValue("wr.wx.payurl");
									String usercode = PropertiesUtil.getValue("wr."+prefx+".usercode");
									data = requestZFWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"2");
								}else{
									String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
									String posturl = PropertiesUtil.getValue("wr.wx.payurl");
									String usercode = PropertiesUtil.getValue("wr."+prefx+".usercode");
									data = requestZFWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"0");
								}
							}else if("微信聚富万融".equals(playpay)){
								String productid = "0119";//支付宝扫码
								base.setChannel("jfwr_ZFB");
								if("1".equals(base.getType())){
									productid ="0108";//微信扫码
									base.setChannel("jfwr_WXF");
								}
//								if(payRandomFlag == 1 || (payRandomFlag == 2 && day%2 == 0 ) ){
//									String merNo = 	PropertiesUtil.getValue("wr.jf.merNo");
//									String returnurl=PropertiesUtil.getValue("wr.jf.returnurl");
//									String notifyurl = PropertiesUtil.getValue("wr.jf.notifyurl");
//									String privateKeyPath = PropertiesUtil.getValue("wr.jf.private_key_path");
//									String publicKeyPath = PropertiesUtil.getValue("wr.jf.public_key_path");
//									String posturl =  PropertiesUtil.getValue("wr.jf.url");
//									String subMchId =PropertiesUtil.getValue("wr.jf.wx.subMchId");
//									data =  requestWR(posturl,merNo,subMchId,returnurl,notifyurl,privateKeyPath,publicKeyPath,base.getMoney().toPlainString(), tradeno, productid, "10", c);
//								}else{
									if(payRandomFlag == 3){
										String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
										String posturl = PropertiesUtil.getValue("wr.wx.payurl");
										String usercode = PropertiesUtil.getValue("wr.jf.usercode");
										data = requestWXWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"1");
									}else{
										String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
										String posturl = PropertiesUtil.getValue("wr.wx.payurl");
										String usercode = PropertiesUtil.getValue("wr.jf.usercode");
										data = requestWXWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"0");
									}
							}else if("支付宝聚富万融".equals(playpay)){
								String productid = "0119";//支付宝扫码
								base.setChannel("jfwr_ZFB");
								if("1".equals(base.getType())){
									productid ="0108";//微信扫码
									base.setChannel("jfwr_WXF");
								}
								if(payRandomFlag == 1 || (payRandomFlag == 2 && day%2 == 0 ) ){
									String merNo = 	PropertiesUtil.getValue("wr.jf.merNo");
									String returnurl=PropertiesUtil.getValue("wr.jf.returnurl");
									String notifyurl = PropertiesUtil.getValue("wr.jf.notifyurl");
									String privateKeyPath = PropertiesUtil.getValue("wr.jf.private_key_path");
									String publicKeyPath = PropertiesUtil.getValue("wr.jf.public_key_path");
									String posturl =  PropertiesUtil.getValue("wr.jf.url");
									String subMchId =PropertiesUtil.getValue("wr.jf.ali.subMchId");
									 data =  requestWR(posturl,merNo,subMchId,returnurl,notifyurl,privateKeyPath,publicKeyPath,base.getMoney().toPlainString(), tradeno, productid, "10", c);
								}else if(payRandomFlag ==3){
									String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
									String posturl = PropertiesUtil.getValue("wr.wx.payurl");
									String usercode = PropertiesUtil.getValue("wr.jf.usercode");
									data = requestZFWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"1");
								}else{
									String notifyurl = PropertiesUtil.getValue("wr.wx.notifyurl");
									String posturl = PropertiesUtil.getValue("wr.wx.payurl");
									String usercode = PropertiesUtil.getValue("wr.jf.usercode");
									data = requestZFWR(posturl,tradeno,String.valueOf(base.getMoney().multiply(BigDecimal.valueOf(100)).intValue()),usercode,notifyurl,"0");
								}
							}else{
								resultmap.put("status","1035");
								resultmap.put("msg",Constants.getParamterkey("1035"));
								return callback2(requestdata.get("callback"), resultmap, request, response);
							}
							c++;
							if(data == null || data.isEmpty()){
								log.error("recharge["+playpay+"]业务不支持");
								continue;
							}
							log.error("recharge-->"+JSON.toJSON(data));
							if(data.get("ok")!=null && "9999".equals(data.get("ok").toString())){
								resultmap.put("status","-12");
								resultmap.put("msg","此业务上游不支持");
								return callback2(requestdata.get("callback"), resultmap, request, response);
							}else{
								data.put("m",base.getMoney().toPlainString());
								data.put("tradeno",tradeno);
								if(log.isInfoEnabled()){
									log.info("[recharge]->"+data);
								}
								base.setBankno(tradeno);
								base = userService.createRecharge(base);
								if("0".equals(base.getErrorcode())){
									resultmap.put("data",JSON.toJSON(data));
									return resultmap;
								} 
								resultmap.put("status","-11");
								resultmap.put("msg","网络交互异常，未握手通信");
								resultmap.put("data",JSON.toJSON(new HashMap<String,Object>()));
								return resultmap;
								 
							}
						}catch(Exception e){
							log.error("recharge["+playpay+"]充值请求失败->"+e.getMessage(),e);
							c++;
						}
						if(c>=3){
							resultmap.put("status","-11");
							resultmap.put("msg","网络交互异常，未握手通信");
							resultmap.put("data",JSON.toJSON(new HashMap<String,Object>()));
							return resultmap;
						}
					}
					resultmap.put("status","-11");
					resultmap.put("msg","网络交互异常，未握手通信");
					resultmap.put("data",JSON.toJSON(new HashMap<String,Object>()));
					return resultmap;
			}else{
				String[] str = StringUtils.split(resultstr,"#");
				resultmap.put("status",str[0]);
				resultmap.put("msg",str[1]);
			}
		} catch (Exception e) {
			 log.error(e.getMessage(),e);
			 resultmap.put("status","9999");
			 resultmap.put("msg",Constants.getParamterkey("9999"));
		}
		return callback2(requestdata.get("callback"), resultmap, request, response);
	}
	 
	private Map<String,Object> requestCard70(String money,String tradeno,String code,String ext,int count) throws Exception{
		String msg = Card70Util.buildUrl(money,tradeno,code,ext);
		Map<String,Object> data  = Card70Util.parseRequest(msg);
		return data;
	}
	private Map<String,Object> requestBRWR(String posturl,String orgNo,String merNo,String returnurl,String notifyurl,String privateKeyPath,String publicKeyPath,String money,String tradeno,String productid,String tranid,int count){
		Map<String,Object> d = new HashMap<String,Object>();
		try{
		Map<String,Object> data = WYPayUtil.createRL(orgNo,merNo,returnurl,notifyurl,privateKeyPath,new BigDecimal (money), tradeno, productid, tranid);
		log.error("recharge->请求参数["+JSON.toJSONString(data)+"]");
		String result = WYPayUtil.postData(data,posturl);
		data = null;
		if(log.isInfoEnabled()){
			log.info("recharge->返回数据["+result+"]");
		}
		if(StringUtils.isBlank(result)){
			log.error("recharge->[润联未返回数据]");
			return null;
		}
		if(result.length()<50){
			log.error("recharge->[润联返回异常数据]"+result);
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(result);
		TreeMap<String, String> treeMap = new TreeMap<>();
		for (Entry<String, Object> entry : jsonObject.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().toString();
			treeMap.put(key, value);
		}
		//{"codeUrl":"weixin://wxpay/bizpayurl?pr=ysSyCr3","commodityName":"支付C170412180002392480","nofityUrl":"http://admin.xyungame.net/notify/bsms","orderDate":"20170412","orderNo":"C170412180002392480","productId":"0104","requestNo":"RQ170412180002263566","respCode":"00","respDesc":"交易成功","signature":"UVxCDiBp2TUH61Y1ZUDAvxOJ7KdnIZ9ZxjqlGU3H7wYR1ob1rJsJU2BhlasO91gIBUjKS621Gn7TdJcMDg6Zfn27Wcy1AWXlfAIiC0G/8Klc7s3fb6Mwozs479nzuw/ijoZbaOx4ArKpj7ht7dTZ7nSKgPa6hEI7qJnweYUGX0SXnVJycwnir8/uEKspkrw3A9aqhUbWFOzF4sy2UBIfss4OIQpdzcqC3hUwhpUAhG6uB29BPCb5GPUGz+XyJ+wKx8PvIIYxzWMupZfCfuNLco83xySyNQeklZ4wA3dw1YXto5lOtgud3Kkua5fAHvH6JuPCGHwYkUu0zZAb06fryg==","subMerNo":"800010680010034","transAmt":"1","transId":"10","version":"V2.0"
		if(SignUtil.verferSignData(treeMap, publicKeyPath)){
			if(treeMap !=null && ("00".equals(treeMap.get("respCode").toString()))){
				d.put("orderid", treeMap.get("orderNo"));
				d.put("orderno", treeMap.get("orderNo"));
				String codeurl = treeMap.get("codeUrl").toString();
				String imgurl = "";
				String file = Constants.getConfigkey("common.file.path");
				String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("upload.image.base64.domain"),
						file,StringUtil.mkdirect("/qrcode"),
						codeurl, file+"/logo.jpg",StringUtil.mkfilename("jpg"));
				log.error("recharge->img["+path_+"],codeurl["+codeurl+"]");
				d.put("img",path_);
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
				d.put("ok", "9999");
				log.error("recharge->[润联返回数据]"+result);
			}
		}else{
			log.error("recharge-->校验未通过["+result+"]");
		}
		if(log.isInfoEnabled()){
			log.info("recharge->["+d+"]");
		}
		}catch(Exception e){
			log.error("【万融博时-润联充值异常】"+e.getMessage(),e);
		}
		return d;		 
	}
	private Map<String,Object> requestZFWR(String posturl,String tradeno,String amount,String usercode,String notifyurl,String channelType){
		 Map<String,Object> d = new HashMap<String,Object>();
		 
		 try{
			 Map<String,Object> param   = Weixinwanrong.buildParam(tradeno,amount,usercode,notifyurl,channelType);
			 String sign = RequestSign.createSignStr(JSONObject.parseObject(JSON.toJSONString(param)), "sign");
			 param.put("sign",sign);
			 log.error("recharge-zfb.postdata->"+JSON.toJSONString(param));
			 String result = Posturl.postRequestJson(posturl, param, "", "utf-8");
			 if(StringUtils.isNotBlank(result)){
				 log.error("recharge-zfb-result->["+result+"]");
				Map<String,Object> map = JSONObject.parseObject(result);
				String code = map.get("code").toString();
				String success = map.get("success").toString();
				if("0000".equals(code) && success.equals("true")){
					String signs = map.get("sign").toString();
					JSONObject obj =(JSONObject) map.get("data");
					if(RequestSign.checkSign(obj.toJSONString(),signs, "sign")){
						String orderId = obj.getString("orderId") ;
						String payurl =  obj.getString("payUrl");
						String payImg =  obj.getString("payImg");
						String mount = obj.getString("totalAmount");
						String keyCode = obj.getString("keyCode");
						d.put("orderid", orderId);
						d.put("orderno", orderId);
						String file = Constants.getConfigkey("common.file.path");
						String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("upload.image.base64.domain"),
								file,StringUtil.mkdirect("/qrcode"),
								payurl, file+"/logo.jpg",StringUtil.mkfilename("jpg"));
						log.error("recharge->img["+path_+"],keyCode=["+keyCode+"],payurl["+payurl+"],payimg["+payImg+"],mount["+mount+"]分");
						d.put("img",path_);
						d.put("imgurl", payurl);
						d.put("returncode", "1");
					}else{
						d.put("ok", "9999");
						log.error("recharge-result->"+result);
					}
				}else{
					log.error("recharge-result->"+result);
					d.put("ok", "9999");
				}
			}else{
				log.error("recharge-->没有获取返回数据");
			}
		 }catch(Exception e){
			 log.error("recharge->"+e.getMessage(),e);
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
			 String result = Posturl.postRequestJson(posturl, param, "", "utf-8");
			 if(StringUtils.isNotBlank(result)){
				 log.error("recharge-weixin-result->["+result+"]");
				Map<String,Object> map = JSONObject.parseObject(result);
				String code = map.get("code").toString();
				String success = map.get("success").toString();
				if("0000".equals(code) && success.equals("true")){
					String signs = map.get("sign").toString();
					JSONObject obj =(JSONObject) map.get("data");
					if(RequestSign.checkSign(obj.toJSONString(),signs, "sign")){
						String orderId = obj.getString("orderId") ;
						String payurl =  obj.getString("payUrl");
						String payImg =  obj.getString("payImg");
						String mount = obj.getString("totalAmount");
						String keyCode = obj.getString("keyCode");
						d.put("orderid", orderId);
						d.put("orderno", orderId);
						String file = Constants.getConfigkey("common.file.path");
						String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("upload.image.base64.domain"),
								file,StringUtil.mkdirect("/qrcode"),
								payurl, file+"/logo.jpg",StringUtil.mkfilename("jpg"));
						log.error("recharge->img["+path_+"],keyCode=["+keyCode+"],payurl["+payurl+"],payimg["+payImg+"],mount["+mount+"]分");
						d.put("img",path_);
						d.put("imgurl", payurl);
						d.put("returncode", "1");
					}else{
						d.put("ok", "9999");
						log.error("recharge-result->"+result);
					}
				}else{
					 log.error("recharge-result->"+result);
					d.put("ok", "9999");
				}
			}else{
				log.error("recharge-->没有获取返回数据");
			}
		 }catch(Exception e){
			 log.error("recharge->"+e.getMessage(),e);
		 }
		return d;
	}
	private Map<String,Object> requestWR(String posturl,String merNo,String subMchId,String returnurl,String notifyurl,String privateKeyPath,String publicKeyPath,String money,String tradeno,String productid,String tranid,int count) throws Exception{
		Map<String,Object> d = new HashMap<String,Object>();
		try{
		Map<String,Object> data = WYPayUtil.createWeixin(merNo,subMchId,returnurl,notifyurl,privateKeyPath,new BigDecimal (money), tradeno, productid, tranid);
		log.error("recharge-postdata->"+JSON.toJSONString(data));
		String result = WYPayUtil.postData(data,posturl);
		if(StringUtils.isBlank(result)) return null;
		if(SignUtils.verferSignData(result,publicKeyPath)){
			data = WYPayUtil.parseString(result);
			if(log.isInfoEnabled()){
				log.info("recharge->返回数据["+data+"]");
			}
		}else{
			log.error("recharge-->校验未通过["+result+"]");
		}
		if(data !=null && "0000".equals(data.get("respCode").toString())){
			d.put("orderid", data.get("orderNo"));
			d.put("orderno", data.get("orderNo"));
			String codeurl = data.get("codeUrl").toString();
			codeurl = new String(Base64Util.decode(codeurl),"UTF-8");
			String file = Constants.getConfigkey("common.file.path");
			String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("upload.image.base64.domain"),
					file,StringUtil.mkdirect("/qrcode"),
					codeurl, file+"/logo.jpg",StringUtil.mkfilename("jpg"));
			log.error("recharge->img["+path_+"]merNO=["+merNo+"],codeurl["+codeurl+"]");
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
		}else{
			log.error("recharge--result["+result+"]");
			d.put("ok", "9999");
		}
		if(log.isInfoEnabled()){
			log.info("recharge->["+d+"]");
		}
		}catch(Exception e){
			log.error("recharge【万融在线充值异常】merNo="+merNo+e.getMessage(),e);
		}
		return d;		 
	}
}
