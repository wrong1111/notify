package com.game.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.game.dao.UserDao;
import com.game.entity.TPayRecord;
import com.game.pojo.PayVo;
import com.game.service.PayService;
import com.game.utils.common.dao.support.Page;

public class PayServiceImpl implements PayService{

	@Autowired
	UserDao userDao;
	
	
	@Override
	public Map<String, String> findLastPayCompanyCount(Date nowdate, String companycode) {
		return userDao.findLastPayCompanyCount(nowdate, companycode);
	}

	@Override
	public TPayRecord createTPayRecord(TPayRecord record) {
		return (TPayRecord) userDao.save(record);
	}

	@Override
	public void updateTPayRecord(TPayRecord record) {
		TPayRecord payrecord = userDao.findByOrder(record.getOrderno());
		if(payrecord == null) return;
		if(StringUtils.isNotBlank(record.getQrcode())) {
			payrecord.setQrcode(record.getQrcode());
		}
		if(StringUtils.isNotBlank(record.getQrcodeurl())) {
			payrecord.setQrcodeurl(record.getQrcodeurl());
		}
		if(StringUtils.isNotBlank(record.getPaystr())) {
			payrecord.setPaystr(record.getPaystr());
		}
		if(record.getNoticetimes() == null) {
			record.setNoticetimes(0);
		}
		userDao.update(record);
	}

	@Override
	public Page findNotify2Send(TPayRecord record,int page ,int pagesize) {
		return userDao.findNotify2Send(record, page, pagesize);
	}

}
