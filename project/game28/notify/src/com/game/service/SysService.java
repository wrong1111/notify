package com.game.service;

import java.util.Map;

import com.game.entity.TSysConfig;
import com.game.entity.TSysPartner;
import com.game.utils.common.exception.ServiceException;

public interface SysService {

	public TSysConfig findByKey(String key) throws ServiceException;
	public Map<String,TSysPartner> findAll() throws ServiceException;
}
