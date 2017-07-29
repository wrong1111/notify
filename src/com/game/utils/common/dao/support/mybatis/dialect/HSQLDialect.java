package com.game.utils.common.dao.support.mybatis.dialect;

/**
 * Dialect for HSQLDB
 */
public class HSQLDialect implements Dialect {

	public String getLimitString(String sql, int offset, int limit) {
		boolean hasOffset = offset>0;
		return new StringBuffer( sql.length() + 10 )
		.append( sql )
		.insert( sql.toLowerCase().indexOf( "select" ) + 6, hasOffset ? " limit "+offset+" "+limit : " top "+limit )
		.toString();
	}
	
	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset() {
		return true;
	}
    
}
