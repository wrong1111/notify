package com.game.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ecs_order_info")
public class EcsOrderInfo extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer order_id;
	private String order_sn;
	private Integer user_id;
	private Integer order_status;
	private Integer shipping_status;
	private Integer pay_status;
	private BigDecimal money_paid;
	private Integer pay_time;
	private Integer shipping_time;
	private String invoice_no;
	private BigDecimal order_amount;
	private String consignee;
	private Integer country;
	private Integer province;
	private Integer city;
	private Integer district;
	private String address;
	private String zipcode;
	private String tel;
	private String mobile;
	private String email;
	private String best_time;
	private String sign_building;
	private String postscript;
	private Integer shipping_id;
	private String shipping_name;
	String pay_name;
	Integer pay_id;
	String how_oos;
	String how_surplus;
	String pack_name;
	String card_name;
	String card_message;
	String inv_payee;
	String inv_content;
	BigDecimal goods_amount;
	BigDecimal shipping_fee;
	BigDecimal insure_fee;
	BigDecimal pay_fee;
	BigDecimal pack_fee;
	BigDecimal card_fee;
	BigDecimal goods_discount_fee;
	BigDecimal surplus;
	Integer integral;
	BigDecimal integral_money;
	BigDecimal bonus;
	Integer from_ad;
	String referer;
	Integer add_time;
	Integer confirm_time;
	Integer pack_id;
	Integer card_id;
	Integer bonus_id;
	String extension_code;
	Integer extension_id;
	String to_buyer;
	String pay_note;
	Integer agency_id;
	String inv_type;
	BigDecimal tax;
	Integer is_separate;
	Integer parent_id;
	BigDecimal discount;
	Boolean callback_status;
	Integer lastmodify;
	
	
	
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getOrder_status() {
		return order_status;
	}
	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}
	public Integer getShipping_status() {
		return shipping_status;
	}
	public void setShipping_status(Integer shipping_status) {
		this.shipping_status = shipping_status;
	}
	public Integer getPay_status() {
		return pay_status;
	}
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}
	public BigDecimal getMoney_paid() {
		return money_paid;
	}
	public void setMoney_paid(BigDecimal money_paid) {
		this.money_paid = money_paid;
	}
	public Integer getPay_time() {
		return pay_time;
	}
	public void setPay_time(Integer pay_time) {
		this.pay_time = pay_time;
	}
	public Integer getShipping_time() {
		return shipping_time;
	}
	public void setShipping_time(Integer shipping_time) {
		this.shipping_time = shipping_time;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public BigDecimal getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public Integer getCountry() {
		return country;
	}
	public void setCountry(Integer country) {
		this.country = country;
	}
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public Integer getDistrict() {
		return district;
	}
	public void setDistrict(Integer district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBest_time() {
		return best_time;
	}
	public void setBest_time(String best_time) {
		this.best_time = best_time;
	}
	public String getSign_building() {
		return sign_building;
	}
	public void setSign_building(String sign_building) {
		this.sign_building = sign_building;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public Integer getShipping_id() {
		return shipping_id;
	}
	public void setShipping_id(Integer shipping_id) {
		this.shipping_id = shipping_id;
	}
	public String getShipping_name() {
		return shipping_name;
	}
	public void setShipping_name(String shipping_name) {
		this.shipping_name = shipping_name;
	}
	public String getPay_name() {
		return pay_name;
	}
	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}
	public Integer getPay_id() {
		return pay_id;
	}
	public void setPay_id(Integer pay_id) {
		this.pay_id = pay_id;
	}
	public String getHow_oos() {
		return how_oos;
	}
	public void setHow_oos(String how_oos) {
		this.how_oos = how_oos;
	}
	public String getHow_surplus() {
		return how_surplus;
	}
	public void setHow_surplus(String how_surplus) {
		this.how_surplus = how_surplus;
	}
	public String getPack_name() {
		return pack_name;
	}
	public void setPack_name(String pack_name) {
		this.pack_name = pack_name;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	public String getCard_message() {
		return card_message;
	}
	public void setCard_message(String card_message) {
		this.card_message = card_message;
	}
	public String getInv_payee() {
		return inv_payee;
	}
	public void setInv_payee(String inv_payee) {
		this.inv_payee = inv_payee;
	}
	public String getInv_content() {
		return inv_content;
	}
	public void setInv_content(String inv_content) {
		this.inv_content = inv_content;
	}
	public BigDecimal getGoods_amount() {
		return goods_amount;
	}
	public void setGoods_amount(BigDecimal goods_amount) {
		this.goods_amount = goods_amount;
	}
	public BigDecimal getShipping_fee() {
		return shipping_fee;
	}
	public void setShipping_fee(BigDecimal shipping_fee) {
		this.shipping_fee = shipping_fee;
	}
	public BigDecimal getInsure_fee() {
		return insure_fee;
	}
	public void setInsure_fee(BigDecimal insure_fee) {
		this.insure_fee = insure_fee;
	}
	public BigDecimal getPay_fee() {
		return pay_fee;
	}
	public void setPay_fee(BigDecimal pay_fee) {
		this.pay_fee = pay_fee;
	}
	public BigDecimal getPack_fee() {
		return pack_fee;
	}
	public void setPack_fee(BigDecimal pack_fee) {
		this.pack_fee = pack_fee;
	}
	public BigDecimal getCard_fee() {
		return card_fee;
	}
	public void setCard_fee(BigDecimal card_fee) {
		this.card_fee = card_fee;
	}
	public BigDecimal getGoods_discount_fee() {
		return goods_discount_fee;
	}
	public void setGoods_discount_fee(BigDecimal goods_discount_fee) {
		this.goods_discount_fee = goods_discount_fee;
	}
	public BigDecimal getSurplus() {
		return surplus;
	}
	public void setSurplus(BigDecimal surplus) {
		this.surplus = surplus;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public BigDecimal getIntegral_money() {
		return integral_money;
	}
	public void setIntegral_money(BigDecimal integral_money) {
		this.integral_money = integral_money;
	}
	public BigDecimal getBonus() {
		return bonus;
	}
	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}
	public Integer getFrom_ad() {
		return from_ad;
	}
	public void setFrom_ad(Integer from_ad) {
		this.from_ad = from_ad;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public Integer getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Integer add_time) {
		this.add_time = add_time;
	}
	public Integer getConfirm_time() {
		return confirm_time;
	}
	public void setConfirm_time(Integer confirm_time) {
		this.confirm_time = confirm_time;
	}
	public Integer getPack_id() {
		return pack_id;
	}
	public void setPack_id(Integer pack_id) {
		this.pack_id = pack_id;
	}
	public Integer getCard_id() {
		return card_id;
	}
	public void setCard_id(Integer card_id) {
		this.card_id = card_id;
	}
	public Integer getBonus_id() {
		return bonus_id;
	}
	public void setBonus_id(Integer bonus_id) {
		this.bonus_id = bonus_id;
	}
	public String getExtension_code() {
		return extension_code;
	}
	public void setExtension_code(String extension_code) {
		this.extension_code = extension_code;
	}
	public Integer getExtension_id() {
		return extension_id;
	}
	public void setExtension_id(Integer extension_id) {
		this.extension_id = extension_id;
	}
	public String getTo_buyer() {
		return to_buyer;
	}
	public void setTo_buyer(String to_buyer) {
		this.to_buyer = to_buyer;
	}
	public String getPay_note() {
		return pay_note;
	}
	public void setPay_note(String pay_note) {
		this.pay_note = pay_note;
	}
	public Integer getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(Integer agency_id) {
		this.agency_id = agency_id;
	}
	public String getInv_type() {
		return inv_type;
	}
	public void setInv_type(String inv_type) {
		this.inv_type = inv_type;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public Integer getIs_separate() {
		return is_separate;
	}
	public void setIs_separate(Integer is_separate) {
		this.is_separate = is_separate;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Boolean getCallback_status() {
		return callback_status;
	}
	public void setCallback_status(Boolean callback_status) {
		this.callback_status = callback_status;
	}
	public Integer getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(Integer lastmodify) {
		this.lastmodify = lastmodify;
	}
	
	
	
	
	
	
	

}
