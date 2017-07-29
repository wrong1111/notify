package com.game.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the t_mem_money_record database table.
 * 
 */
@Entity
@Table(name="t_mem_money_record")
@NamedQuery(name="TMemMoneyRecord.findAll", query="SELECT t FROM TMemMoneyRecord t")
public class TMemMoneyRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private BigDecimal addmoney;

	private String bankcode;

	private String bankname;

	private String bankno;

	@Temporal(TemporalType.TIMESTAMP)
	private Date builddate;

	private String buildsrc;

	private String bulidcode;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createtime;

	private String failmsg;

	private String flag;

	private BigDecimal money;

	@Temporal(TemporalType.TIMESTAMP)
	private Date optdate;

	private String optuserid;

	private String platcode;

	private String realname;

	private String tradeno;

	private String type;

	private Integer userid;

	
	@Transient
	private String title;
	
	
	public TMemMoneyRecord() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAddmoney() {
		return this.addmoney;
	}

	public void setAddmoney(BigDecimal addmoney) {
		this.addmoney = addmoney;
	}

	public String getBankcode() {
		return this.bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getBankname() {
		return this.bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankno() {
		return this.bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public Date getBuilddate() {
		return this.builddate;
	}

	public void setBuilddate(Date builddate) {
		this.builddate = builddate;
	}

	public String getBuildsrc() {
		return this.buildsrc;
	}

	public void setBuildsrc(String buildsrc) {
		this.buildsrc = buildsrc;
	}

	public String getBulidcode() {
		return this.bulidcode;
	}

	public void setBulidcode(String bulidcode) {
		this.bulidcode = bulidcode;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getFailmsg() {
		return this.failmsg;
	}

	public void setFailmsg(String failmsg) {
		this.failmsg = failmsg;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public BigDecimal getMoney() {
		return this.money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getOptdate() {
		return this.optdate;
	}

	public void setOptdate(Date optdate) {
		this.optdate = optdate;
	}

	public String getOptuserid() {
		return this.optuserid;
	}

	public void setOptuserid(String optuserid) {
		this.optuserid = optuserid;
	}

	public String getPlatcode() {
		return this.platcode;
	}

	public void setPlatcode(String platcode) {
		this.platcode = platcode;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getTradeno() {
		return this.tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
	@Transient
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}