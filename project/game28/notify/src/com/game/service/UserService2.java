package com.game.service;

import java.util.Date;
import java.util.Map;

import com.game.pojo.MembaseVo;

public interface UserService2 {
	
	public Map<String, String> findLastPayCompanyCount(Date nowdate,String companycode);
	
	public MembaseVo createRecharge(MembaseVo vo);
}
