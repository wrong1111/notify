package com.game.utils.common.dao;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	public static final String GAME="GAME";  
    public static final String SHOP="SHOP"; 
    
	@Override
	protected Object determineCurrentLookupKey() {
		Object obj = DynamicDataSourceHolder.getDataSourceType();
		return  obj;
	}

}
