package com.game.utils;

public final class TableConstants {
	/**
	 * 根据彩种ID 得到彩种对应的【走势图】表名
	 * @param lotteryId
	 * @return
	 */
	public static String getChartTableName(Integer lotteryId){
		return "t_chart_"+lotteryId;
	}
	
	 
	/**
	 * 根据彩种ID 得到彩种对应的【遗漏】表名
	 * @param lotteryId
	 * @return
	 */
	public static String getElTableName(Integer lotteryId){
		return "t_chart_el_"+lotteryId;
	}
	 
}
