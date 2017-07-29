/**
 * 
 */
package com.game.utils.common.dao.entity;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.game.utils.common.clazz.GenericsUtils;
import com.game.utils.common.dao.IEntityDao;
import com.game.utils.common.dao.generic.HibernateGenericDao;
import com.game.utils.common.dao.generic.HibernateGenericDao2;
import com.game.utils.common.dao.support.ModelSetup;
import com.game.utils.common.dao.support.Page;
import com.game.utils.common.dao.support.hibernate.HibernateModelSetup;


/**
 * 负责为单个Entity对象提供CRUD操作的Hibernate DAO基类. <p/>
 * 子类只要在类定义时指定所管理Entity的Class,
 * 即拥有对单个Entity对象的CRUD操作.
 */
@SuppressWarnings("unchecked")
public abstract class HibernateEntityDao2<T extends Serializable> extends HibernateGenericDao2
        implements IEntityDao<T> {

    private Class<T> entityClass;// DAO所管理的Entity类型.

    /**
     * 在构造函数中将泛型T.class赋给entityClass.
     */
    public HibernateEntityDao2() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
    }

    /**
     * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
     */
    protected Class<T> getEntityClass() {
        if (entityClass == null) {
            entityClass = GenericsUtils.getSuperClassGenricType(getClass());
        }
        return entityClass;
    }

    /**
     * 得到记录总条数
     *
     * @return
     */
    public Integer getCount(ModelSetup modelSetup) {
        HibernateModelSetup hibernateModelSetup=(HibernateModelSetup)modelSetup;
        Criteria c = this.createCriteria();
        hibernateModelSetup.setup(c);
        return this.getCount(c);
    }

    /**
     * 根据ID获取对象.
     *
     * @see HibernateGenericDao#getId(Class,Object)
     */
    public T get(Serializable id) {
        return get(getEntityClass(), id);
    }
    
    
    /**
     * 获取全部对象
     *
     * @see HibernateGenericDao#getAll(Class)
     */
    public List<T> getAll() {
        return getAll(getEntityClass());
    }

    public List<T> getAll(ModelSetup modelSetup) {
        HibernateModelSetup hibernateModelSetup=(HibernateModelSetup)modelSetup;
          Criteria criteria = this.createCriteria();
        hibernateModelSetup.setup(criteria);
        return criteria.list();
    }

    public  int update(Object o) {
        return super.update(o);
    }
    public int create(Object o) {
    	return this.saveOrUpdate(o);
    }
    
    public Object merge(Object o){
    	return super.merge(o);
    }
    
    //返回主键
    public Serializable save(Object o) {
    	return super.save(o);
    }
    
    public <T> void saveOrUpdateAll(List<T> objects){
        super.saveOrUpdateAll(objects);
    }
    

    public <T> void saveAll(List<T> objects){
        super.saveAll(objects);
    }
    
    public <T> void updateAll(List<T> objects){
        super.updateAll(objects);
    }
    
    @Override
    public int remove(Object o) {
        return super.remove(o);

    }

    /**
     * 获取全部对象,带排序参数.
     *
     * @see com.kms.framework.core.dao.generic.HibernateGenericDao#getAll(Class,String,boolean)
     */
    public List<T> getAll(String orderBy, boolean isAsc) {
        return getAll(getEntityClass(), orderBy, isAsc);
    }

    /**
     * 根据ID移除对象.
     *
     * @see HibernateGenericDao#removeById(Class, java.io.Serializable)
     */
    public int removeById(Serializable id) {
        return removeById(getEntityClass(), id);
    }

    /**
     * 取得Entity的Criteria.
     */
    public Criteria createCriteria(Criterion... criterions) {
        return createCriteria(getEntityClass(), criterions);
    }

    /**
     * 取得Entity的Criteria,带排序参数.
     */
    public Criteria createCriteria(String orderBy, boolean isAsc,
                                   Criterion... criterions) {
        return createCriteria(getEntityClass(), orderBy, isAsc, criterions);
    }

    /**
     * 根据属性名和属性值查询对象.
     *
     * @return 符合条件的对象列表
     * @see HibernateGenericDao#findBy(Class,String,Object)
     */
    public List<T> findBy(String propertyName, Object value) {
        return findBy(getEntityClass(), propertyName, value);
    }

    /**
     * 根据属性名和属性值查询对象,带排序参数.
     *
     * @return 符合条件的对象列表
     * @see com.kms.framework.core.dao.generic.HibernateGenericDao#findBy(Class,String,Object,String,boolean)
     */
    public List<T> findBy(String propertyName, Object value, String orderBy,
                          boolean isAsc) {
        return findBy(getEntityClass(), propertyName, value, orderBy, isAsc);
    }

    /**
     * 根据属性名和属性值查询单个对象.
     *
     * @return 符合条件的唯一对象 or null
     * @see HibernateGenericDao#findUniqueBy(Class,String,Object)
     */
    public T findUniqueBy(String propertyName, Object value) {
        return findUniqueBy(getEntityClass(), propertyName, value);
    }
                     
    /**
     * 判断对象某些属性的值在数据库中唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     * @see com.kms.framework.core.dao.generic.HibernateGenericDao#isUnique(Class,Object,String)
     */
    public boolean isUnique(Object entity, String uniquePropertyNames) {
        return isUnique(getEntityClass(), entity, uniquePropertyNames);
    }

    /**
     * 消除与 Hibernate Session 的关联
     *
     * @param entity
     */
    public void evit(Object entity) {
        getSession().evict(entity);
    }

    public Page pagedQuery(ModelSetup modelSetup, int pageNo, int pageSize) {
        HibernateModelSetup hibernateModelSetup=(HibernateModelSetup)modelSetup;
        //首先判断hql是否为空，不为空则调用hql查询；再判断sql是否为空，不为空则调用sql查询；hql和sql都为空则继续流程
        if (null != hibernateModelSetup.getHql() && !"".equals(hibernateModelSetup.getHql())) {
        	return pagedQueryHql(hibernateModelSetup,pageNo,pageSize);
        }
        if (null != hibernateModelSetup.getSql() && !"".equals(hibernateModelSetup.getSql())) {
        	return pagedQuerySql(hibernateModelSetup,pageNo,pageSize);
        }
        Criteria criteria = this.createCriteria();
        hibernateModelSetup.setup(criteria);
        return pagedQuery(criteria, pageNo, pageSize);
    }


    /**
     * 通过pojo中的属性名得到时间格式 这是模糊查询时间字段的时候需要用到 如果与默认格式不同则请在子类中重构该方法
     *
     * @param propertyName
     * @return
     */
    protected String getDateFormatByProperty(String propertyName) {
        return "yyyy-MM-dd hh24:mi";
    }
    
    public Page pagedQueryHql(HibernateModelSetup hibernateModelSetup, int pageNo, int pageSize) {
    	String hql = hibernateModelSetup.getHql();
    	return pagedQuery(hql, pageNo, pageSize,hibernateModelSetup.getParams());
    }
    
    public Page pagedQuerySql(HibernateModelSetup hibernateModelSetup, int pageNo, int pageSize) {
    	String sql = hibernateModelSetup.getSql();
    	return pagedQuerySql(sql, pageNo, pageSize,hibernateModelSetup.getParams());
    }

    public Page pagedQuery(String sql, int pageNo, int pageSize, boolean isHql) {
    	if (isHql) {
    		return pagedQuery(sql, pageNo, pageSize);
    	} else {
    		return pagedQuerySql(sql, pageNo, pageSize);
    	}  	
    }

}
