package com.game.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.dao.OrderDao2;
import com.game.entity.EcsOrderInfo;
import com.game.service.OrderService2;

@Service("orderService")
public class OrderServiceImpl2 implements OrderService2{

	@Autowired
	OrderDao2 orderDao;
	
	@Override
	public EcsOrderInfo findByOrdersn(String ordersn) {
		return orderDao.findOrderBysn(ordersn);
	}

	@Override
	public void updateEcsOrderStatus(EcsOrderInfo orderinfo) {
		orderDao.updateEntity(orderinfo);
	}

}
