package com.game.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.game.dao.OrderDao2;
import com.game.entity.BaseEntity;
import com.game.entity.EcsOrderInfo;
import com.game.utils.common.dao.entity.HibernateEntityDao2;

@Repository("orderDao2")
public class OrderDaoImpl2 extends HibernateEntityDao2<Serializable> implements OrderDao2{

	@Override
	public EcsOrderInfo findOrderBysn(String ordersn) {
		return super.findUniqueBy(EcsOrderInfo.class, "order_sn", ordersn);
	}

	@Override
	public void updateEntity(BaseEntity entity) {
		super.update(entity);
	}

}
