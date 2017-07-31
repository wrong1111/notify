package com.game.service;

import java.util.Date;
import java.util.Map;

import com.game.entity.TPayRecord;
import com.game.entity.TPayRecordLog;
import com.game.pojo.NotifyVo;
import com.game.utils.common.dao.support.Page;

public interface PayService {

	public Map<String, String> findLastPayCompanyCount(Date nowdate,String companycode);
	
	public TPayRecord createTPayRecord(TPayRecord record);
	
	public void updateTPayRecord(TPayRecord record);
	
	public Page findNotify2Send(String  status,int page ,int pagesize);
	
	/**
	 * @param payvo
	 * 异步通知过来结果修改.
	 * retunpaystr	异步通知记录
		returncode	异步通知结果
		returnpaytime	异步通知时间
		returnpayresult	异步通知处理结果
	 */
	public NotifyVo updatePayReceive(NotifyVo payvo);
	
	public void saveRecordLog(TPayRecordLog log);
	
	public void updateNotify(String orderno,Integer times,Date lasttime,String status);
}
