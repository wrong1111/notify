package com.game.utils.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.game.entity.TSysPartner;
import com.game.utils.Constants;
import com.game.utils.CookieUtil;
import com.game.utils.DateUtils;
import com.game.utils.MemcacheUtil;
import com.game.utils.PropertiesUtil;
import com.game.utils.encription.Md5Util;


public class BaseAction {

	public Logger log = Logger.getLogger(getClass());
	
	Integer page = 1;
	Integer pagesize = 20;
	Integer id;
	String token;
	String version;
	String partnerid;
	String key;
	String service;
	
	
	String msg;
	String status;
	
	/**
	 * params  校验 
	 * @param resultFlag
	 * @return
	 */
	protected Map<String, Object> vaildParamsResult(String resultFlag){
		Map<String, Object> result=new TreeMap<String,Object>();
		String[] reStrings = resultFlag.split("#");
		result.put("status", reStrings[0]);
		result.put("msg", reStrings[1]);
		String rkey = md5str2(result);
		result.put("key", rkey);
		return result;
	}
	/**
	 * 系统内部错误返回
	 * @return
	 */
	protected Map<String, Object> exceptionResult(){
		Map<String, Object> result=new TreeMap<String,Object>();
		result.put("status", "9999");
		result.put("msg", Constants.getParamterkey("9999"));
		String rkey = md5str2(result);
		result.put("key", rkey);
		return result;
	}
	/**
	 * 返回操作或处理成功map
	 * 简化返回处理
	 * @return
	 */
	public Map<String,Object> SUCESS(){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", "0");
		result.put("msg", "成功");
		return result;
	}
	public String getSignstrByPartnerid(String partnerid){
		String returnStr = DateUtils.date2String(new Date(), new SimpleDateFormat("yyyyMMddHHMMSS"));
		TSysPartner tp = Constants.getPartner(partnerid);
		if(tp == null){
			return returnStr;
		}
		return tp.getSignestring();
	}
	
	
	/**
	 * 解析请求入口的参数
	 *  * 1，初步简单的校验传入参数
	 * 2，验证md5码{所有参数排列顺序见文档说明}
	 * 3,校验partnerid在数据库中是否有匹配存在的。
	 * 全部通过，返回0#
	 * @param request
	 * @return
	 */
	public Map<String,String> parseRequest(HttpServletRequest request){
		//若有emoji表情特殊字符则替换为*号，让Md5校验不通过
		String data = request.getParameter("data");
		String partnerid =request.getParameter("partnerid");
		String version = request.getParameter("version");
		String key = request.getParameter("key");
		String requestid = this.getReqestIp(request);
		Map<String,String> paramap = new HashMap<String,String>();
		paramap.put("data",data);
		paramap.put("partnerid",partnerid);
		paramap.put("version",version);
		paramap.put("key",key);
		paramap.put("requestid", requestid);
		if(log.isInfoEnabled()){
			log.info("[baseAction.parseRequest.paramap] ==> "+paramap.toString());	
		}
		 
		String md5str = data+partnerid+version;
		if(StringUtils.isBlank(data)){
			paramap.put("status","9002#data"+Constants.getParamterkey("9002"));
			return paramap;
		}
		if(StringUtils.isBlank(partnerid)){
			paramap.put("status","9002#partnerid"+Constants.getParamterkey("9002"));
			return paramap;
		}
		if(StringUtils.isBlank(version)){
			paramap.put("status","9002#version"+Constants.getParamterkey("9002"));
			 return paramap;
		}
		TSysPartner tp = Constants.getPartner(partnerid);
		if(tp == null){
			paramap.put("status","9003#"+Constants.getParamterkey("9003"));
			return paramap;
		}
		if(new Date().after(tp.getEndtime())){
			paramap.put("status","9010#"+"【-"+tp.getPartnername()+"】"+Constants.getParamterkey("9010"));
			return paramap;
		}
		String signString = PropertiesUtil.getValue("pay.application.signstring");
		md5str = md5str+signString;
		paramap.put("md5str", signString);
		if(log.isInfoEnabled()){
			log.info("[baseAction.validparameters.md5str] ==> "+md5str);	
			log.info("【"+Md5Util.md5_32(md5str)+"】");
		} 	
 		if(key.equalsIgnoreCase(Md5Util.md5_32(md5str))){
			 paramap.put("status","0#ok");
			 return paramap;
 		} 
 	     paramap.put("status","9001#"+Constants.getParamterkey("9001"));
 	     return paramap;
 	    
		// paramap.put("status","0#ok");
		 //return paramap;
	}
	
	/**
	 * 对返回的所有值，进行排序以后再加密
	 * 返回对应的key
	 * @param paramer
	 * @return
	 */
	public String md5str2(Map<String,Object> paramer){
		Map<String,Object> paramap = new TreeMap<String,Object>();
		paramap.putAll(paramer);
		String signstr  ="";
		if(paramer.get("partnerid") !=null){
			TSysPartner tp = Constants.getPartner(partnerid);
			if(tp != null){
				signstr = tp.getSignestring();
			}
		}
		StringBuilder mdstr = new StringBuilder(); 
		Iterator<String> itor = paramap.keySet().iterator();
		while(itor.hasNext()){
			String key = itor.next().toString();
			mdstr.append(paramer.get(key));
		}
		return Md5Util.md5_32(mdstr+signstr);
	}
	
	
	/**
	 * 验证token 
	 * @param  token 
	 *         token 串对象
	 *          
	 *    token 正常    返回 Map("userid",userid)
	 *                     ("status","0") 
	 * @return
	 */
	public Map<String, Object> vaildToken(Object token) {
		Map<String, Object> result = new TreeMap<String, Object>();
		// token 判断
		if (null == token) {
			result.put("status", "9010");
			result.put("msg", Constants.getParamterkey("common.login.token"));
			String rkey = this.md5str2(result);
			result.put("key", rkey);
			return result;
		}else{
			//传入token为string=""的情况
			if(StringUtils.isBlank(String.valueOf(token))){
				result.put("status", "9010");
				result.put("msg", Constants.getParamterkey("common.login.token"));
				String rkey = this.md5str2(result);
				result.put("key", rkey);
				return result;
			}
		}
		String userid = this.getUserid(token.toString());
		if (StringUtils.isBlank(userid)) {
			result.put("status", "9009");
			result.put("msg", Constants.getParamterkey("common.param.tokenInvalid"));
			String rkey = this.md5str2(result);
			result.put("key", rkey);
			return result;
		}
		result.put("status", "0");
		result.put("userid", userid);
		return result;
	}
	
	/**
	 *  从COOKIE 中获取TOKEN 字符串。
	 * @param request
	 * @param response
	 * @return String
	 */
	public String getTokenString(HttpServletRequest request,
			HttpServletResponse response) {
		return CookieUtil.getCookieValue(request, Constants.LOGIN_TOKEN_CODE);
	}
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					log.error("未知主机",e);
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) {
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	/**
	 * 获取用户ip地址
	 * @param request
	 * @return
	 */
	public String getReqestIp(HttpServletRequest request) {
		// 有cnd 加速时也能取到用户ip地址
		if (request == null)
			return "";
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null) {
			ip = request.getHeader("X-Real-IP");
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Cdn-Src-Ip");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		}
		if (ip != null) {
			return ip.split(",")[0].trim();
		}
		return ip;
	}
	
	
	/**
	 * 获取用户ID
	 * @param token
	 * @return
	 */
	public String getUserid(String token) {
		if(StringUtils.isBlank(token)){
			return "";
		}
		try{
			Object obj = MemcacheUtil.get(token);
			if(obj==null){
				return "";
			}else{
				Map<String,String> umap = (HashMap<String,String>)obj;
				return umap.get("_userid")!=null?umap.get("_userid").toString():"";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取用户名
	 * @param token
	 * @return
	 */
	public String getUsername(String token) {
		if(StringUtils.isBlank(token)){
			return "";
		}
		try{
			Object obj = MemcacheUtil.get(token);
			Map<String,String> umap = (HashMap<String,String>)obj;
			String u = umap.get("_username").toString();
			return u;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	 
	/**
	 * 有空值的返回json也会以null填充
	 * @param callback
	 * @param resultmap
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String,Object> callback3(String callback,Map<String,Object> resultmap,HttpServletRequest request,HttpServletResponse response){
		if(StringUtils.isBlank(callback)){
			return resultmap;
		} 
		try {
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(callback+"("+JSON.toJSONString(resultmap)+")");
		} catch (IOException e) {
			 
		}
		return null;
	}
	public Object callback(String callback,Map<String,Object> resultmap,HttpServletRequest request,HttpServletResponse response){
		return callback2(callback,resultmap,request,response);
	}
	public Object callback2(String callback,Map<String,Object> resultmap,HttpServletRequest request,HttpServletResponse response){
		if(StringUtils.isBlank(callback)){
			Object obj = JSONObject.toJSONString(resultmap, SerializerFeature.WriteMapNullValue);
			return obj;
		} 
		try {
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(callback+"("+JSONObject.toJSONString(resultmap, SerializerFeature.WriteMapNullValue)+")");
		} catch (IOException e) {
			 
		}
		return null;
	}
	 
	/**
	 * 获取用户账户余额
	 * @param token
	 * @return
	 */
	public String getUsermoney(String token) {
		if(StringUtils.isBlank(token)){
			return "0";
		}
		try{
			Object obj = MemcacheUtil.get(token);
			Map<String,String> umap = (HashMap<String,String>)obj;
			String m = umap.get("_amount").toString();
			return m;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 判断用户是否登录
	 * @param token
	 * @return
	 */
	public boolean isLogin(String token){
		if(StringUtils.isBlank(token)){
			return false;
		}
		boolean bool = false;
		try{
			if(MemcacheUtil.get(token)!=null){
				bool = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bool;
	}
	
	/**
	 * 判断用户是否具有商家权限
	 * @param token
	 * @return
	 */
	public boolean isShop(String token){
		if(StringUtils.isBlank(token)){
			return false;
		}
		boolean bool = false;
		try{
			Map<String, String> usermap = (HashMap<String, String>)MemcacheUtil.get(token);
			if(usermap!=null&&StringUtils.isNotBlank(usermap.get("_shopid"))&&"2".equals(usermap.get("_shopflag"))){
				bool = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bool;
	}
	
	/**店铺正常或者店铺在审核中
	 * <pre>
	 * @param token
	 * @return
	 * </pre>
	 */
	public boolean isShop2(String token){
		if(StringUtils.isBlank(token)){
			return false;
		}
		boolean bool = false;
		try{
			Map<String, String> usermap = (HashMap<String, String>) MemcacheUtil.get(token);
			if (usermap != null && StringUtils.isNotBlank(usermap.get("_shopid"))
					&& ("2".equals(usermap.get("_shopflag")) || "1".equals(usermap.get("_shopflag")))||"4".equals(usermap.get("_shopflag"))) {
				bool = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bool;
	}
	
	/**
	 * <pre>
	 * @param serviceName
	 * @param paramsService
	 * @return
	 * </pre>
	 */
	public Map<String, Object> validService(String serviceName,String paramsService){
		Map<String, Object> resultmap=new HashMap<String,Object>();
		if(!serviceName.equalsIgnoreCase(paramsService)){
			resultmap.put("status","9007");
			resultmap.put("msg",Constants.getParamterkey("common.param.service"));
			resultmap.put("key",this.md5str2(resultmap));
		}else{
			resultmap.put("status", "0");
		}
		return resultmap;
	}
	
	/**
	 * 获取商家店铺id
	 * @param token
	 * @return
	 */
	public String getShopid(String token){
		if(StringUtils.isBlank(token)){
			return "";
		}
		try{
			Map<String, String> usermap = (HashMap<String, String>)MemcacheUtil.get(token);
			String s= usermap.get("_shopid");
			return s;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
