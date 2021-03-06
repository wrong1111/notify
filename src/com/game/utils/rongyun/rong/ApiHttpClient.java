package com.game.utils.rongyun.rong;

import com.game.utils.PropertiesUtil;
import com.game.utils.rongyun.rong.models.ChatroomInfo;
import com.game.utils.rongyun.rong.models.FormatType;
import com.game.utils.rongyun.rong.models.GroupInfo;
import com.game.utils.rongyun.rong.models.Message;
import com.game.utils.rongyun.rong.models.SdkHttpResult;
import com.game.utils.rongyun.rong.util.HttpUtil;
import com.game.utils.rongyun.rong.models.*;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ApiHttpClient {

	private static final String RONGCLOUDURI = PropertiesUtil.getValue("rongyun.url");
	
	private static final String UTF8 = "UTF-8";

	// 获取token
	public static SdkHttpResult getToken(String appKey, String appSecret,
			String userId, String userName, String portraitUri,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil
				.CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
						+ "/user/getToken." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		sb.append("&name=").append(URLEncoder.encode(userName==null?"":userName, UTF8));
		sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri==null?"":portraitUri, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 检查用户在线状态
	public static SdkHttpResult checkOnline(String appKey, String appSecret,
			String userId, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/user/checkOnline." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 刷新用户信息
	public static SdkHttpResult refreshUser(String appKey, String appSecret,
			String userId, String userName, String portraitUri,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/user/refresh." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		if (userName != null) {
			sb.append("&name=").append(URLEncoder.encode(userName, UTF8));
		}
		if (portraitUri != null) {
			sb.append("&portraitUri=").append(
					URLEncoder.encode(portraitUri, UTF8));
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 封禁用户
	public static SdkHttpResult blockUser(String appKey, String appSecret,
			String userId, int minute, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/user/block." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		sb.append("&minute=").append(
				URLEncoder.encode(String.valueOf(minute), UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 解禁用户
	public static SdkHttpResult unblockUser(String appKey, String appSecret,
			String userId, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/user/unblock." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 获取被封禁用户
	public static SdkHttpResult queryBlockUsers(String appKey,
			String appSecret, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/user/block/query." + format.toString());

		return HttpUtil.returnResult(conn);
	}

	// 添加用户到黑名单
	public static SdkHttpResult blackUser(String appKey, String appSecret,
			String userId, List<String> blackUserIds, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/user/blacklist/add." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		if (blackUserIds != null) {
			for (String blackId : blackUserIds) {
				sb.append("&blackUserId=").append(
						URLEncoder.encode(blackId, UTF8));
			}
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 从黑名单移除用户
	public static SdkHttpResult unblackUser(String appKey, String appSecret,
			String userId, List<String> blackUserIds, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/user/blacklist/remove." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		if (blackUserIds != null) {
			for (String blackId : blackUserIds) {
				sb.append("&blackUserId=").append(
						URLEncoder.encode(blackId, UTF8));
			}
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 获取黑名单用户
	public static SdkHttpResult QueryblackUser(String appKey, String appSecret,
			String userId, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/user/blacklist/query." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 创建群
	public static SdkHttpResult createGroup(String appKey, String appSecret,
			List<String> userIds, String groupId, String groupName,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/group/create." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
		sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));
		if (userIds != null) {
			for (String id : userIds) {
				sb.append("&userId=").append(URLEncoder.encode(id, UTF8));
			}
		}
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 加入群
	public static SdkHttpResult joinGroup(String appKey, String appSecret,
			String userId, String groupId, String groupName, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/group/join." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
		sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 批量加入群
	public static SdkHttpResult joinGroupBatch(String appKey, String appSecret,
			List<String> userIds, String groupId, String groupName,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/group/join." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
		sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));
		if (userIds != null) {
			for (String id : userIds) {
				sb.append("&userId=").append(URLEncoder.encode(id, UTF8));
			}
		}
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 退出群
	public static SdkHttpResult quitGroup(String appKey, String appSecret,
			String userId, String groupId, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/group/quit." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 批量退出群
	public static SdkHttpResult quitGroupBatch(String appKey, String appSecret,
			List<String> userIds, String groupId, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/group/quit." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
		if (userIds != null) {
			for (String id : userIds) {
				sb.append("&userId=").append(URLEncoder.encode(id, UTF8));
			}
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 解散群
	public static SdkHttpResult dismissGroup(String appKey, String appSecret,
			String userId, String groupId, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil
				.CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
						+ "/group/dismiss." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 同步用户群信息
	public static SdkHttpResult syncGroup(String appKey, String appSecret,
			String userId, List<GroupInfo> groups, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/group/sync." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8));
		if (groups != null) {
			for (GroupInfo info : groups) {
				if (info != null) {
					sb.append(
							String.format("&group[%s]=",
									URLEncoder.encode(info.getId(), UTF8)))
							.append(URLEncoder.encode(info.getName(), UTF8));
				}
			}
		}
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 刷新群信息
	public static SdkHttpResult refreshGroupInfo(String appKey,
			String appSecret, String groupId, String groupName,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil
				.CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
						+ "/group/refresh." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(URLEncoder.encode(groupId, UTF8));
		sb.append("&groupName=").append(URLEncoder.encode(groupName==null?"":groupName, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 刷新群信息
	public static SdkHttpResult refreshGroupInfo(String appKey,
			String appSecret, GroupInfo group, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil
				.CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
						+ "/group/refresh." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(URLEncoder.encode(group.getId(), UTF8));
		sb.append("&groupName=").append(
				URLEncoder.encode(group.getName(), UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 发送消息(push内容为消息内容)
	public static SdkHttpResult publishMessage(String appKey, String appSecret,
			String fromUserId, List<String> toUserIds, Message msg,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/message/private/publish." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
		if (toUserIds != null) {
			for (int i = 0; i < toUserIds.size(); i++) {
				sb.append("&toUserId=").append(
						URLEncoder.encode(toUserIds.get(i), UTF8));
			}
		}
		sb.append("&objectName=")
				.append(URLEncoder.encode(msg.getType(), UTF8));
		sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 发送消息(可传递push内容)
	public static SdkHttpResult publishMessage(String appKey, String appSecret,
			String fromUserId, List<String> toUserIds, Message msg,
			String pushContent, String pushData, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/message/publish." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
		if (toUserIds != null) {
			for (int i = 0; i < toUserIds.size(); i++) {
				sb.append("&toUserId=").append(
						URLEncoder.encode(toUserIds.get(i), UTF8));
			}
		}
		sb.append("&objectName=")
				.append(URLEncoder.encode(msg.getType(), UTF8));
		sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

		if (pushContent != null) {
			sb.append("&pushContent=").append(URLEncoder.encode(pushContent, UTF8));
		}

		if (pushData != null) {
			sb.append("&pushData=").append(URLEncoder.encode(pushData, UTF8));
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 发送系统消息
	public static SdkHttpResult publishSystemMessage(String appKey,
			String appSecret, String fromUserId, List<String> toUserIds,
			Message msg, String pushContent, String pushData, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/message/system/publish." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
		if (toUserIds != null) {
			for (int i = 0; i < toUserIds.size(); i++) {
				sb.append("&toUserId=").append(
						URLEncoder.encode(toUserIds.get(i), UTF8));
			}
		}
		sb.append("&objectName=")
				.append(URLEncoder.encode(msg.getType(), UTF8));
		sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

		if (pushContent != null) {
			sb.append("&pushContent=").append(URLEncoder.encode(pushContent, UTF8));
		}

		if (pushData != null) {
			sb.append("&pushData=").append(URLEncoder.encode(pushData, UTF8));
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 发送群消息
	public static SdkHttpResult publishGroupMessage(String appKey,
			String appSecret, String fromUserId, List<String> toGroupIds,
			Message msg, String pushContent, String pushData, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/message/group/publish." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
		if (toGroupIds != null) {
			for (int i = 0; i < toGroupIds.size(); i++) {
				sb.append("&toGroupId=").append(
						URLEncoder.encode(toGroupIds.get(i), UTF8));
			}
		}
		sb.append("&objectName=")
				.append(URLEncoder.encode(msg.getType(), UTF8));
		sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

		if (pushContent != null) {
			sb.append("&pushContent=").append(URLEncoder.encode(pushContent, UTF8));
		}

		if (pushData != null) {
			sb.append("&pushData=").append(URLEncoder.encode(pushData, UTF8));
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 发送聊天室消息
	public static SdkHttpResult publishChatroomMessage(String appKey,
			String appSecret, String fromUserId, List<String> toChatroomIds,
			Message msg, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil
				.CreatePostHttpConnection(appKey, appSecret, RONGCLOUDURI
						+ "/message/chatroom/publish." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));
		if (toChatroomIds != null) {
			for (int i = 0; i < toChatroomIds.size(); i++) {
				sb.append("&toChatroomId=").append(
						URLEncoder.encode(toChatroomIds.get(i), UTF8));
			}
		}
		sb.append("&objectName=")
				.append(URLEncoder.encode(msg.getType(), UTF8));
		sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}
	
	//发广播消息
	public static SdkHttpResult broadcastMessage(String appKey, String appSecret,
			String fromUserId, Message msg,String pushContent, String pushData, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/message/broadcast." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));		
		sb.append("&objectName=")
				.append(URLEncoder.encode(msg.getType(), UTF8));
		sb.append("&content=").append(URLEncoder.encode(msg.toString(), UTF8));
		if (pushContent != null) {
			sb.append("&pushContent=").append(URLEncoder.encode(pushContent, UTF8));
		}

		if (pushData != null) {
			sb.append("&pushData=").append(URLEncoder.encode(pushData, UTF8));
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 创建聊天室
	public static SdkHttpResult createChatroom(String appKey, String appSecret,
			List<ChatroomInfo> chatrooms, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/chatroom/create." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("1=1");
		if (chatrooms != null) {
			for (ChatroomInfo info : chatrooms) {
				if (info != null) {
					sb.append(
							String.format("&chatroom[%s]=",
									URLEncoder.encode(info.getId(), UTF8)))
							.append(URLEncoder.encode(info.getName(), UTF8));
				}
			}
		}
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 销毁聊天室
	public static SdkHttpResult destroyChatroom(String appKey,
			String appSecret, List<String> chatroomIds, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/chatroom/destroy." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("1=1");
		if (chatroomIds != null) {
			for (String id : chatroomIds) {
				sb.append("&chatroomId=").append(URLEncoder.encode(id, UTF8));
			}
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 查询聊天室信息
	public static SdkHttpResult queryChatroom(String appKey, String appSecret,
			List<String> chatroomIds, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/chatroom/query." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("1=1");
		if (chatroomIds != null) {
			for (String id : chatroomIds) {
				sb.append("&chatroomId=").append(URLEncoder.encode(id, UTF8));
			}
		}

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 获取消息历史记录下载地址
	public static SdkHttpResult getMessageHistoryUrl(String appKey,
			String appSecret, String date, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/message/history." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("date=").append(URLEncoder.encode(date, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	// 删除消息历史记录
	public static SdkHttpResult deleteMessageHistory(String appKey,
			String appSecret, String date, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/message/history/delete." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("date=").append(URLEncoder.encode(date, UTF8));
		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}
	
	// 获取群内成员
	public static SdkHttpResult queryGroupUserList(String appKey,
			String appSecret, String groupId, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/group/user/query." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(
				URLEncoder.encode(groupId == null ? "" : groupId, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}
	
	//添加群成员禁言
	public static SdkHttpResult groupUserGagAdd(String appKey,
			String appSecret, String groupId, String userId, long minute,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/group/user/gag/add." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(
				URLEncoder.encode(groupId == null ? "" : groupId, UTF8));
		sb.append("&userId=").append(
				URLEncoder.encode(userId == null ? "" : userId, UTF8));
		sb.append("&minute=").append(
				URLEncoder.encode(String.valueOf(minute), UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	//移除禁言群成员
	public static SdkHttpResult groupUserGagRollback(String appKey,
			String appSecret, String groupId, String userId, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/group/user/gag/rollback." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(
				URLEncoder.encode(groupId == null ? "" : groupId, UTF8));
		sb.append("&userId=").append(
				URLEncoder.encode(userId == null ? "" : userId, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	//查询被禁言的群成员
	public static SdkHttpResult groupUserGagList(String appKey,
			String appSecret, String groupId, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/group/user/gag/list." + format.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("groupId=").append(
				URLEncoder.encode(groupId == null ? "" : groupId, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	//添加敏感词
	public static SdkHttpResult wordFilterAdd(String appKey, String appSecret,
			String word, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/wordfilter/add." + format.toString());

		if (word == null || word.length() == 0) {
			throw new Exception("word is not null or empty.");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("word=").append(
				URLEncoder.encode(word == null ? "" : word, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	//移除敏感词
	public static SdkHttpResult wordFilterDelete(String appKey,
			String appSecret, String word, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/wordfilter/delete." + format.toString());

		if (word == null || word.length() == 0) {
			throw new Exception("word is not null or empty.");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("word=").append(
				URLEncoder.encode(word == null ? "" : word, UTF8));

		HttpUtil.setBodyParameter(sb, conn);

		return HttpUtil.returnResult(conn);
	}

	//查询敏感词
	public static SdkHttpResult wordFilterList(String appKey, String appSecret,
			FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret,
				RONGCLOUDURI + "/wordfilter/list." + format.toString());
		StringBuilder sb = new StringBuilder();
		sb.append("1=1");
		HttpUtil.setBodyParameter(sb, conn);
		return HttpUtil.returnResult(conn);
	}

	//发送不落地push
	public static SdkHttpResult push(String appKey, String appSecret,
			PushMessage message, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreateJsonPostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/push." + format.toString());

		HttpUtil.setBodyParameter(message.toString(), conn);
		return HttpUtil.returnResult(conn);
	}
	
	//给用户打标签
	public static SdkHttpResult setUserTag(String appKey, String appSecret,
			UserTag tag, FormatType format) throws Exception {

		HttpURLConnection conn = HttpUtil.CreateJsonPostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/user/tag/set." + format.toString());

		HttpUtil.setBodyParameter(tag.toString(), conn);
		return HttpUtil.returnResult(conn);
	}

	/**
	 * 获取聊天室内的成员列表
	 * @param appKey
	 * @param appSecret
	 * @param rootid
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static SdkHttpResult getUserList(String appKey,String appSecret,String rootid,FormatType format) throws Exception{
		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
				appSecret, RONGCLOUDURI + "/chatroom/user/query.json");
		StringBuilder sb = new StringBuilder();
		sb.append("chatroomId=").append(URLEncoder.encode(rootid == null ? "" : rootid, UTF8)).append("&count=400&order=2");
		HttpUtil.setBodyParameter(sb,conn);
		return HttpUtil.returnResult(conn);
	}
	
	///聊天室成员禁言服务chatroom/user/gag/add
	public static SdkHttpResult chartroomGagAdd(String appKey,
			String appSecret, String userId,String roomid,Integer min, FormatType format)
			throws Exception {

		HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,appSecret,RONGCLOUDURI + "/chatroom/user/gag/add." + format.toString());
		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(URLEncoder.encode(userId, UTF8)).append("&chatroomId=").append(URLEncoder.encode(roomid == null ? "" : roomid, UTF8)).append("&minute="+(min == null ? "10" : min));
		HttpUtil.setBodyParameter(sb, conn);
		return HttpUtil.returnResult(conn);
	}
	///聊天室成员移除服务/chatroom/user/gag/rollback
		public static SdkHttpResult chartroomGagRollback(String appKey,
				String appSecret, String userId,String roomid, FormatType format)
				throws Exception {

			HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
					appSecret,
					RONGCLOUDURI + "/chatroom/user/gag/rollback." + format.toString());

			StringBuilder sb = new StringBuilder();
			sb.append("userId=").append(URLEncoder.encode(userId, UTF8)).append("&chatroomId=").append(URLEncoder.encode(roomid == null ? "" : roomid, UTF8));
			HttpUtil.setBodyParameter(sb, conn);

			return HttpUtil.returnResult(conn);
		}
		
		///聊天室成员禁言用户服务//chatroom/user/gag/list
			public static SdkHttpResult chartroomGaglist(String appKey,
					String appSecret, String roomid, FormatType format)
					throws Exception {
				HttpURLConnection conn = HttpUtil.CreatePostHttpConnection(appKey,
						appSecret,
						RONGCLOUDURI + "/chatroom/user/gag/list." + format.toString());
				StringBuilder sb = new StringBuilder();
				sb.append("chatroomId=").append(URLEncoder.encode(roomid == null ? "" : roomid, UTF8));
				HttpUtil.setBodyParameter(sb, conn);

				return HttpUtil.returnResult(conn);
			}
	public static void main(String[] args) {
		//创建聊天室
		ApiHttpClient client = new ApiHttpClient();
		String appKey ="n19jmcy59uen9"; //"p5tvi9dstu4p4";
		String appSecret ="D8FjiuECjQwWx";//"Z65k66h1xou";
		List<ChatroomInfo> chatrooms = new ArrayList<ChatroomInfo>();
//		ChatroomInfo room = new ChatroomInfo("10008","测试聊天室");
//		chatrooms.add(room);
		SdkHttpResult result = null;
		try {
			//result = client.createChatroom(appKey, appSecret, chatrooms, FormatType.json);
//			  result = client.checkOnline(appKey, appSecret,"26", FormatType.json);
//			System.out.println(result.getResult());
//			result = client.getToken(appKey, appSecret, "26", "test29","http://cache.xyungame.net/base/2016/12/14/e9ed27de-968e-4cc1-9572-de6bb0ae5667.jpeg", FormatType.json );
//			System.out.println(result.getResult());
			//result = client.chartroomGagRollback(appKey, appSecret, "106265", "10007",FormatType.json );
			
			result = client.chartroomGagAdd(appKey, appSecret, "106265", "10007", Integer.valueOf(100), FormatType.json);
			System.out.println(result.getResult());
//			List<String> blist =new ArrayList<String>();
//			blist.add("1001");
//			blist.add("10000");
//			SdkHttpResult result = client.queryChatroom(appKey, appSecret,blist, FormatType.json);
//			System.out.println(result.getResult());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
