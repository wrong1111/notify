package com.game.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.game.dao.UserDao;
import com.game.entity.TPayRecord;
import com.game.entity.TPayRecordLog;
import com.game.pojo.NotifyVo;
import com.game.service.PayService;
import com.game.utils.Constants;
import com.game.utils.common.dao.support.Page;
import com.game.utils.encription.Md5Util;

@Service("payService")
public class PayServiceImpl implements PayService{

	
	@Autowired
	UserDao userDao;
	
	
	@Override
	public Map<String, String> findLastPayCompanyCount(Date nowdate, String companycode) {
		return userDao.findLastPayCompanyCount(nowdate, companycode);
	}

	@Override
	public TPayRecord createTPayRecord(TPayRecord record) {
		Integer id = (Integer) userDao.save(record);
		record.setId(id);
		return record;
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
		if(StringUtils.isNotBlank(record.getPayresult())){
			payrecord.setPayresult(record.getPayresult());
		}
		if(record.getNoticetimes() == null) {
			record.setNoticetimes(0);
		}
		if(payrecord.getNoticetimes() == null) {
			payrecord.setNoticetimes(0);
		}
		if(StringUtils.isNotBlank(record.getChannel())) {
			payrecord.setChannel(record.getChannel());
		}
		userDao.update(record);
	}


	@Override
	public NotifyVo updatePayReceive(NotifyVo vo) {
		if(vo == null || StringUtils.isBlank(vo.getMerchantOrderNo())) {
			return null;
		}
		TPayRecord record = userDao.findByOrder(vo.getMerchantOrderNo());
		if(record == null) return null;
		
		if(StringUtils.isNotBlank(vo.getNoticestr())) {
			record.setRetunpaystr(vo.getNoticestr());
		}
		if(StringUtils.isNotBlank(vo.getStatus())) {
			record.setReturncode(vo.getStatus());
		}
		
		if(StringUtils.isNotBlank(vo.getResponseDesc())) {
			record.setReturnpayresult(vo.getResponseDesc());
		}
		if(record.getReturnpaytime() == null) {
			record.setReturnpaytime(vo.getCreateTime());
		}
		 
		//同时更新通知
		vo.setUrl(record.getNoticeurl());
		vo.setMerchantNo(record.getMemno());
		vo.setCreateTime(new Date());
		vo.setLastNotifyTime(vo.getCreateTime());
		vo.setNotifyTimes(0);
		
		//组成通知下游的字符串
		HashMap<String,String> notifyStr = new HashMap<String,String>();
		notifyStr.put("orderno", vo.getMerchantOrderNo());
		notifyStr.put("code",vo.getStatus());
		notifyStr.put("requestDesc", vo.getResponseDesc());
		notifyStr.put("money", String.valueOf(record.getMoney().intValue()));
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("partnerid", vo.getMerchantNo());
		param.put("data", JSON.toJSON(notifyStr));
		String md5str = Md5Util.md5_32(JSON.toJSONString(notifyStr)+Constants.getPartner(vo.getMerchantNo()).getSignestring());
		param.put("sign", md5str);
		vo.setNoticestr(JSON.toJSONString(param));
		vo.setStatus("");
		 
		//更新通知下游字符串
		record.setNoticestr(vo.getNoticestr());
		
		userDao.update(record);
		return vo;
	}

	@Override
	public void saveRecordLog(TPayRecordLog log) {
		userDao.save(log);
	}

	@Override
	public void updateNotify(String orderno,Integer times,Date lasttime,String status) {
		TPayRecord record = userDao.findByOrder(orderno);
		if(record == null) {
			System.out.println("orderno["+orderno+"],不存在");
			return;
		}
		record.setNoticetimes(times);
		record.setNoticeresult(status);
		record.setNoticelastdatetime(lasttime);
		userDao.update(record);
	}

	@Override
	public Page findNotify2Send(String status, int page, int pagesize) {
		return userDao.findNotify2Send(status, page, pagesize);
	}
	

}
