package com.game.service;

import java.util.Date;
import java.util.Map;

import com.game.entity.TPayRecord;
import com.game.utils.common.dao.support.Page;

public interface PayService {

	public Map<String, String> findLastPayCompanyCount(Date nowdate,String companycode);
	
	public TPayRecord createTPayRecord(TPayRecord record);
	
	public void updateTPayRecord(TPayRecord record);
	
	public Page findNotify2Send(TPayRecord record,int page ,int pagesize);
}
