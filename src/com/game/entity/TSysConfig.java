package com.game.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the t_sys_config database table.
 * 
 */
@Entity
@Table(name="t_sys_config")
@NamedQuery(name="TSysConfig.findAll", query="SELECT t FROM TSysConfig t")
public class TSysConfig implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String keyname;

	private String keyvalue;

	private String status;

	public TSysConfig() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeyname() {
		return this.keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

	public String getKeyvalue() {
		return this.keyvalue;
	}

	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}