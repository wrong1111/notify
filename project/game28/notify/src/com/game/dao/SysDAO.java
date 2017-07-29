package com.game.dao;

import java.util.List;

import com.game.entity.TSysConfig;
import com.game.entity.TSysPartner;
import com.game.utils.common.exception.DaoException;

public interface SysDAO {

	public TSysConfig findByKey(String key)throws DaoException;
	public List<TSysPartner> findAll() throws DaoException;
		 
}
