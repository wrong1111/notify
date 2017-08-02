package com.game.controller;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.game.pojo.NotifyVo;
import com.game.serverutil.excutor.NotifyParam;
import com.game.serverutil.excutor.NotifyQueue;
import com.game.serverutil.excutor.NotifyTask;
import com.game.serverutil.excutor.NotifyThread;
import com.game.service.OrderService2;
import com.game.service.PayService;
import com.game.utils.PropertiesUtil;
import com.game.utils.StringUtil;
import com.game.utils.XMLUtil;
import com.game.utils.common.BaseAction;
import com.game.utils.wanrong.SignUtils;
import com.game.utils.wanrong.weixin.RequestSign;

@Controller
@RequestMapping("/notify")
public class NotifyController extends BaseAction{


	@Autowired
	NotifyParam notifyParam;
	
	@Autowired
	NotifyQueue notifyQueue;
	
	@Autowired
	PayService payService;
	
	@Autowired
	OrderService2 orderService2;
	
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
				String sign=jsonmap.get("sign").toString();;
				JSONObject data = (JSONObject)jsonmap.get("data");
				log.error("[recharge-weixinpay]->data["+data+"]");
				
				String tradeno = data.getString("orderId");
				String money = data.getString("totalAmount");
				String status = data.getString("status");
				String keyCode = data.getString("keyCode");
				
				boolean success =(boolean)jsonmap.get("success");
				NotifyVo vo = new NotifyVo();
				vo.setMerchantNo(keyCode);
				vo.setNotifyTimes(0);
				vo.setCreateTime(new Date());
				vo.setMerchantOrderNo(tradeno);
				vo.setLimitNotifyTimes(notifyParam.getMaxNotifyTime());
				vo.setStatus("1".equals(status) && success ?"0000":"1111");
				vo.setResponseDesc(("1".equals(status)?"交易成功":"交易失败")+jsonmap.get("success"));
				vo.setNoticestr(jsonobject);//上游接口通知的字符串
				if(StringUtils.isNotBlank(code) && "0000".equals(code)){
					if(success){//交易成功，解析data
						if(RequestSign.checkSign(data.toJSONString(),sign, "sign")){
							//修改入库操作
							vo = payService.updatePayReceive(vo);
							if("1".equals(status)){
								log.error("[recharge--weixinpay["+keyCode+"]-充值成功]");
							}else{
								log.error("[recharge-weixinpay]-待支付>data["+data+"]");
							}
							NotifyThread.tasks.add(new NotifyTask(payService,vo,notifyQueue,notifyParam));
							pw.write("SUCCESS");
						}else{
							vo.setResponseDesc("校验失败");
							//修改入库操作
							vo = payService.updatePayReceive(vo);
							NotifyThread.tasks.add(new NotifyTask(payService,vo,notifyQueue,notifyParam));
							log.error("[recharge-weixinpay]-校验未通过>data["+data+"]");
							pw.write("SUCCESS");
						}
					}else{
						//业务失败
						vo = payService.updatePayReceive(vo);
						NotifyThread.tasks.add(new NotifyTask(payService,vo,notifyQueue,notifyParam));
						log.error("[recharge-weixinpay]--业务失败->"+jsonobject);
						pw.write("SUCCESS");
					}
				}else{
					//业务失败
					vo = payService.updatePayReceive(vo);
					NotifyThread.tasks.add(new NotifyTask(payService,vo,notifyQueue,notifyParam));
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
					String validstr = StringUtil.buildstr((TreeMap)data);
					String publicKeyPath = PropertiesUtil.getValue("wr."+name+".public_key_path");
					boolean b = SignUtils.verferSignData(validstr,publicKeyPath);
					log.info("["+b+"]--wrong.validstr-->"+validstr+"|publicKeyPath="+publicKeyPath);
					if(b){
						String responseCode = data.get("respCode").toString();//应答码
						String tradeno = data.get("orderNo").toString();
						String respDesc = data.get("respDesc").toString();
						String merNo = data.get("merNo").toString();
						
						NotifyVo vo = new NotifyVo();
						vo.setMerchantNo(merNo);
						vo.setNotifyTimes(0);
						vo.setCreateTime(new Date());
						vo.setMerchantOrderNo(tradeno);
						vo.setLimitNotifyTimes(notifyParam.getMaxNotifyTime());
						vo.setStatus("0000");
						vo.setResponseDesc(respDesc);
						vo.setNoticestr(result);//上游接口通知的字符串
						//修改入库操作
						vo = payService.updatePayReceive(vo);
						if("0000".equals(responseCode)){
							log.error("wrong-"+name+"-recharge【"+name+"充值成功】-merNO="+merNo);
						}else{
							vo.setStatus(responseCode);
							//通知业务失败或者状态不正常
							log.error("wrong."+name+".result-->状态不是成功["+data.toString()+"]");
						}
						//进入通知队列。
						NotifyThread.tasks.add(new NotifyTask(payService,vo,notifyQueue,notifyParam));
						response.getWriter().write("SUCCESS");
						return null;
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
