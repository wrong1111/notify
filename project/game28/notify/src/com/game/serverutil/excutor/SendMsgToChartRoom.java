package com.game.serverutil.excutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.game.utils.PropertiesUtil;
import com.game.utils.rongyun.rong.ApiHttpClient;
import com.game.utils.rongyun.rong.models.FormatType;
import com.game.utils.rongyun.rong.models.Message;
import com.game.utils.rongyun.rong.models.SdkHttpResult;
import com.game.utils.rongyun.rong.models.TxtMessage;

public class SendMsgToChartRoom implements Runnable{

	Logger log = Logger.getLogger(getClass());
	
	Integer userid;
	String username;
	String imgurl;
	String content;
	String rooms;
	String appKey;
	String appSecret;
	/**
	 * @param userid 用户id
	 * @param username 用户名
	 * @param imgurl 图像
	 * @param content 内容
	 * @param rooms roomid
	 * @param appKey 键
	 * @param appSecret 秘钥
	 */
	public SendMsgToChartRoom (Integer userid,String username,String imgurl,String content,String rooms,String appKey,String appSecret){
		this.userid = userid;
		this.content = content;
		this.rooms = rooms;
		this.username = username;
		this.imgurl = imgurl;
		this.appKey = appKey;
		this.appSecret = appSecret;
	}
	@Override
	public void run() {
		send();
	}

	private void send(){
 		if(StringUtils.isBlank(appKey)){
 			appKey = PropertiesUtil.getValue("h5.rongyun.appKey");
 		}
 		if(StringUtils.isBlank(appSecret)){
 			appSecret = PropertiesUtil.getValue("h5.rongyun.appSecret");
 		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("error","");
		result.put("msg","成功");
		try {
			if(userid == null || StringUtils.isBlank(content)|| StringUtils.isBlank(appKey) || StringUtils.isBlank(appSecret)){
				 return;
			}
			log.error("[chartroom],userid["+userid+"],username["+username+"],imgurl["+imgurl+"],content["+content+"]");
			Message mess = new TxtMessage(content);
			SdkHttpResult sdkresult = ApiHttpClient.getToken(appKey, appSecret,userid.toString(),username,imgurl,FormatType.json);
			String strResult = sdkresult.getResult();
			//net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(strResult);
			log.error("[chartroom-token],result["+strResult+"]");
			List<String> tmplist = new ArrayList<String>();
			tmplist.add(rooms);
			sdkresult = ApiHttpClient.publishChatroomMessage(appKey, appSecret, userid.toString(), tmplist, mess, FormatType.json);
			log.error("[chartroom-send],result["+strResult+"]");
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		SendMsgToChartRoom r = new SendMsgToChartRoom(Integer.valueOf(26), "26", "http://cache.xyungame.net/base/2016/12/14/e9ed27de-968e-4cc1-9572-de6bb0ae5667.jpeg", "1000-【大小玩法-大】", "10001", "", "");
		ExcutorUtil.exec(r);
		ExcutorUtil.pool.shutdown();
	}
}
