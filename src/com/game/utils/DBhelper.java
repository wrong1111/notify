package com.game.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.game.entity.TSysPartner;

public class DBhelper {

	public static Connection conn = null;
	public static PreparedStatement ps = null;
	public static ResultSet rs = null;

	// get connection
	public static Connection getConn() {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/game");
			Connection conn = ds.getConnection();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void intiPartnerMap() {
		if (conn == null) {
			conn = getConn();
		}
		TSysPartner tp = null;
		try {
			String sql = "select * from t_sys_partner where status = '1' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			Map<String, TSysPartner> map1 = new HashMap<String,TSysPartner>();
			while (rs.next()) {
				tp = new TSysPartner();
				tp.setConnect(rs.getString("connect"));
				tp.setId(rs.getInt("id"));
				tp.setPartnername(rs.getString("partnername"));
				tp.setPartnerpasswd(rs.getString("partnerpasswd"));
				tp.setSignestring(rs.getString("signstring"));
				tp.setName(rs.getString("name"));
				tp.setEndtime(rs.getDate("endtime"));
				map1.put(String.valueOf(tp.getPartnername()), tp);
			}
			if(map1!=null && !map1.isEmpty()){
				Constants.partnermap = map1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	public static void initParameMap() {
		if (conn == null) {
			conn = getConn();
		}
		Map<String, String> map1 = new HashMap<String, String>();
		Map<String, String> map2 = new HashMap<String, String>();

		try {
			String sql = "select * from t_sys_dict where status = '1' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String key = rs.getString("dictname").trim();
				String value = rs.getString("dictvalue").trim();
				// 替换已经存在静态变量
//				if (key.startsWith("sms")) {
					// Constants.smsmap.put(key, value);
					map1.put(key, value);
//				} else {
					// Constants.parametermap.put( key ,value);
					map2.put(key, value);
//				}
			}
			if(map1!=null && !map1.isEmpty()){
				Constants.smsmap = map1;
			}
			if(map2!=null && !map2.isEmpty()){
				Constants.parametermap = map2;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	public static void initConfigMap() {
		if (conn == null) {
			conn = getConn();
		}
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select * from t_sys_config where status = '1' ";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("keyname"), rs.getString("keyvalue"));
			}
			rs.close();
			if(map!=null && !map.isEmpty()){
				Constants.configmap = map;
			}

			/*
			sql = "select * from t_sys_dict where status = '1' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			Map<String, String> map1 = new HashMap<String, String>();
			Map<String, String> map2 = new HashMap<String, String>();
			while (rs.next()) {
				String key = rs.getString("dictname");
				String value = rs.getString("dictvalue");
				// 替换已经存在静态变量
//				if (key.startsWith("sms")) {
					// Constants.smsmap.put(key, value);
					// System.out.println(value);
					map1.put(key, value);
//				} else {
					// Constants.parametermap.put( key ,value);
					map2.put(key, value);
//				}
			}
			if(map1!=null && !map1.isEmpty()){
				Constants.smsmap = map1;
			}
			if(map2!=null && !map2.isEmpty()){
				Constants.parametermap = map2;
			}
			*/
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	// init config map
	public static void init() {
		if (conn == null) {
			conn = getConn();
		}
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select * from t_sys_config where status = '1' ";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("keyname"), rs.getString("keyvalue"));
			}
			rs.close();
			if(map!=null && !map.isEmpty()){
				Constants.configmap = map;
			}

//			sql = "select * from t_sys_dict where status = '1' ";
//			ps = conn.prepareStatement(sql);
//			rs = ps.executeQuery();
//			Map<String, String> map1 = new HashMap<String, String>();
//			Map<String, String> map2 = new HashMap<String, String>();
//			while (rs.next()) {
//				String key = rs.getString("dictname").trim();
//				String value = rs.getString("dictvalue").trim();
//				// 替换已经存在静态变量
////				if (key.startsWith("sms")) {
//					map1.put(key, value);
////				} else {
//					map2.put(key, value);
////				}
//			}
//			if(map1!=null && !map1.isEmpty()){
//				Constants.smsmap = map1;
//			}
//			if(map2!=null && !map2.isEmpty()){
//				Constants.parametermap = map2;
//			}
//			rs.close();
			sql = "select * from t_sys_partner where status = '1' ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			Map<String, TSysPartner> map3 = new HashMap<String, TSysPartner>();
			TSysPartner tp = null;
			while (rs.next()) {
				tp = new TSysPartner();
				tp.setConnect(rs.getString("connect"));
				tp.setId(rs.getInt("id"));
				tp.setPartnername(rs.getString("partnername"));
				tp.setPartnerpasswd(rs.getString("partnerpasswd"));
				tp.setSignestring(rs.getString("signstring"));
				tp.setName(rs.getString("name"));
				tp.setEndtime(new Date(rs.getTimestamp("endtime").getTime()));
				map3.put(tp.getPartnername(), tp);
			}
			if(map3!=null && !map3.isEmpty()){
				Constants.partnermap = map3;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	}
