package com.game.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the t_sys_partner database table.
 * 
 */
@Entity
@Table(name="t_sys_partner")
@NamedQuery(name="TSysPartner.findAll", query="SELECT t FROM TSysPartner t")
public class TSysPartner implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String connect;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endtime;

	private String name;

	private String partnername;

	private String partnerpasswd;

	private String signestring;

	private String status;

	public TSysPartner() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConnect() {
		return this.connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	public Date getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartnername() {
		return this.partnername;
	}

	public void setPartnername(String partnername) {
		this.partnername = partnername;
	}

	public String getPartnerpasswd() {
		return this.partnerpasswd;
	}

	public void setPartnerpasswd(String partnerpasswd) {
		this.partnerpasswd = partnerpasswd;
	}

	public String getSignestring() {
		return this.signestring;
	}

	public void setSignestring(String signestring) {
		this.signestring = signestring;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}