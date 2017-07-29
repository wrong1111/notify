package com.game.utils.common.service;

import java.io.Serializable;

import com.game.utils.common.dao.support.ModelSetup;
import com.game.utils.common.dao.support.Page;

/**
 * server基类
 * @author XiaYong
 *
 */
public interface IBaseEntityService {
    public <T> void create(T entity);

    public <T> void update(T entity);

    public void removeById(Serializable id) ;

    public <T> T get(Serializable id);

    public Page pagedQuery(ModelSetup modelSetup, int pageNo, int pageSize);

    public String getIdName(Class clazz) ;
}
