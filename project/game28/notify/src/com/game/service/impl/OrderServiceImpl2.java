package com.game.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.game.entity.EcsOrderInfo;
import com.game.service.OrderService2;
import com.game.utils.DBhelper;

@Service("orderService2")
public class OrderServiceImpl2 implements OrderService2{

	static final String ORDER_FIND_SQL = " SELECT order_id,order_sn,order_amount,goods_amount,pay_id FROM  ecs_order_info WHERE order_sn ='%s' ";
	static final String ORDER_UPDATE_SQL = " UPDATE  ecs_order_info SET order_status=1 ,pay_status=2 ,shipping_status =3  WHERE order_sn ='%s' ";
	@Override
	public EcsOrderInfo findByOrdersn(String ordersn) {
		if(StringUtils.isBlank(ordersn)) {
			return null;
		}
		EcsOrderInfo info = null;
		Connection conn = null;
		PreparedStatement ps = null; 
		ResultSet rs = null;
		try {
			conn = DBhelper.getConn2();
			ps = conn.prepareStatement(String.format(ORDER_FIND_SQL, ordersn));
			rs = ps.executeQuery();
			while (rs.next()) {
				info = new EcsOrderInfo() ;
				info.setOrder_sn(ordersn);
				info.setOrder_id(rs.getInt("order_id"));
				info.setOrder_amount(rs.getBigDecimal("goods_amount"));
				info.setPay_id(rs.getInt("pay_id"));
			}
			return info;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(!rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(!ps.isClosed()) {
					ps.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
	}

	@Override
	public void updateEcsOrderStatus(EcsOrderInfo orderinfo) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			if(orderinfo!=null && StringUtils.isNotBlank(orderinfo.getOrder_sn())) {
				conn = DBhelper.getConn2();
				ps = conn.prepareStatement(String.format(ORDER_UPDATE_SQL, orderinfo.getOrder_sn()));
				ps.executeUpdate();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(!ps.isClosed()) {
					ps.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
