package com.game.utils.common.domain.entity;

import java.io.Serializable;

/**
 * 实体类基类
 * @author XiaYong
 *
 * @param <T>
 */

@SuppressWarnings("serial")
public abstract class Entity<T> implements Serializable{
	/**
	 * 统一的ID声明
	 * 
	 */
    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
