package com.game.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_pay_record")
public class TPayRecord extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer id;
	String memno;
	BigDecimal money;
	String channel;
	String orderno;
	String paystr;
	String returnpaystr;
	String qrcode;
	String qrcodeurl;
	String noticeurl;
	String rquestid;
	Date paytime;
	String payresult;
	String retunpaystr;
	String returncode;
	Date returnpaytime;
	String returnpayresult;

	Integer noticetimes;
	Date noticedatetime;
	Date noticelastdatetime;
	String  noticeresult;
	String noticestr;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMemno() {
		return memno;
	}
	public void setMemno(String memno) {
		this.memno = memno;
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
	public String getPaystr() {
		return paystr;
	}
	public void setPaystr(String paystr) {
		this.paystr = paystr;
	}
	public String getReturnpaystr() {
		return returnpaystr;
	}
	public void setReturnpaystr(String returnpaystr) {
		this.returnpaystr = returnpaystr;
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
	public String getNoticeurl() {
		return noticeurl;
	}
	public void setNoticeurl(String noticeurl) {
		this.noticeurl = noticeurl;
	}
	public String getRquestid() {
		return rquestid;
	}
	public void setRquestid(String rquestid) {
		this.rquestid = rquestid;
	}
	public Date getPaytime() {
		return paytime;
	}
	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	public String getPayresult() {
		return payresult;
	}
	public void setPayresult(String payresult) {
		this.payresult = payresult;
	}
	public String getRetunpaystr() {
		return retunpaystr;
	}
	public void setRetunpaystr(String retunpaystr) {
		this.retunpaystr = retunpaystr;
	}
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	public Date getReturnpaytime() {
		return returnpaytime;
	}
	public void setReturnpaytime(Date returnpaytime) {
		this.returnpaytime = returnpaytime;
	}
	public String getReturnpayresult() {
		return returnpayresult;
	}
	public void setReturnpayresult(String returnpayresult) {
		this.returnpayresult = returnpayresult;
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
	public String getNoticeresult() {
		return noticeresult;
	}
	public void setNoticeresult(String noticeresult) {
		this.noticeresult = noticeresult;
	}
	public String getNoticestr() {
		return noticestr;
	}
	public void setNoticestr(String noticestr) {
		this.noticestr = noticestr;
	}
	
	

}
