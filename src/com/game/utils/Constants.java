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
	
	//充值账号编码匹配
	public static Map<String, String> PAY_COMPANY_CODE = new HashMap<String,String>();
	public static Map<String, String> PAY_COMPANY_NAME = new HashMap<String,String>();
	
	
	//充值账号_最后更新时间秒_充值成功次数    =>微信聚富万融_1232394209404_0
	public static String ALIPAY_LAST_PAY_COMAPANY_TIME	= "_"+System.currentTimeMillis()+"_0";
	public static String  WEIXIN_LAST_PAY_COMPANY_TIME	= "_"+ System.currentTimeMillis()+"_0";
	
	private static long ptime = System.currentTimeMillis();
	private static long ctime = System.currentTimeMillis();
	public static TSysPartner getPartner(String key){
		if(partnermap == null || partnermap.isEmpty()){
			ptime = System.currentTimeMillis();
			DBhelper.intiPartnerMap();
			if(log.isInfoEnabled()){
				log.info("【Constats.getPartner.size】=>"+partnermap.size());
			}
		}
		return partnermap.get(key);
	}
	
	public static String getParamterkey(String key){
		return parametermap.get(key);
	}
	
	public static String getConfigkey(String key){
		if(configmap == null || configmap.isEmpty()){
			ctime = System.currentTimeMillis();
			DBhelper.initConfigMap();
			if(log.isInfoEnabled()){
				log.info("【Constats.getConfig.size】=>"+configmap.size());
			}
		}
		return configmap.get(key);
	}
	
	/**
	 * 发送短信模版
	 */
	public static Map<String,String> smsmap = new HashMap<String,String>();
	
	/***
	 * 彩种类别。
	 * */
	public final static Map<Integer,String> lotterymap = new HashMap<Integer,String>();
	
	/**
	 * 彩种玩法
	 */
	public static Map<String,String> playtypemap = new HashMap<String,String>();
	
	static{
		
		
		lotterymap.put(1,"幸运28");
		lotterymap.put(2,"快乐28");
		lotterymap.put(3,"每日竞猜");
		lotterymap.put(4,"加拿大28");
		lotterymap.put(5,"激情赛车");
		lotterymap.put(6,"红包雨");
		lotterymap.put(7,"幸运骰子");
		lotterymap.put(8,"西部28");
		lotterymap.put(21,"幸运28返水");
		lotterymap.put(24,"加拿大28返水");
		 
		
		playtypemap.put("1-A", "大小单双");
		playtypemap.put("1-B", "极值玩法");
		playtypemap.put("1-C", "组合玩法");
		playtypemap.put("1-D", "单点");
		playtypemap.put("1-E", "波胆玩法");
		
		
		playtypemap.put("21-A", "大小单双");
		playtypemap.put("21-B", "极值玩法");
		playtypemap.put("21-C", "组合玩法");
		playtypemap.put("21-D", "单点");
		playtypemap.put("21-E", "波胆玩法");
		
		
		playtypemap.put("2-A", "大小单双");
		playtypemap.put("2-B", "极值玩法");
		playtypemap.put("2-C", "组合玩法");
		playtypemap.put("2-D", "单点");
		playtypemap.put("2-E", "波胆玩法");
		
		playtypemap.put("3-A", "胜负玩法");
		 
		playtypemap.put("4-A", "大小单双");
		playtypemap.put("4-B", "极值玩法");
		playtypemap.put("4-C", "组合玩法");
		playtypemap.put("4-D", "单点");
		playtypemap.put("4-E", "波胆玩法");
		
		playtypemap.put("24-A", "大小单双");
		playtypemap.put("24-B", "极值玩法");
		playtypemap.put("24-C", "组合玩法");
		playtypemap.put("24-D", "单点");
		playtypemap.put("24-E", "波胆玩法");
		
		playtypemap.put("5-A", "冠军大小");
		playtypemap.put("5-B", "冠军单双");
		playtypemap.put("5-C", "冠军单点");
		playtypemap.put("5-D", "龙虎玩法");
		playtypemap.put("5-E", "冠亚军和大小");
		playtypemap.put("5-F", "冠亚军和单双");
		playtypemap.put("5-G", "冠亚军和组合");
		playtypemap.put("5-H", "冠亚军和单点");
		playtypemap.put("5-J", "牛牛玩法");
		
		
		playtypemap.put("7-A", "大小单双");
		playtypemap.put("7-C", "组合玩法");
		playtypemap.put("7-D", "单点玩法");
		playtypemap.put("7-E", "形态玩法");
		
		parametermap.put("9999","系统信息异常");
		parametermap.put("9001","md5校验未通过");
		parametermap.put("9002","参数不能为空");
		parametermap.put("9003","partnerid无效");
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
		parametermap.put("9016","此接口暂不支持此彩种");
		parametermap.put("9017","此账号已锁定，请联系客服");
		parametermap.put("9018","此账号已锁，暂停交易");
		parametermap.put("9019","此账号未关联本应用");
		parametermap.put("9020","此账号已经关联过本应用");
		
		parametermap.put("1001","用户已经存在");
		parametermap.put("1002","用户不存在");
		parametermap.put("1003","登录密码不正确");
		parametermap.put("1004","目前只支持一个用户一张银行卡");
		parametermap.put("1005","指定银行卡不存在");
		parametermap.put("1006","账户余额不足");
		parametermap.put("1007","指定彩种,当前期号不存在");
		parametermap.put("1008","指定彩种,不存在");
		parametermap.put("1009","指定彩种,期号不允许投注");
		parametermap.put("1010","指定彩种,投注期号不是当前期，不允许投注");
		parametermap.put("1011","指定彩种,已停售");
		parametermap.put("1012","指定彩种,此玩法已停售");
		parametermap.put("1013","指定彩种,此玩法已限售");
		parametermap.put("1014","指定彩种,此玩法不存在");
		parametermap.put("1015","指定彩种,期号已经开奖不允许投注");
		parametermap.put("1016","账户类型不具有对应权限，请联系客服");
		parametermap.put("1017","真实姓名错误");
		parametermap.put("1018","竞猜投注，指定场次,已过投注截止时间");
		parametermap.put("1019","竞猜投注，指定场次,已开赛果，不再接受投注");
		parametermap.put("1020","竞猜投注，指定场次,选项,赔率有误，请重新选择再下单");
		parametermap.put("1021","不是本人，无权限操作");
		parametermap.put("1022","不允许重复此项操作");
		parametermap.put("1023","您已经申请过，请查看审核结果");
		parametermap.put("1024","您账户余额不足以支付预备金");
		parametermap.put("1025","您指定预备金未达到上庄申请最低要求金额");
		parametermap.put("1026","您在当前彩种，未上庄不能进行此项操作");
		parametermap.put("1027","您在当前彩种，不能进行此项操作");
		parametermap.put("1028","您在当前彩种，上庄中不能进行此项操作");
		parametermap.put("1029","未找到上庄用户信息");
		parametermap.put("1030","您增加预备金不满足最低要求金额");
		parametermap.put("1031","在线支付限额X元/笔，大额支付请联系管理员");
		parametermap.put("1032","未达到最低限额");
		parametermap.put("1033","目前不支持此类充值，请更换充值方式");
		parametermap.put("1034","暂不支持此类业务");
		parametermap.put("1035","充值渠道未正确配置");
		parametermap.put("1036","不支持对冲投注");
		parametermap.put("2001","期结中");
		
		//公司名义申请的账号
		PAY_COMPANY_CODE.put("jl2_WXF","微江陵万融2");
		PAY_COMPANY_CODE.put("jl2_ZFB","支江陵万融2");
		
		PAY_COMPANY_CODE.put("jl1_WXF","微江陵万融1");
		PAY_COMPANY_CODE.put("jl1_ZFB","支江陵万融1");
		
		PAY_COMPANY_CODE.put("jfwr_WXF","微信聚富万融");
		PAY_COMPANY_CODE.put("jfwr_ZFB","支付宝聚富万融");
		 
		PAY_COMPANY_CODE.put("jlin_WXF","微信江陵万融");
		PAY_COMPANY_CODE.put("jlin_ZFB","支付江陵万融");
		
		PAY_COMPANY_NAME.put("微信聚富万融", "jfwr_WXF");
		PAY_COMPANY_NAME.put("支付宝聚富万融", "jfwr_ZFB");
		
		PAY_COMPANY_NAME.put("微信江陵万融", "jlin_WXF");
		PAY_COMPANY_NAME.put("支付江陵万融", "jlin_ZFB");
		
		PAY_COMPANY_NAME.put("微江陵万融1", "jl1_WXF");
		PAY_COMPANY_NAME.put("支江陵万融1", "jl1_ZFB");
		
		PAY_COMPANY_NAME.put("微江陵万融2", "jl2_WXF");
		PAY_COMPANY_NAME.put("支江陵万融2", "jl2_ZFB");
		
	}	
	
	/**
	 * @author wrong1111
	 *6001	    中奖
6002	    返利
6101	充值
6102	会员充值
6103	购买幸运币
6104	转进幸运币
6105	退还预备金

6201	商品兑换收入


7001	订单认购
7101	提现
7102	会员扣款
7201	商品兑换支出

7103	转出幸运币
7104	兑换幸运币
7105	支付预备金
7106	支付佣金

	 */
	public static enum TradeType{
		OUT_CREATE("7001"),OUT_DRAW("7101"),OUT_CHARGBACK("7102"),OUT_CHANGES("7201"),OUT_CHANGE_GIVE("7103"),OUT_GIVE("7104"),OUT_APPLYMONEY("7105"),OUT_FIRE("7106"),
		IN_AWARD("6001"),IN_FAVORABLE("6002"),IN_ACTIVE("6003"),IN_RECHARG("6102"),IN_CHANGES("6201"),IN_REVOKE("6004"),IN_BUY("6013"),IN_CHANGE_BUY("6104"),
		IN_REBACK("6105");
		String value;
		TradeType(String value){
			this.value = value;
		}
		public String getValue(){
			return this.value;
		}
	} 
	public static String createToken(Integer userid,String username){
		return Md5Util.md5(String.valueOf(userid)+DateUtils.date2String(new Date(), DateUtils.simpleDateFormat3)+username).toLowerCase();
	
	}
}
