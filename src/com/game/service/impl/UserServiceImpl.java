package com.game.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.dao.UserDao;
import com.game.entity.TMemMoneyRecord;
import com.game.pojo.MembaseVo;
import com.game.service.UserService2;
import com.game.utils.TradUtil;

@Service("userService")
public   class UserServiceImpl implements UserService2 {

	Logger log = Logger.getLogger(getClass());
	@Autowired
	UserDao userDao;
	@Override
	public Map<String, String> findLastPayCompanyCount(Date nowdate, String companycode) {
		 
		return null;
	}
	@Override
	public MembaseVo createRecharge(MembaseVo vo) {
		Integer userid = Integer.valueOf(vo.getUserid());
		TMemMoneyRecord record = new TMemMoneyRecord();
		String tradeno  = vo.getBankno();
		if(StringUtils.isBlank(tradeno)){
			tradeno = TradUtil.getTradingNo("C");
		}
		record.setTradeno(tradeno);
		record.setAddmoney(BigDecimal.ZERO);
		record.setCreatetime(new Date());
		record.setFlag("0");
		record.setMoney(vo.getMoney().multiply(BigDecimal.valueOf(100)));//转换成分
		record.setPlatcode("9997");
		record.setBankcode(vo.getChannel());
		record.setType("1");
		userDao.save(record);
		
		vo.setCode(record.getBankcode());//支付通道
		vo.setCardno(tradeno);//订单号
		vo.setErrorcode("0");
		return vo;
	}
	
}
