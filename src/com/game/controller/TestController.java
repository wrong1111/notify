package com.game.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.game.entity.EcsOrderInfo;
import com.game.entity.TSysConfig;
import com.game.service.OrderService2;
import com.game.service.SysService;
import com.game.utils.common.BaseAction;

@Controller
@RequestMapping("/test")
public class TestController extends BaseAction {
	
	@Autowired
	OrderService2 orderService2;
	
	@Autowired
	SysService sysService;
	
	@RequestMapping("/order2")
	@ResponseBody
	public Map<String,Object> order2(@RequestParam(value="ordersn") String ordersn) {
		Map<String,Object> result = super.SUCESS();
		if(StringUtils.isNotBlank(ordersn)) {
			EcsOrderInfo info = orderService2.findByOrdersn(ordersn);
			result.put("sevice2",  JSON.toJSON(info==null?new EcsOrderInfo():info));
		}
		return result;
	}
	@RequestMapping("/u2")
	@ResponseBody
	public Map<String,Object> u2(@RequestParam(value="ordersn") String ordersn) {
		Map<String,Object> result = super.SUCESS();
		try {
			if(StringUtils.isNotBlank(ordersn)) {
				EcsOrderInfo info = orderService2.findByOrdersn(ordersn);
				if(info!=null) {
					info.setOrder_status(1);
					info.setPay_status(2);
					info.setShipping_status(3);
					orderService2.updateEcsOrderStatus(info);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping("/order")
	@ResponseBody
	public Map<String,Object> order(@RequestParam(value="keyname") String keyname) {
		Map<String,Object> result = super.SUCESS();
		try {
			if(StringUtils.isNotBlank(keyname)) {
				TSysConfig  info = sysService.findByKey(keyname);
				result.put("sevice",  JSON.toJSON(info==null?new EcsOrderInfo():info));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
