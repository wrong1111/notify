package com.game.pojo;

import java.math.BigDecimal;

public class MembaseVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1789119986518346450L;
	String username;//用户名
	String code;//验证码
	String pwd;//密码
	String pwd2;//密码2
	String requesturl;//请求前URL
	String requestip;//请求IP
	String channel;//推荐渠道
	String channelno;//推荐用户id
	
	String mobile;
	String newpwd;
	String newpwd2;
	String signstr;
	String cardno;
	String sign;
	String state;
	
	String userid;
	int errorcount;
	boolean bindflag = false;
	
	String complete;
	String opentown;
	String bankno;
	
	String realname;
	String imgurl;
	String nickname;
	String birthday;
	String sex;
	String provice;
	String city;
	String address;
	
	String qq;
	String chartim;
	String uid;
	String openid;
	String type;
	String userlevel;
	BigDecimal money;
	String alipay;
	String rytoken;
	
	String acceptuser;
	String givemoney;
	
    int validmobile=0;
    String chatflag;
    Integer popuflag;
    Integer favflag;
    Integer bindtype = 0;
    
    private String wxqrcode;
    private String aliqrcode;
    
    public String getOpentown() {
		return opentown;
	}
	public void setOpentown(String opentown) {
		this.opentown = opentown;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPwd2() {
		return pwd2;
	}
	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}
	public String getRequesturl() {
		return requesturl;
	}
	public void setRequesturl(String requesturl) {
		this.requesturl = requesturl;
	}
	public String getRequestip() {
		return requestip;
	}
	public void setRequestip(String requestip) {
		this.requestip = requestip;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannelno() {
		return channelno;
	}
	public void setChannelno(String channelno) {
		this.channelno = channelno;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNewpwd() {
		return newpwd;
	}
	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}
	public String getNewpwd2() {
		return newpwd2;
	}
	public void setNewpwd2(String newpwd2) {
		this.newpwd2 = newpwd2;
	}
	public String getSignstr() {
		return signstr;
	}
	public void setSignstr(String signstr) {
		this.signstr = signstr;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getErrorcount() {
		return errorcount;
	}
	public void setErrorcount(int errorcount) {
		this.errorcount = errorcount;
	}
	public boolean isBindflag() {
		return bindflag;
	}
	public void setBindflag(boolean bindflag) {
		this.bindflag = bindflag;
	}
	public String getComplete() {
		return complete;
	}
	public void setComplete(String complete) {
		this.complete = complete;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	 
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserlevel() {
		return userlevel;
	}
	public void setUserlevel(String userlevel) {
		this.userlevel = userlevel;
	}
	public int getValidmobile() {
		return validmobile;
	}
	public void setValidmobile(int validmobile) {
		this.validmobile = validmobile;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getChartim() {
		return chartim;
	}
	public void setChartim(String chartim) {
		this.chartim = chartim;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getAlipay() {
		return alipay;
	}
	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}
	public String getProvice() {
		return provice;
	}
	public void setProvice(String provice) {
		this.provice = provice;
	}
	public String getRytoken() {
		return rytoken;
	}
	public void setRytoken(String rytoken) {
		this.rytoken = rytoken;
	}
	 
	public String getGivemoney() {
		return givemoney;
	}
	public void setGivemoney(String givemoney) {
		this.givemoney = givemoney;
	}
	public String getAcceptuser() {
		return acceptuser;
	}
	public void setAcceptuser(String acceptuser) {
		this.acceptuser = acceptuser;
	}
	public String getChatflag() {
		return chatflag;
	}
	public void setChatflag(String chatflag) {
		this.chatflag = chatflag;
	}
	public Integer getPopuflag() {
		return popuflag;
	}
	public void setPopuflag(Integer popuflag) {
		this.popuflag = popuflag;
	}
	public Integer getFavflag() {
		return favflag;
	}
	public void setFavflag(Integer favflag) {
		this.favflag = favflag;
	}
	public Integer getBindtype() {
		return bindtype;
	}
	public void setBindtype(Integer bindtype) {
		this.bindtype = bindtype;
	}
	public String getWxqrcode() {
		return wxqrcode;
	}
	public void setWxqrcode(String wxqrcode) {
		this.wxqrcode = wxqrcode;
	}
	public String getAliqrcode() {
		return aliqrcode;
	}
	public void setAliqrcode(String aliqrcode) {
		this.aliqrcode = aliqrcode;
	}
    
    
}
