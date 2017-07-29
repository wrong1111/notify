package com.game.utils.common.dao.support.mybatis.dialect;
/**
 * 类似hibernate的Dialect,但只精简出分页部分
 */
public interface Dialect {
	
    public boolean supportsLimit();

    public String getLimitString(String sql, int offset, int limit);
 
    public boolean supportsLimitOffset();
    
}

