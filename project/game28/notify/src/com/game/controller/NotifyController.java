package com.game.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.game.pojo.TMemMoneyRecordVo;
import com.game.utils.PropertiesUtil;
import com.game.utils.StringUtil;
import com.game.utils.XMLUtil;
import com.game.utils.common.BaseAction;
import com.game.utils.wanrong.SignUtils;
import com.game.utils.wanrong.weixin.RequestSign;

@Controller("/notify")
public class NotifyController extends BaseAction{


	@RequestMapping("/weixinwr")
	public String weixinwr(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			String jsonobject = XMLUtil.parseReq(request);
			log.error("[recharge-weixinpay]--["+jsonobject+"]");
			if(StringUtils.isNotBlank(jsonobject)){
				Map<String,Object> jsonmap = JSONObject.parseObject(jsonobject);
				String code = jsonmap.get("code").toString();
				if(StringUtils.isNotBlank(code) && "0000".equals(code)){
					boolean success =(boolean)jsonmap.get("success");
					if(success){//交易成功，解析data
						String sign=jsonmap.get("sign").toString();;
						JSONObject data = (JSONObject)jsonmap.get("data");
						log.error("[recharge-weixinpay]->data["+data+"]");
						//Map<String,Object> datamap = (Map<String, Object>) JSON.parse(data);
						//JSONObject datamap = JSONObject.parse(data);
						if(RequestSign.checkSign(data.toJSONString(),sign, "sign")){
							String tradeno = data.getString("orderId");
							String money = data.getString("totalAmount");
							String status = data.getString("status");
							String keyCode = data.getString("keyCode");
							if("1".equals(status)){
								TMemMoneyRecordVo vo = new TMemMoneyRecordVo();
								vo.setTradeno(tradeno);
								vo.setMoney(new BigDecimal(money));//入库时，已经乘100了。
								vo.setSuc("1");
								log.error("[recharge--weixinpay["+keyCode+"]-充值成功]");
								//修改入库
								//是否是商城订单，处理商城订单
								//进入通知队列
								
							}else{
								log.error("[recharge-weixinpay]-待支付>data["+data+"]");
							}
							pw.write("SUCCESS");
						}else{
							log.error("[recharge-weixinpay]-校验未通过>data["+data+"]");
							pw.write("SUCCESS");
						}
					}else{
						//业务失败
						log.error("[recharge-weixinpay]--业务失败->"+jsonobject);
						pw.write("SUCCESS");
					}
				}else{
					//业务失败
					log.error("[recharge-weixinpay]--code业务交易失败->"+jsonobject);
					pw.write("SUCCESS");
				}
			}else{
				log.error("[recharge-weixinpay]--没有返回值");
				pw.write("FAIL");
			}
			
		} catch (Exception e) {
			 log.error("[recharge-weixinpay]--失败:"+e.getMessage(),e);
			 pw.write("FAIL");
		}
		pw.flush();
		return null;
	}
	
	@RequestMapping(value="/{name}/wr")
	public String wr(@PathVariable(value="name") String name,HttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		try{
			out = response.getWriter();
			String result = XMLUtil.parseReq(request);
			log.error("recharge.wrong."+name+".result-->"+result);
			//result="respCode=0000&transactionId=4004482001201704126753596090&orderDate=20170412&respDesc=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F&transAmt=1100&productId=0108&orderNo=C170412170547192995&timeEnd=20170412170654&bankType=CCB_CREDIT&transId=10&signature=FPmCLEoSR2ihv0Dsf3WhqTlgM7d%2Brz4AEoG2ZDVvyqBN8i0Xu4AOwdfkZLOXfDssRzV3XzS3SPKU5XQ1yzJWJiFhwoPNdUHQ1Nrc8YGhug3RDnj80RlToYMF9T4dEZUPBzLkdegLTcC6pL%2BsCqenX5XDois0yodxDx%2BNB7mQ02ILWGrGp%2BcOyp3jSHMdTKl96EpRcx9b8oqa8T4gQRtDaxtiejIbJLUkr%2FvXyQ3yS2txiMy0lesSAlY%2FKCvOyfqcX5kO2XLfIldv3oWpbJyeB30GB0Rq0EqijRZfc5ncVOjQfo2qNFoqTmMd893Mdsd%2FehxwiwKBGdjOny1pZwfbnw%3D%3D&merNo=310440300002748&orderId=10003998136";
			//result ="codeUrl=d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXFnUnhRRmw=&commodityName=支付WR170227191957354017&imgUrl=http://113.107.235.97:9080/payment-gate-web/gateway/api/getQRCodeImage?d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPXFnUnhRRmw=&merNo=310440300001134&notifyUrl=http://admin.xyungame.net/notify/wanrong&orderDate=20170227&orderNo=WR170227191957354017&productId=0108&requestNo=RQ170227191957604390&respCode=0000&respDesc=交易成功&returnUrl=http://admin.xyungame.net/notify/wanrong&transAmt=100&transId=10&version=V1.0";
			//result ="respCode=0000&transactionId=4004482001201702271491728017&orderDate=20170227&respDesc=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F&transAmt=100&productId=0108&orderNo=WR170227103310624655&timeEnd=20170227103503&bankType=CFT&transId=10&signature=SsHaK%2F0CsSnzrGh6oQ3Y5ANm86lH1Lox11odMRZxDocym0oJzpVTkdeUdHV5Dq%2BeXBlFeM14P60m9fts2j5ruGntLDW5S6Bm1HbbutAKKgP0B%2FJr774Of61PKP3g9u09bIzfB9myptbVp9mp%2BtoK1JpC0h0SLCFG7hW8osWj4rDoyrhV9Zok0nZhKgcyuCH20seGs08DoBAYxcR2tGTsaj6HAPbmXZYQS0Od9Mk1BRHQuOPY9hwkAqzzO%2BD%2BryWR15fYwTC8jGh1yYU3w9J09z%2FiyU%2FBcfntGMwHP8kgHMR6RHYV8h%2Fxi7GW7eTnWrRvDBijlTc5r6DzedqvMjyTYg%3D%3D&merNo=310440300001134&orderId=200000211990";
			if(StringUtils.isNotBlank(result)){
					result = URLDecoder.decode(result, "utf-8");
					Map<String,Object> data = StringUtil.parseString(result);
					String responseCode = data.get("respCode").toString();//应答码
					String tradeno = data.get("orderNo").toString();
					String money  = data.get("transAmt").toString();//交易以分为单位
					String respDesc = data.get("respDesc").toString();
					String returncode = "1";
					String merNo = data.get("merNo").toString();
					//String sign = data.get("signature").toString();
					//data.remove("signature");
					String validstr = StringUtil.buildstr((TreeMap)data);
					//boolean b = RSAUtil.verifyByKeyPath(validstr, sign, PropertiesUtil.getValue("wanrong.public_key_path"), "UTF-8");
					String publicKeyPath = PropertiesUtil.getValue("wr."+name+".public_key_path");
					boolean b = SignUtils.verferSignData(validstr,publicKeyPath);
					if(log.isInfoEnabled()){
						log.info(b+"--wrong.validstr-->"+validstr+"|merNo="+merNo+",publicKeyPath="+publicKeyPath);
					}
					if(b){
						if("0000".equals(responseCode)){
							TMemMoneyRecordVo vo = new TMemMoneyRecordVo();
							vo.setTradeno(tradeno);
							vo.setMoney(new BigDecimal(money));//入库时，已经乘100了。
							vo.setSuc(returncode);
							log.error("wrong-"+name+"-recharge【"+name+"充值成功】-merNO="+merNo);
							//修改入库状态。
							//判断是否是商城订单，还需要修改商城订单。
							//进入通知队列。
							response.getWriter().write("SUCCESS");
							return null;
						}else{
							log.error("wrong."+name+".result-->状态不是成功["+data.toString()+"]");
							
						}
				}else{
					log.error("wrong."+name+".sing->数据校验未通过["+result+"]");
				}
			}else{
				log.error("wrong.jl1.result->没有获取到通过结果字符串["+result+"]");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		out.print("SUCCESS");
		out.close();
		return null;
	}
}
