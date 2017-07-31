package com.game.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayVo implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal money;
	private String channel;
	private String orderno;
	private String noticeurl;
	private String noticepageurl;
	private String paychannel;
	private Date createtime;
	private String requestip;
	private String memno;
	
	
	///===
	private String qrcode;
	private String qrcodeurl;
	private Integer noticetimes;
	private Date noticedatetime;
	private Date noticelastdatetime;
	private String noticestr;
	
	private String result;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getQrcodeurl() {
		return qrcodeurl;
	}
	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}
	public Integer getNoticetimes() {
		return noticetimes;
	}
	public void setNoticetimes(Integer noticetimes) {
		this.noticetimes = noticetimes;
	}
	public Date getNoticedatetime() {
		return noticedatetime;
	}
	public void setNoticedatetime(Date noticedatetime) {
		this.noticedatetime = noticedatetime;
	}
	public Date getNoticelastdatetime() {
		return noticelastdatetime;
	}
	public void setNoticelastdatetime(Date noticelastdatetime) {
		this.noticelastdatetime = noticelastdatetime;
	}
	public String getNoticestr() {
		return noticestr;
	}
	public void setNoticestr(String noticestr) {
		this.noticestr = noticestr;
	}
	public String getMemno() {
		return memno;
	}
	public void setMemno(String memno) {
		this.memno = memno;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getRequestip() {
		return requestip;
	}
	public void setRequestip(String requestip) {
		this.requestip = requestip;
	}
	
	
	
	public String getPaychannel() {
		return paychannel;
	}
	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getNoticeurl() {
		return noticeurl;
	}
	public void setNoticeurl(String noticeurl) {
		this.noticeurl = noticeurl;
	}
	public String getNoticepageurl() {
		return noticepageurl;
	}
	public void setNoticepageurl(String noticepageurl) {
		this.noticepageurl = noticepageurl;
	}
	
	
}
