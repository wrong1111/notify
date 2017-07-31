package com.game.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_pay_record")
public class TPayRecordLog extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer id;
	String memno;
	String orderno;
	String noticeurl;
	String httpstatus;
	String request;
	String response;
	Date createtime;
	
	public TPayRecordLog() {
		
	}
	
	public TPayRecordLog(String memno,String orderno,String noticeurl,String httpstatus,String request,String response,Date createtime) {
		this.memno = memno;
		this.orderno = orderno;
		this.noticeurl = noticeurl;
		this.httpstatus = httpstatus;
		this.request  = request;
		this.response = response;
		this.createtime = createtime;
				
	}
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
	public String getHttpstatus() {
		return httpstatus;
	}
	public void setHttpstatus(String httpstatus) {
		this.httpstatus = httpstatus;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

}
