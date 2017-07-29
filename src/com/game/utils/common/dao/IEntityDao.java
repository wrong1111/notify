package com.game.utils.common.dao;


import java.io.Serializable;
import java.util.List;

import com.game.utils.common.dao.support.ModelSetup;
import com.game.utils.common.dao.support.Page;

/**
 * dao基类
 * @author XiaYong
 *
 * @param <T>
 */
public interface IEntityDao<T> {
    T get(Serializable id);

    List<T> getAll();

    List<T> getAll(ModelSetup ModelSetup);

    int update(Object o);

    int create(Object o);
    
    Serializable save(Object o);

    int remove(Object o);

    int removeById(Serializable id);
    
    void evit(Object entity);
    
    //清空session(hibernate)
    void clear();
    
    Object merge(Object o);
    
    /**
     *  将saveAll公布出来 laiyonghong update on 2013/10/18
     * @param objects
     */
    <T> void saveOrUpdateAll(List<T> objects);
    
    public <T> void saveAll(List<T> objects);
    
    public <T> void updateAll(List<T> objects);
    
    /**
     * 获取Entity对象的主键名.
     * @param clazz
     * @return
     */
    String getIdName(Class clazz);

    Integer getCount(ModelSetup ModelSetup);

    Page pagedQuery(ModelSetup modelSetup, int pageNo, int pageSize);
    
    Page pagedQuery(String sql, int pageNo, int pageSize, boolean isHql);
}
