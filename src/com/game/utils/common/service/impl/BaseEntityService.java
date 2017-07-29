
package com.game.utils.common.service.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.game.utils.common.dao.IEntityDao;
import com.game.utils.common.dao.support.ModelSetup;
import com.game.utils.common.dao.support.Page;
import com.game.utils.common.domain.entity.Entity;
import com.game.utils.common.service.IBaseEntityService;
import com.game.utils.common.clazz.GenericsUtils;

/**
 * server实现类的基类
 * @author XiaYong
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseEntityService<T extends Entity> implements IBaseEntityService{
    private Log logger = LogFactory.getLog(BaseEntityService.class);
    public Class<T> getEntityClass() {
        if (entityClass == null)
            entityClass = (Class<T>) GenericsUtils.getSuperClassGenricType(getClass());
        return entityClass;
    }

    protected Class<T> entityClass;

    protected String getEntityName() {
        return getEntityClass().getSimpleName();
    }
    abstract protected IEntityDao<T> getDao();

    public  void create(Object entity) {

        getDao().create(entity);
        //logger.info("create a {}:{}" ,new Object[]{ getEntityName(),((T)entity).getId()});
    }

    public void update(Object entity) {
        getDao().update(entity);
        //logger.info("update a {}:{}" ,new Object[]{  getEntityName(),((T)entity).getId()});
    }

    public void removeById(Serializable id) {
        getDao().removeById(id);
        //logger.info("delete a {}:{}" + new Object[]{  getEntityName(),id});

    }

    public T get(Serializable id) {
        return  getDao().get(id);
    }

    public Page pagedQuery(ModelSetup modelSetup, int pageNo, int pageSize) {
        return getDao().pagedQuery(modelSetup, pageNo, pageSize);
    }

    public String getIdName(Class clazz) {
        return getDao().getIdName(clazz);
    }
}
