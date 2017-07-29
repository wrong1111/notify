package com.game.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class StoreVo  implements Comparable<StoreVo> ,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private BigDecimal ordermoney = BigDecimal.ZERO;
	private BigDecimal awardmoney = BigDecimal.ZERO;
	private BigDecimal othermoney = BigDecimal.ZERO;
	private Integer openvalue;
	private String playtype;
	
	public BigDecimal getOrdermoney() {
		return ordermoney;
	}
	public void setOrdermoney(BigDecimal ordermoney) {
		this.ordermoney = ordermoney;
	}
	public BigDecimal getAwardmoney() {
		return awardmoney;
	}
	public void setAwardmoney(BigDecimal awardmoney) {
		this.awardmoney = awardmoney;
	}
	public Integer getOpenvalue() {
		return openvalue;
	}
	public void setOpenvalue(Integer openvalue) {
		this.openvalue = openvalue;
	}
	@Override
	public int compareTo(StoreVo o) {
		if(this.awardmoney ==null){
			this.awardmoney =BigDecimal.ZERO;
		}
		return this.awardmoney.compareTo(o.getAwardmoney() == null? BigDecimal.ZERO:o.getAwardmoney());
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public BigDecimal getOthermoney() {
		return othermoney;
	}
	public void setOthermoney(BigDecimal othermoney) {
		this.othermoney = othermoney;
	}
	public String getPlaytype() {
		return playtype;
	}
	public void setPlaytype(String playtype) {
		this.playtype = playtype;
	}
}
