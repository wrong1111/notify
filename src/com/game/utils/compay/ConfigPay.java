package com.game.utils.compay;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wrong1111
 * 充值方式 配置
 */
public class ConfigPay implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String platcode;//充值平台
	String paycode;//本站定义的充值类型
	BigDecimal  freemoney;//支付手续费
	Integer status;//充值方式是否激活

}
