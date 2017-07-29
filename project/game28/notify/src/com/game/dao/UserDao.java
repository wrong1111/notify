package com.game.dao;

import java.util.Date;
import java.util.Map;

import com.game.utils.common.dao.IEntityDao;

public interface UserDao extends IEntityDao<Integer>{
	
	public Map<String, String> findLastPayCompanyCount(Date lastTime,String compancode);
	
}
