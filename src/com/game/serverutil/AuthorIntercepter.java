package com.game.serverutil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.game.entity.TSysPartner;
import com.game.utils.Constants;
import com.game.utils.common.BaseAction;
import com.game.utils.encription.Md5Util;

@Component("authorIntercepter")
public class AuthorIntercepter extends HandlerInterceptorAdapter{
	Logger logger = Logger.getLogger(getClass());
	
	String[] IGNORE_URL = new String[] {"/notify/","/interface/test/"};
	
	@Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        String url = request.getRequestURL().toString();
        //不拦截上面定义的路径
        for (String str : IGNORE_URL) {
            if (url.contains(str)) {
               return true;
            }
        }
        String contentType = request.getContentType();
        String charset = request.getCharacterEncoding();
        response.setContentType(contentType);
        response.setCharacterEncoding(charset);
        
        String data = request.getParameter("data");
		String partnerid =request.getParameter("partnerid");
		String version = request.getParameter("version");
		String key = request.getParameter("key");
		String requestid = BaseAction.getReqestIp(request);
		logger.error("[AuthorIntercepter.preHandle],请求方法["+url+"],请求参数["+data+"],partnerid["+partnerid+"],version["+version+"],key["+key+"]");
		Map<String,String> paramap = new HashMap<String,String>();
		String md5str = data+partnerid+version;
		
		if(StringUtils.isBlank(data)){
			paramap.put("status","9002#data"+Constants.getParamterkey("9002"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap));
	        	return false;
	        }
		}
		if(StringUtils.isBlank(partnerid)){
			paramap.put("status","9002#partnerid"+Constants.getParamterkey("9002"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap));
	        	return false;
	        }
		}
		if(StringUtils.isBlank(version)){
			paramap.put("status","9002#version"+Constants.getParamterkey("9002"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap));
	        	return false;
	        }
		}
		TSysPartner tp = Constants.getPartner(partnerid);
		if(tp == null){
			paramap.put("status","9003#"+Constants.getParamterkey("9003"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap));
	        	return false;
	        }
		}
		if(new Date().after(tp.getEndtime())){
			paramap.put("status","9010#"+"【-"+tp.getPartnername()+"】"+Constants.getParamterkey("9010"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap));
	        	return false;
	        }
		}
		String signString = tp.getSignestring();//PropertiesUtil.getValue("pay.application.signstring");
		md5str = md5str+signString;
		paramap.put("md5str", signString);
 		if(key.equalsIgnoreCase(Md5Util.md5_32(md5str))){
			 return true;
 		} 
 	    
 		paramap.put("status","9001#"+Constants.getParamterkey("9001")); 
        if(contentType.indexOf("json")>-1) {
        	response.getWriter().write(JSON.toJSONString(paramap));
        	return false;
        }
        return true;
    }
    
}
