package com.game.utils.common.dao.generic;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.game.utils.common.clazz.BeanUtils;
import com.game.utils.common.clazz.MapBeanUtils;
import com.game.utils.common.dao.DynamicDataSource;
import com.game.utils.common.dao.DynamicDataSourceHolder;
import com.game.utils.common.dao.support.Page;

/**
 * Hibernate Dao的泛型基类.
 */
@SuppressWarnings("unchecked")
public abstract class HibernateGenericDao2 extends HibernateSessionDao{
	protected Log logger = LogFactory.getLog(getClass());
	 
	public Session getSession() {
		 DynamicDataSourceHolder.setDataSourceType(DynamicDataSource.SHOP);
	     SessionFactory sessionFactory = super.getSessionFactory();
	     Session session = null;
	     try {
	    	session = sessionFactory.openSession();
	     }catch(Exception e) {
	    	 logger.error("[session],error->"+e.getMessage(),e);
	    	 session =  sessionFactory.getCurrentSession();
	     }
	     return session;
	}

	/**
	 * 根据ID获取对象. 实际调用Hibernate的session.get()方法返回实体
	 */
	public <T> T get(Class<T> entityClass, Serializable id) {
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
	 */
	public <T> T load(Class<T> entityClass, Serializable id) {
		return (T) getSession().load(entityClass, id);
	}

	/**
	 * 获取全部对象.
	 */
	public <T> List<T> getAll(Class<T> entityClass) {
		return getSession().createCriteria(entityClass).list();
	}

	/**
	 * 获取全部对象,带排序字段与升降序参数.
	 */
	public <T> List<T> getAll(Class<T> entityClass, String orderBy,
			boolean isAsc) {
		Assert.hasText(orderBy);
		if (isAsc)
			return getSession().createCriteria(entityClass)
					.addOrder(Order.asc(orderBy)).list();
		else
			return getSession().createCriteria(entityClass)
					.addOrder(Order.desc(orderBy)).list();
	}

	/**
	 * 保存对象.
	 */
	public int saveOrUpdate(Object o) {
		getSession().saveOrUpdate(o);
		return 1;
	}

	// 返回主键
	public Serializable save(Object o) {
		Serializable id = getSession().save(o);
		getSession().flush();
		getSession().evict(o);
		return id;
	}

	
	public Object merge(Object o){
		return getSession().merge(o);
	}
	public int update(Object o) {
		getSession().update(o);
		return 1;
	}

	public <T> void saveOrUpdateAll(List<T> objects) {
		int count =0;
		for (Object entity : objects) {
			getSession().saveOrUpdate(entity);
			if ((count++) % 50 == 0 ) { //50, same as the JDBC batch size  
				getSession().flush();    
				getSession().clear();   
			  }    
		}
	}
	
	public <T> void saveAll(List<T> objects) {
		int count =0;
		for (Object entity : objects) {
			getSession().save(entity);
			if ((count++) % 50 == 0 ) { //50, same as the JDBC batch size  
				getSession().flush();    
				getSession().clear();   
			  }    
		}
	}
	
	public <T> void updateAll(List<T> objects) {
		int count =0;
		for (Object entity : objects) {
			getSession().update(entity);
			if ((count++) % 50 == 0 ) { //50, same as the JDBC batch size  
				getSession().flush();    
				getSession().clear();   
			  }    
		}
	}

	/**
	 * 删除对象.
	 */
	public int remove(Object o) {
		getSession().delete(o);
		return 1;
	}

	/**
	 * 根据ID删除对象.
	 */
	public <T> int removeById(Class<T> entityClass, Serializable id) {
		return remove(get(entityClass, id));
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	/**
	 * 创建Query对象.
	 * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
	 * 留意可以连续设置,如下：
	 * 
	 * <pre>
	 * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * </pre>
	 * 
	 * 调用方式如下：
	 * 
	 * <pre>
	 *        dao.createQuery(hql)
	 *        dao.createQuery(hql,arg0);
	 *        dao.createQuery(hql,arg0,arg1);
	 *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
	 * </pre>
	 * 
	 * @param values
	 *            可变参数.
	 */
	public Query createQuery(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	public Query createQuery(String hql) {
		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		return query;
	}

	/**
	 * sql语句查询
	 * 
	 * @param sql
	 * @param values
	 * @return
	 */
	public Query createSQLQuery(String sql, Object... values) {
		Assert.hasText(sql);
		Query query = getSession().createSQLQuery(sql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}
	/**
	 * sql语句查询
	 * 转化成对象返回
	 * @param sql
	 * @param values
	 * @return
	 */
	public Query createSQLQuery(String sql,Class classes ,Object... values) {
		Assert.hasText(sql);
		Query query = getSession().createSQLQuery(sql).addEntity(classes);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}
	/**
	 * sql语句查询(map)
	 * 
	 * @param sql
	 * @param values
	 * @return
	 */
	public Query createSQLQuery(String sql, Map<String, Object> map) {
		Assert.hasText(sql);

		Query query = getSession().createSQLQuery(sql);
		if (map != null) {
			for (String key : map.keySet()) {
				query.setParameter(key, map.get(key));
			}
		}
		return query;
	}
	/**
	 * sql语句查询(map)
	 * 转化成class 实体对象返回
	 * @param sql
	 * @param values
	 * @return
	 */
	public  Query createSQLQuery(String sql,Class classes ,Map<String, Object> map) {
		Assert.hasText(sql);

		Query query = getSession().createSQLQuery(sql).addEntity(classes);
		if (map != null) {
			for (String key : map.keySet()) {
				query.setParameter(key, map.get(key));
			}
		}
		return query;
	}
	public Query createSQLQuery(String sql) {
		Assert.hasText(sql);
		Query query = getSession().createSQLQuery(sql);
		return query;
	}
	/**
	 * 转化成class 返回
	 * @param sql
	 * @return
	 */
	public Query createSQLQuery(String sql,Class classes) {
		Assert.hasText(sql);
		Query query = getSession().createSQLQuery(sql).addEntity(classes);
		return query;
	}
	/**
	 * sql语句查询
	 * 转化成自定义的对象
	 * @param sql
	 * @param values
	 * @return 5.2.10
	//session.createQuery(hql,transformerClass);
	 * session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(transformerClass));
	 */
	public List createSQLQueryToObj(String sql,Class classobj, Object... values) {
		Assert.hasText(sql);
		Query query = getSession().createSQLQuery(sql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		List<Map<String,Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List rlist =  new ArrayList();
		if(list!=null && !list.isEmpty()){
			for(Map<String,Object> m : list){
				 try {
					Object obj = MapBeanUtils.toObject(classobj, m);
					rlist.add(obj);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return rlist;
	}
	/**
	 * 创建Criteria对象.
	 * 
	 * @param criterions
	 *            可变的Restrictions条件列表,见{@link #createQuery(String,Object...)}
	 */
	public <T> Criteria createCriteria(Class<T> entityClass,
			Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 创建Criteria对象，带排序字段与升降序字段.
	 * 
	 */
	public <T> Criteria createCriteria(Class<T> entityClass, String orderBy,
			boolean isAsc, Criterion... criterions) {
		Assert.hasText(orderBy);

		Criteria criteria = createCriteria(entityClass, criterions);

		if (isAsc)
			criteria.addOrder(Order.asc(orderBy));
		else
			criteria.addOrder(Order.desc(orderBy));

		return criteria;
	}

	/**
	 * 根据hql查询,直接使用HibernateTemplate的find函数.
	 * 
	 * @param values
	 *            可变参数,见{@link #createQuery(String,Object...)}
	 */
	public List find(String hql, Object... values) {
		Assert.hasText(hql);
		Query queryObject = getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}

	/**
	 * 根据hql查询
	 * 
	 * @param map
	 */
	public List find(String hql, Map<String, Object> map) {
		Assert.hasText(hql);
		Query queryObject = getSession().createQuery(hql);
		if (map != null) {
			for (String key : map.keySet()) {
				queryObject.setParameter(key, map.get(key));
			}
		}
		return queryObject.list();
	}

	/**
	 * 根据属性名和属性值查询对象.
	 * 
	 * @return 符合条件的对象列表
	 */
	public <T> List<T> findBy(Class<T> entityClass, String propertyName,
			Object value) {
		Assert.hasText(propertyName);
		return createCriteria(entityClass, Restrictions.eq(propertyName, value))
				.list();
	}

	/**
	 * 根据属性名和属性值查询对象,带排序参数.
	 */
	public <T> List<T> findBy(Class<T> entityClass, String propertyName,
			Object value, String orderBy, boolean isAsc) {
		Assert.hasText(propertyName);
		Assert.hasText(orderBy);
		return createCriteria(entityClass, orderBy, isAsc,
				Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 根据属性名和属性值查询唯一对象.
	 * 
	 * @return 符合条件的唯一对象 or null if not found.
	 */
	public <T> T findUniqueBy(Class<T> entityClass, String propertyName,
			Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(entityClass,
				Restrictions.eq(propertyName, value)).uniqueResult();
	}

	/**
	 * 分页查询函数，使用hql.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 */
	public Page pagedQuery(String hql, int pageNo, int pageSize,
			Object... values) {
		Assert.hasText(hql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询
		String countQueryString = " select count(*) "
				+ removeSelect(removeOrders(hql));
		List countlist = this.find(countQueryString, values);
		int totalCount = ((Long) countlist.get(0)).intValue();

		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = createQuery(hql, values);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();

		return new Page(startIndex, totalCount, pageSize, list, pageNo);
	}
	/**
	 * 分页查询函数，使用sql.
	 * 返回 list中包含一条记录一个Bean对象
	 * @param pageNo
	 *            页号,从1开始.
	 *            
	 */
	public Page pagedQuerySql(String sql, int pageNo, int pageSize,Class clazz,
			Object... values) {
		Assert.hasText(sql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询
//		 String countQueryString = " select count (*) as cnt " + removeSelect(removeOrders(sql)) +" ";
		String countQueryString = " select count(*) as cnt from (" + sql + ") pagea ";
		Query sqlQuery = createSQLQuery(countQueryString, values);
		List countlist = sqlQuery.list();
		int totalCount = ((Number) countlist.get(0)).intValue();
		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = createSQLQuery(sql, values);
		//List list = query.setResultTransformer(Transformers.aliasToBean(classes)).setFirstResult(startIndex).setMaxResults(pageSize).list();
		List<Map<String,Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(startIndex).setMaxResults(pageSize).list();
		List rlist =  new ArrayList();
		if(list!=null && !list.isEmpty()){
			for(Map<String,Object> m : list){
				try {
					Object obj = MapBeanUtils.toObject(clazz, m);
					rlist.add(obj);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new Page(startIndex, totalCount, pageSize, rlist, pageNo);
	}
	/**
	 * 分页查询函数，使用sql.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 */
	public Page pagedQuerySql(String sql, int pageNo, int pageSize,
			Object... values) {
		Assert.hasText(sql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询
		// String countQueryString = " select count (*) as cnt "
		// + removeSelect(removeOrders(sql));
		String countQueryString = " select count(*) as cnt from (" + sql + ") pagea ";
		Query sqlQuery = createSQLQuery(countQueryString, values);
		List countlist = sqlQuery.list();
		int totalCount = ((Number) countlist.get(0)).intValue();
		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = createSQLQuery(sql, values);
		sqlQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();

		return new Page(startIndex, totalCount, pageSize, list, pageNo);
	}
	/**
	 * 分页查询函数，使用sql.(map)
	 *  返回list<MAP<String,Object>>
	 * @param sql
	 *        执行的sql语句
	 * @param pageNo
	 *            页号,从1开始.
	 * @param pagesize 
	 *         每页条数
	 * @param  values
	 *        Map<String,object>
	 */
	public Page pagedQuerySql(String sql, int pageNo, int pageSize,
			Map<String, Object> values) {
		Assert.hasText(sql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询
		// String countQueryString = " select count (*) as cnt "
		// + removeSelect(removeOrders(sql));
		String countQueryString = " select count(*) as cnt from (" + sql + ") pagea ";
		Query sqlQuery = createSQLQuery(countQueryString, values);
		List countlist = sqlQuery.list();
		int totalCount = ((Number) countlist.get(0)).intValue();
		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = createSQLQuery(sql, values);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();
		return new Page(startIndex, totalCount, pageSize, list, pageNo);
	}

	/**
	 * 分页查询函数，使用sql.(map)
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 */
	public Page pagedQuerySql(String sql, int pageNo, int pageSize,
			Map<String, Object> values, LinkedHashMap<String, Boolean> sorts) {
		Assert.hasText(sql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询
		// String countQueryString = " select count (*) as cnt "
		// + removeSelect(removeOrders(sql));
		String countQueryString = " select count(*) as cnt from (" + sql + ") pagea ";
		Query sqlQuery = createSQLQuery(countQueryString, values);
		List countlist = sqlQuery.list();
		int totalCount = ((Number) countlist.get(0)).intValue();
		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		if (sorts != null && sorts.size() > 0) {
			StringBuilder sortby = new StringBuilder(" order by ");
			Iterator<Entry<String, Boolean>> iterator = sorts.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Boolean> next = iterator.next();
				String key = next.getKey();
				Boolean value = next.getValue();
				sortby.append(key).append(value ? " asc" : " desc");
				if (iterator.hasNext()) {
					sortby.append(",");
				}
			}
			sql = "select * from (" + sql + ") " + sortby.toString();
		}
		Query query = createSQLQuery(sql, values);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();
		return new Page(startIndex, totalCount, pageSize, list, pageNo);
	}

	/**
	 * 统计条数
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getCount(Criteria criteria) {

		return ((Number) criteria.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
	}

	/**
	 * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
		Assert.notNull(criteria);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		CriteriaImpl impl = (CriteriaImpl) criteria;

		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtils.forceGetProperty(impl,
					"orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 执行查询
		int totalCount = ((Number) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		try {
			BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}
		// 返回分页对象
		if (totalCount < 1)
			return new Page();

		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		List list = criteria.setFirstResult(startIndex).setMaxResults(pageSize)
				.list();
		return new Page(startIndex, totalCount, pageSize, list, pageNo);

	}

	/**
	 * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, criterions);
		return pagedQuery(criteria, pageNo, pageSize);
	}

	/**
	 * 分页查询函数，根据entityClass和查询条件参数,排序参数创建默认的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
			String orderBy, boolean isAsc, Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, orderBy, isAsc,
				criterions);
		return pagedQuery(criteria, pageNo, pageSize);
	}

	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public <T> boolean isUnique(Class<T> entityClass, Object entity,
			String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = createCriteria(entityClass).setProjection(
				Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name,
						PropertyUtils.getProperty(entity, name)));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getIdName(entityClass);

			// 取得entity的主键值
			Serializable id = getId(entityClass, entity);

			// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return (Integer) criteria.uniqueResult() == 0;
	}

	/**
	 * 取得对象的主键值,辅助函数.
	 */
	public Serializable getId(Class entityClass, Object entity)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Assert.notNull(entity);
		Assert.notNull(entityClass);
		return (Serializable) PropertyUtils.getProperty(entity,
				getIdName(entityClass));
	}

	/**
	 * 取得对象的主键名,辅助函数.
	 */
	public String getIdName(Class clazz) {
		Assert.notNull(clazz);
		ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
		Assert.notNull(meta, "Class " + clazz
				+ " not define in hibernate session factory.");
		String idName = meta.getIdentifierPropertyName();
		Assert.hasText(idName, clazz.getSimpleName()
				+ " has no identifier property define.");
		return idName;
	}

	/**
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 * 
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 * 
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 构造Criteria的排序条件默认函数.可供其他查询函数调用
	 * 
	 * @param criteria
	 *            Criteria实例.
	 * @param sortMap
	 *            排序条件.
	 * @param entity
	 *            entity对象,用于使用反射来获取某些属性信息
	 */
	protected void sortCriteria(Criteria criteria, Map sortMap, Class entity) {
		if (!sortMap.isEmpty()) {
			for (Object o : sortMap.keySet()) {
				String fieldName = o.toString();
				String orderType = sortMap.get(fieldName).toString();

				// 处理嵌套属性如category.name,modify_user.id,暂时只处理一级嵌套
				if (fieldName.indexOf('.') != -1) {
					String alias = StringUtils.substringBefore(fieldName, ".");
					criteria.createAlias(alias, alias);
				}

				if ("asc".equalsIgnoreCase(orderType)) {
					criteria.addOrder(Order.asc(fieldName));
				} else {
					criteria.addOrder(Order.desc(fieldName));
				}
			}
		}
	}

	/**
	 * 得到物理字段名
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @return
	 */
	public <T> String getColumnName(Class<T> entityClass, String propertyName) {
		SingleTableEntityPersister ep = (SingleTableEntityPersister) getSessionFactory()
				.getClassMetadata(entityClass);
		return ep.getPropertyColumnNames(propertyName)[0];
	}
}
