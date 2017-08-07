package com.game.serverutil.excutor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.entity.TPayRecordLog;
import com.game.pojo.NotifyVo;
import com.game.service.PayService;

public class NotifyTask implements Runnable, Delayed {

	Logger logger = Logger.getLogger(getClass());

	private long executeTime;
	private NotifyVo notifyRecord;
	private NotifyQueue notifyQueue;
	private NotifyParam notifyParam;
	private PayService payService;

	public NotifyTask(PayService payService, NotifyVo notifyRecord, NotifyQueue notifyQueue, NotifyParam notifyParam) {
		this.notifyRecord = notifyRecord;
		this.notifyQueue = notifyQueue;
		this.notifyParam = notifyParam;
		this.payService = payService;
		this.executeTime = getExecuteTime(notifyRecord);
	}

	private long getExecuteTime(NotifyVo record) {
		long lastTime = record.getLastNotifyTime().getTime();
		Integer nextNotifyTime = notifyParam.getNotifyParams().get(record.getNotifyTimes());
		return (nextNotifyTime == null ? 0 : nextNotifyTime * 1000) + lastTime;
	}

	@Override
	public int compareTo(Delayed o) {
		NotifyTask task = (NotifyTask) o;
		return executeTime > task.executeTime ? 1 : (executeTime < task.executeTime ? -1 : 0);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(executeTime - System.currentTimeMillis(), unit.SECONDS);
	}

	@Override
	public void run() {
		// 得到当前通知对象的通知次数
		Integer notifyTimes = notifyRecord.getNotifyTimes();
		String responseMsg = "";
		HttpPost httppost = null;
		CloseableHttpClient httpclient = null;
		// 去通知
		try {
			logger.info("notify-thread-Notify Url " + notifyRecord.getUrl() + " ;notify orderno:"
					+ notifyRecord.getMerchantOrderNo() + ";notify times:" + notifyRecord.getNotifyTimes());

			/** 采用 httpClient */
			StringBuilder sb = new StringBuilder();
			httpclient = HttpClientBuilder.create().build();
			httppost = new HttpPost(notifyRecord.getUrl());
			// 配置请求的超时设置
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(20000)
					.setConnectTimeout(20000).setSocketTimeout(20000).build();
			httppost.setConfig(requestConfig);
			httppost.addHeader("Content-Type", "application/json;charset=utf-8");
			int responseStatus = 0;

			
			httppost.setEntity(new StringEntity(notifyRecord.getNoticestr(), "utf-8"));
			CloseableHttpResponse response = httpclient.execute(httppost);
			responseStatus = response.getStatusLine().getStatusCode();
			if (responseStatus == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedInputStream instream = new BufferedInputStream(entity.getContent());
				byte[] chars = new byte[2048];
				int len = 0;
				while ((len = instream.read(chars)) != -1) {
					sb.append(new String(chars, 0, len, "utf-8"));
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info(notifyRecord.getUrl() + ",statusok[" + responseStatus + "]result[" + sb.toString() + "]");
			}

			notifyRecord.setNotifyTimes(notifyTimes + 1);
			String successValue = notifyParam.getSuccessValue();
			String result = sb.toString();

			// 得到返回状态，如果是200，也就是通知成功
			if (result != null
					&& (responseStatus == 200 || responseStatus == 201 || responseStatus == 202 || responseStatus == 203
							|| responseStatus == 204 || responseStatus == 205 || responseStatus == 206)) {
				responseMsg = result;
				responseMsg = responseMsg.length() >= 600 ? responseMsg.substring(0, 600) : responseMsg;
				logger.info("orderno： " + notifyRecord.getMerchantOrderNo() + " HTTP_STATUS：" + responseStatus
						+ "请求返回信息：" + responseMsg);
				// 通知成功
				if (responseMsg.trim().equals(successValue)) {
					payService.updateNotify(notifyRecord.getMerchantOrderNo(), notifyRecord.getNotifyTimes(),
							notifyRecord.getLastNotifyTime(), "SUCCESS");
				} else {
					notifyQueue.addElementToList(notifyRecord);
					payService.updateNotify(notifyRecord.getMerchantOrderNo(), notifyRecord.getNotifyTimes(),
							notifyRecord.getLastNotifyTime(), "HTTPOK");
				}
				if (logger.isInfoEnabled()) {
					logger.info("notify Update NotifyRecord:" + JSONObject.toJSONString(notifyRecord) + ";responseMsg:"
							+ responseMsg);
				}
			} else {
				notifyQueue.addElementToList(notifyRecord);
				// 再次放到通知列表中，由添加程序判断是否已经通知完毕或者通知失败
				payService.updateNotify(notifyRecord.getMerchantOrderNo(), notifyRecord.getNotifyTimes(),
						notifyRecord.getLastNotifyTime(), "HTTPERR");
			}
			// 写通知日志表
			payService.saveRecordLog(
					new TPayRecordLog(notifyRecord.getMemno(), notifyRecord.getMerchantOrderNo(), notifyRecord.getUrl(),
							String.valueOf(responseStatus), notifyRecord.getNoticestr(), responseMsg, new Date()));
			if (logger.isInfoEnabled()) {
				logger.info("notify-Insert NotifyRecordLog, merchantNo:" + notifyRecord.getMerchantNo()
						+ ",merchantOrderNo:" + notifyRecord.getMerchantOrderNo());
			}
		} catch (Exception e) {
			responseMsg = e.getMessage();
			if(responseMsg.length()>450) {
				responseMsg = responseMsg.substring(0,450);
			}
			logger.error("notify-NotifyTask", e);
			notifyQueue.addElementToList(notifyRecord);
			payService.updateNotify(notifyRecord.getMerchantOrderNo(), notifyRecord.getNotifyTimes(),
					notifyRecord.getLastNotifyTime(), "HTTPERR");
			// 写通知日志表
			payService.saveRecordLog(new TPayRecordLog(notifyRecord.getMemno(), notifyRecord.getMerchantOrderNo(),
					notifyRecord.getUrl(), "0", notifyRecord.getNoticestr(), responseMsg, new Date()));
		} finally {
			httppost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
