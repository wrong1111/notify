package com.game.pojo;

import java.io.Serializable;
import java.util.Date;

public class NotifyVo implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer notifyId;

	private Date createTime;
	/** 最后一次通知时间 **/
	private Date lastNotifyTime;

	/** 通知次数 **/
	private Integer notifyTimes = 0;

	/** 限制通知次数 **/
	private Integer limitNotifyTimes = 5;

	/** 通知URL **/
	private String url;

	/** 商户编号 **/
	private String merchantNo;
	
	/**交易指定编号*/
	private String memno;

	/** 商户订单号 **/
	private String merchantOrderNo;

	/** 状态 **/
	private String status;

	/** 通知字符串 */
	private String noticestr;

	/** 交易结果描述 **/
	private String responseDesc;

	public NotifyVo() {
	}

	public NotifyVo(Date createTime, Date lastNotifyTime, Integer notifyTimes, Integer limitNotifyTimes, String url,
			String merchantNo, String merchantOrderNo, String status,String noticestr,String responseDesc) {
		this.createTime = createTime;
		this.lastNotifyTime = lastNotifyTime;
		this.notifyTimes = notifyTimes;
		this.limitNotifyTimes = limitNotifyTimes;
		this.url = url;
		this.merchantNo = merchantNo;
		this.merchantOrderNo = merchantOrderNo;
		this.status = status;
		this.noticestr = noticestr;
		this.responseDesc = responseDesc;
	}

	/** 最后一次通知时间 **/
	public Date getLastNotifyTime() {
		return lastNotifyTime;
	}

	/** 最后一次通知时间 **/
	public void setLastNotifyTime(Date lastNotifyTime) {
		this.lastNotifyTime = lastNotifyTime;
	}

	/** 通知次数 **/
	public Integer getNotifyTimes() {
		return notifyTimes;
	}

	/** 通知次数 **/
	public void setNotifyTimes(Integer notifyTimes) {
		this.notifyTimes = notifyTimes;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/** 限制通知次数 **/
	public Integer getLimitNotifyTimes() {
		return limitNotifyTimes;
	}

	/** 限制通知次数 **/
	public void setLimitNotifyTimes(Integer limitNotifyTimes) {
		this.limitNotifyTimes = limitNotifyTimes;
	}

	/** 通知URL **/
	public String getUrl() {
		return url;
	}

	/** 通知URL **/
	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	/** 商户编号 **/
	public String getMerchantNo() {
		return merchantNo;
	}

	/** 商户编号 **/
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo == null ? null : merchantNo.trim();
	}

	/** 商户订单号 **/
	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	/** 商户订单号 **/
	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo == null ? null : merchantOrderNo.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNoticestr() {
		return noticestr;
	}

	public void setNoticestr(String noticestr) {
		this.noticestr = noticestr;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public Integer getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Integer notifyId) {
		this.notifyId = notifyId;
	}

	public String getMemno() {
		return memno;
	}

	public void setMemno(String memno) {
		this.memno = memno;
	}

}
