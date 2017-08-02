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
import com.alibaba.fastjson.serializer.SerializerFeature;
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
        String url = request.getRequestURL().toString();
        //不拦截上面定义的路径
        for (String str : IGNORE_URL) {
            if (url.contains(str)) {
               return true;
            }
        }
        String contentType = request.getContentType();
        String charset = request.getCharacterEncoding();
        if(StringUtils.isBlank(contentType)) {
        	contentType = "application/json;charset=UTF-8";
        }
        if(StringUtils.isNotBlank(contentType)) {
        	response.setContentType(contentType);
        }else {
        	response.setContentType(contentType);
        }
        if(StringUtils.isNotBlank(charset)) {
        	response.setCharacterEncoding(charset);
        }
        
        String data = request.getParameter("data");
		String partnerid =request.getParameter("partnerid");
		String version = request.getParameter("version");
		String key = request.getParameter("key");
		String requestip = BaseAction.getIpAddr(request);
		logger.error("[AuthorIntercepter.preHandle],请求方法["+url+"],请求参数["+data+"],partnerid["+partnerid+"],version["+version+"],key["+key+"],requestid["+requestip+"]");
		Map<String,String> paramap = new HashMap<String,String>();
		String md5str = data+partnerid+version;
		
		if(StringUtils.isBlank(data)){
			paramap.put("status","9002");
			paramap.put("partnerid",partnerid);
			paramap.put("msg", "data"+Constants.getParamterkey("9002"));
			if(StringUtils.isNotBlank(contentType) && contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap,SerializerFeature.WriteMapNullValue));
	        	return false;
	        }
		}
		if(StringUtils.isBlank(partnerid)){
			paramap.put("status","9002");
			paramap.put("partnerid",partnerid);
			paramap.put("msg", "partnerid"+Constants.getParamterkey("9002"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap,SerializerFeature.WriteMapNullValue));
	        	return false;
	        }
		}
		if(StringUtils.isBlank(version)){
			paramap.put("status","9002");
			paramap.put("partnerid",partnerid);
			paramap.put("msg", "version"+Constants.getParamterkey("9002"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap,SerializerFeature.WriteMapNullValue));
	        	return false;
	        }
		}
		TSysPartner tp = Constants.getPartner(partnerid);
		if(tp == null){
			paramap.put("status","9003");
			paramap.put("partnerid",partnerid);
			paramap.put("msg", Constants.getParamterkey("9003"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap,SerializerFeature.WriteMapNullValue));
	        	return false;
	        }
		}
		if(new Date().after(tp.getEndtime())){
			paramap.put("status","9010");
			paramap.put("partnerid",partnerid);
			paramap.put("msg", "【"+tp.getPartnername()+"】"+Constants.getParamterkey("9010"));
			if(contentType.indexOf("json")>-1) {
	        	response.getWriter().write(JSON.toJSONString(paramap,SerializerFeature.WriteMapNullValue));
	        	return false;
	        }
		}
		String signString = tp.getSignestring();//PropertiesUtil.getValue("pay.application.signstring");
		md5str = md5str+signString;
		paramap.put("md5str", signString);
 		if(key.equalsIgnoreCase(Md5Util.md5_32(md5str))){
			 return true;
 		} 
 		paramap.put("status","9001");
		paramap.put("partnerid",partnerid);
		paramap.put("msg",Constants.getParamterkey("9001"));
        if(contentType.indexOf("json")>-1) {
        	response.getWriter().write(JSON.toJSONString(paramap,SerializerFeature.WriteMapNullValue));
        	return false;
        }
        return true;
    }
    
}
