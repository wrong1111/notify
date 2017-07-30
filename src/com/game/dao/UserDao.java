package com.game.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.game.entity.TPayRecord;
import com.game.utils.common.dao.IEntityDao;
import com.game.utils.common.dao.support.Page;

public interface UserDao extends IEntityDao<Serializable>{
	
	public Map<String, String> findLastPayCompanyCount(Date lastTime,String compancode);
	
	public TPayRecord findByOrder(String orderno);
	
	public Page findNotify2Send(TPayRecord record,int page ,int pagesize);
	
}
