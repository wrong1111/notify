package com.game.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_sys_seq")
public class TSysSeq implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer seqid;
	private String seqname;
	
	@Column(name="seq_id")
	public Integer getSeqid() {
		return seqid;
	}
	public void setSeqid(Integer seqid) {
		this.seqid = seqid;
	}
	
	@Id
	@Column(name="seq_name")
	public String getSeqname() {
		return seqname;
	}
	public void setSeqname(String seqname) {
		this.seqname = seqname;
	}

}
