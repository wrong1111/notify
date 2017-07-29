package com.game.utils.wx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.game.utils.Constants;
import com.game.utils.DateUtils;
import com.game.utils.Posturl;
import com.game.utils.PropertiesUtil;
import com.game.utils.QRCodeUtil;
import com.game.utils.TradUtil;
import com.game.utils.XMLUtil;
import com.game.utils.encription.Md5Util;

/*
为了保证支付接口的安全性，本次升级提交接口，在接口提交参数中增加了sign2参数，sign2的加密方式："money={0}&userid={1}&orderid={2}&bankid={3}&keyvalue={4}" 。接口文档请在后台重新下载，请即时升级，若有疑问请联系技术。
另外对接时请注意以下2点：
1.接受通知时请校验下发成功金额与本地金额是否一致，可以接口查询实际成功金额。
2.接受通知时1秒可能会下发多次，请做防并发处理。
*/
public class Card70Util {	

	static Logger log = Logger.getLogger(Card70Util.class);
	final static String url = PropertiesUtil.getValue("70card.url");
	final static String keyvalue =  PropertiesUtil.getValue("70card.keyvalue");
	final static String userid =  PropertiesUtil.getValue("70card.userid");
	final static String synchadviceurl = PropertiesUtil.getValue("70card.result.adviceurl");
	//http://yy.yzch.net/pay.aspx?userid={}&orderid={}&money={}&url={}&aurl={}&bankid={}&sign={}&ext={}
	/**
	 * @param money 金额，元为单位
	 * @param tradeno 订单号
	 * @param code 微信	2001 支付宝	2003
	 @param ext 额外参数
	 * @return
	 */
	public static String buildUrl(String money,String tradeno,String code,String ext){
		String md5str = Md5Util.md5("userid="+userid+"&orderid="+tradeno+"&bankid="+code+"&keyvalue="+keyvalue).toLowerCase();
		String md5str2 = Md5Util.md5("money="+money+"&userid="+userid+"&orderid="+tradeno+"&bankid="+code+"&keyvalue="+keyvalue).toLowerCase();
		String result = url+"?userid="+userid+"&orderid="+tradeno+"&money="+money+"&url="+synchadviceurl+"&bankid="+code+"&sign="+md5str+"&ext="+ext+"&sign2="+md5str2;
		return result;
	} 
	private static String mkdirect(String path){
		return path+"/"+DateUtils.date2String(new Date(), new SimpleDateFormat("yyyy/MM/dd"))+"/";
	}
	private static String mkfilename(String suff){
		return TradUtil.getRandomStr(10)+"."+suff;
	}
	public static Map<String,Object> parseRequest(String r) throws Exception{
		String result = Posturl.getRequest(r);
		if(StringUtils.isBlank(result)){
			return new HashMap<String,Object>();
		}
		if(StringUtils.isNotBlank(result) && result.length()>20 && result.indexOf("<root>")>-1){
			Map<String,Object> data=   XMLUtil.xml2Map(result);
			if(data.get("imgurl")!=null){
				String file = Constants.getConfigkey("common.file.path");
				String url = data.get("imgurl").toString();
				String path_ = QRCodeUtil.buildcodeurl(Constants.getConfigkey("upload.image.base64.domain"),
						file,mkdirect("/qrcode"),
						url, file+"/logo.jpg",mkfilename("jpg"));
				data.put("img",path_);
			}
			return data;
		}else{
			log.error("[card70]->"+r+"|result=>"+result);
		}
		return new HashMap<String,Object>();
		//<root><returncode>1</returncode><imgurl>http://storenew.yzch.net/XingYeWxPayOwn1.aspx?OrderNo=E117020600139459</imgurl><orderno>E117020600139459</orderno></root>
	}
	
}
