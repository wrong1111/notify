package com.game.utils.sms;


public interface ISend {
	
	/**
	 * 短信发送接口
	 * @param m 手机号以","分隔
	 * @param c 短信内容。控制在70字以内。
	 * @param i 发送次数
	 * @return
	 */
	public String send(int i,String m,String c);
	
	/**
	 * 查询余额
	 * @param data
	 * @return
	 */
	public String blance(int c);

}
