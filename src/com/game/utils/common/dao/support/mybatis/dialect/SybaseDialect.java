package com.game.utils.common.dao.support.mybatis.dialect;

public class SybaseDialect implements Dialect {

	public String getLimitString(String sql, int offset, int limit) {
		throw new UnsupportedOperationException( "paged queries not supported" );
	}

	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return false;
	}

}
