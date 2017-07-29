package com.game.utils.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.game.utils.Constants;
import com.game.utils.Posturl;
import com.game.utils.encription.Md5Util;

public class MDSend implements ISend {
	static Logger log = Logger.getLogger(MDSend.class);
	private String sn = Constants.getConfigkey("sms.mandao.sn");// 序列号
	private String smsurl = Constants.getConfigkey("sms.mandao.smsurl");// 发送短信地址
	private String pwd = Constants.getConfigkey("sms.mandao.pwd");// 密码
	private static Map<String, String> errorcode = new HashMap<String, String>();

	 
	 
	@Override
	public String send(int i, String m, String c) {
		
		String[] smsurls = StringUtils.split(smsurl, "#");
		String result = "";
		String url = smsurls[0];
		 
		Map<String,Object> datamap = this.initBlanceMap();
		datamap.put("Mobile",m);
		try {
			datamap.put("Content",URLEncoder.encode(c,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		datamap.put("Ext","");
		datamap.put("Stime","");
		datamap.put("Rrid","");
		datamap.put("msgfmt","");
		while (true) {
			if (i > 2) {
				url = smsurls[1];
			}
			if (i > 5) {
				return "-1";
			}
			log.info("url="+url+">>sendmsg="+datamap.toString());
			//result = sendMsg(url+"/mt",datamap,"gb2312");
 			//result = Posturl.postRequest(url + "/mdSmsSend_u", datamap,"utf-8");
			result = Posturl.postRequest(url+"/mdsmssend", datamap);
			log.error(result);
			if(StringUtils.isNotBlank(result)){
				result = this.xmlParse(result,"string");
				break;
			}
			log.info(result);
			log.error("漫道短信接口发送结果：" + result);
			i++;
		} 
		return result;
	}

	static {
		errorcode.put("-2", "帐号/密码不正确");
		errorcode.put("-4", "余额不足支持本次发送");
		errorcode.put("-5", "数据格式错误");
		errorcode.put("-6", "参数有误");
		errorcode.put("-7", "权限受限");
		errorcode.put("-8", "流量控制错误");
		errorcode.put("-9", "扩展码权限错误");
		errorcode.put("-10", "内容长度长");
		errorcode.put("-11", "内部数据库错误");
		errorcode.put("-12", "序列号状态错误");
		errorcode.put("-14", "服务器写文件失败");
		errorcode.put("-17", "没有权限");
		errorcode.put("-19", "禁止同时使用多个接口地址");
		errorcode.put("-20", "相同手机号，相同内容重复提交");
		errorcode.put("-22", "Ip鉴权失败");
		errorcode.put("-23", "缓存无此序列号信息");
		errorcode.put("-601", "序列号为空，参数错误");
		errorcode.put("-602", "序列号格式错误，参数错误");
		errorcode.put("-603", "密码为空，参数错误");
		errorcode.put("-604", "手机号码为空，参数错误");
		errorcode.put("-605", "内容为空，参数错误");
		errorcode.put("-606", "ext长度大于9，参数错误");
		errorcode.put("-607", "参数错误 扩展码非数字");
		errorcode.put("-608", "参数错误 定时时间非日期格式");
		errorcode.put("-609", "rrid长度大于18,参数错误 ");
		errorcode.put("-610", "参数错误 rrid非数字");
		errorcode.put("-611", "参数错误 内容编码不符合规范");
		errorcode.put("-623", "手机个数与内容个数不匹配");
		errorcode.put("-624", "扩展个数与手机个数数");
		errorcode.put("-625", "定时时间个数与手机个数数不匹配");
		errorcode.put("-626", "rrid个数与手机个数数不匹配");

	}

	@Override
	public String blance(int c) {
		String[] smsurls = StringUtils.split(smsurl, "#");
		String result = "";
		String url = smsurls[0];
		if (c > 2) {
			url = smsurls[1];
		}
		while (true) {
			if (c > 5) {
				return "-1";
			}
			result = Posturl
					.postRequest(url + "/balance", this.initBlanceMap());
			log.error("漫道短信接口余额查询：" + result);
			try {
				if (StringUtils.isNotBlank(result)) {
					result = this.xmlParse(result,"string");
					break;
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				c++;
				blance(c++);
			}
			c++;
		}
		return result;
	}

	/**
	 * 初始化传参
	 * 
	 * @param c
	 * @return
	 */
	private Map<String, Object> initBlanceMap() {
		Map<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("Sn", this.sn);
		datamap.put("Pwd", Md5Util.md5_32(this.sn + this.pwd).toUpperCase());
		return datamap;
	}

	public static void main(String[] args) {
		ISend send = new MDSend();
		send.blance(0);
	}

	public String xmlParse(String xmlString, String nodename) {
		if(StringUtils.isBlank(xmlString)) return "";
		Document doc = null;
		try {
			// 读取并解析XML文档
			// SAXReader就是一个管道，用一个流的方式，把xml文件读出来
			// SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
			// Document document = reader.read(new File("User.hbm.xml"));
			// 下面的是通过解析xml字符串的
			doc = DocumentHelper.parseText(xmlString); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			log.info("nodename:" + nodename + "==根节点：" + rootElt.getName()); // 拿到根节点的名称
			if (nodename.equals(rootElt.getName())) {
				String s = rootElt.getTextTrim();
				log.info("result:" + s);
				return s;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
}
