package com.game.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.game.utils.common.BaseAction;

/**
 * @author wrong1111
 * 支付交换中心
 */

@Controller("/pay")
public class PayController extends BaseAction{

	/**
	 * @return
	 * 请求支付
	 */
	@RequestMapping("/recharge")
	@ResponseBody
	public Map<String,Object> recharge(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result =super.SUCESS();
		//通过解析request 获取参数 data,partnerid,service,time,version,key
		final	Map<String,String> requestdata = super.parseRequest(request);
		result.put("partnerid",requestdata.get("partnerid"));
		//验证参数是否合法。 0#cefp 
		String resultstr = requestdata.get("status");
		//0#开头的为成功标识可以进行后续业务逻辑处理了。
		try {
			if(resultstr.startsWith("0#")){
				String data = requestdata.get("data");
				
			}else{
				result.put("status", "1");
				result.put("msg", "md5校验未通过");
			}
		}catch(Exception e){
			result.put("status","9999");
			result.put("msg", e.getMessage());
		}
		return result;
	}
}
