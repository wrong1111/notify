package com.game.utils.compay;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author wrong1111
 * 支付基类
 */
public class BasePay implements Serializable,Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String tradeno;//本站支付订单号
	String out_pay_no ;//第三方支付订单号
	String bank_no;//第三方支付银行流水号;
	String bank_code;//第三方支付银行流水;
	String platcode;//支付平台方式，微信?支付宝?快捷微信支付?
	String paycode;//本站定义的充值类型定义
	BigDecimal  paymoney;//支付金额
	BigDecimal  freemoney;//支付手续费
	
	public String getTradeno() {
		return tradeno;
	}
	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}
	public String getOut_pay_no() {
		return out_pay_no;
	}
	public void setOut_pay_no(String out_pay_no) {
		this.out_pay_no = out_pay_no;
	}
	public String getBank_no() {
		return bank_no;
	}
	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getPlatcode() {
		return platcode;
	}
	public void setPlatcode(String platcode) {
		this.platcode = platcode;
	}
	public BigDecimal getPaymoney() {
		return paymoney;
	}
	public void setPaymoney(BigDecimal paymoney) {
		this.paymoney = paymoney;
	}
	public BigDecimal getFreemoney() {
		return freemoney;
	}
	public void setFreemoney(BigDecimal freemoney) {
		this.freemoney = freemoney;
	}
	
	

}
