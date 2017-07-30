package com.game.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.game.entity.TSysPartner;
import com.game.utils.encription.Md5Util;

public class Constants {
	private static Logger log = Logger.getLogger(Constants.class);
	
	public static Integer plataccount = 0;//指定平台账号
	public static String LOGIN_TOKEN_CODE="_token";
	public static final String WEIXIN	= "WEIXIN_txt";	//微信配置路径
	public static String accessToken = "";//微信号的accesstoken
	public static int accessTokenValid = 0;//微信号token过期 时间。
	public static long accessTokenTime = 0;//获取微信最后时间
	public static final int WXEXPIRED = 2*60*59;//1小时58分
	
	public static Integer RECHARGE_LIMIT = 2;
	public static final String RECHART_SEQ="recharge_seq";
	public static final String RECHARGE_WX="recharge_wx";
	public static final String RECHARGE_ZFB="recharge_zfb";
	
	public static String File_TEMP = "tmp";
	
	public static String PARTNER = "_goal.partner";
	/**
	 * 短信验证码保存有效时间，10分钟.
	 */
	public static int SMS_MAX_VALID = 10;//短信有效保存时间 10分钟
	
	
	/**
	 * 短信验证码有效签名串
	 */
	public static String SMS_SIGSTR="1243434QWWdxcw!@#$";
	
	
	/**
	 * 缓存过期 时间默认 3小时
	 */
	public final static int EXPIRED=3*60*60; 
	//全局的系统参数
	public static Map<String,String> configmap = new HashMap<String,String>();
	
	//全局的对外提示信息或异常参数都从数据库读取。
	public static Map<String, String> parametermap = new HashMap<String, String>();
	//全局商家partnerid
	public static Map<String,TSysPartner> partnermap = new HashMap<String, TSysPartner>();
		
	//充值账号_最后更新时间秒_充值成功次数    =>微信聚富万融_1232394209404_0
	public static String ALIPAY_LAST_PAY_COMAPANY_TIME	= "_"+System.currentTimeMillis()+"_0";
	public static String  WEIXIN_LAST_PAY_COMPANY_TIME	= "_"+ System.currentTimeMillis()+"_0";
	
	public static String getParamterkey(String key){
		return parametermap.get(key);
	}
	public static TSysPartner getPartner(String key){
		if(partnermap == null || partnermap.isEmpty()){
			DBhelper.intiPartnerMap();
			if(log.isInfoEnabled()){
				log.info("【Constats.getPartner.size】=>"+partnermap.size());
			}
		}
		return partnermap.get(key);
	}
	public static String getConfigkey(String key){
		if(configmap == null || configmap.isEmpty()){
			DBhelper.initConfigMap();
			if(log.isInfoEnabled()){
				log.info("【Constats.getConfig.size】=>"+configmap.size());
			}
		}
		return configmap.get(key);
	}
	
	 
	static{
		
		parametermap.put("9999","系统信息异常");
		parametermap.put("9001","md5校验未通过");
		parametermap.put("9002","参数不能为空");
		parametermap.put("9003","参数无效");
		parametermap.put("9004","目前只支持1.0版本");
		parametermap.put("9005","未获取有效登录token");
		parametermap.put("9006","用户未登录或登录已经失效");
		parametermap.put("9998","文件路径指定错误");
		parametermap.put("9007","指定文件不存在");
		parametermap.put("9008","值超过指定长度");
		parametermap.put("9009","不是有效的参数");
		parametermap.put("9010","不存在或已经过期");
		parametermap.put("9011","不是指定参数长度");
		parametermap.put("9012","参数包含敏感词");
		parametermap.put("9013","资料未完善");
		parametermap.put("9014","业务处理失败，请稍后重试或联系客服");
		parametermap.put("9015","业务错误，请调用合适的接口");
		parametermap.put("1031","在线支付限额X元/笔，大额支付请联系管理员");
		parametermap.put("1032","未达到最低限额");
		parametermap.put("1033","目前不支持此类充值，请更换充值方式");
		parametermap.put("1034","暂不支持此类业务");
		parametermap.put("1035","充值渠道未正确配置");
		
	}	
	 
}
