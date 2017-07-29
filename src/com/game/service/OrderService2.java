package com.game.service;

import com.game.entity.EcsOrderInfo;

public interface OrderService2 {

	public EcsOrderInfo findByOrdersn(String ordersn);
	
	public void updateEcsOrderStatus(EcsOrderInfo orderinfo);
}
