package com.game.dao;

import com.game.entity.BaseEntity;
import com.game.entity.EcsOrderInfo;

public interface OrderDao2  {

	public EcsOrderInfo findOrderBysn(String ordersn);
	
	public void updateEntity(BaseEntity entity);
}
