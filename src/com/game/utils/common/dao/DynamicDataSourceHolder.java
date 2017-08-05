package com.game.utils.common.dao;

public class DynamicDataSourceHolder {

	 // 线程本地环境  
    private static final ThreadLocal contextHolder = new ThreadLocal();  
      
    // 设置数据源类型  
    public static void setDataSourceType(String dataSourceType) { 
    	System.out.println("set-->"+dataSourceType);
        contextHolder.set(dataSourceType);
    }  
  
    // 获取数据源类型  
    public static String getDataSourceType() {
    	String obj =(String) contextHolder.get();
    	System.out.println("get--->"+obj);
        return obj;  
    }  
  
    // 清除数据源类型  
    public static void clearDataSourceType() {  
    	System.out.println("clear===");
        contextHolder.remove();  
    }  
}
