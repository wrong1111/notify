package com.game.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.game.dao.SysDAO;
import com.game.entity.TSysConfig;
import com.game.entity.TSysPartner;
import com.game.utils.common.dao.entity.HibernateEntityDao;
import com.game.utils.common.exception.DaoException;

@Repository("sysDao")
public class SysDAOImpl extends HibernateEntityDao<Serializable> implements SysDAO {

	public List<TSysPartner> findAll() {
		return super.createCriteria(TSysConfig.class).add(Restrictions.eq("status", "1")).list();
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<TSysConfig> criteria = builder.createQuery(TSysConfig.class);
	}

	@Override
	public TSysConfig findByKey(String key) throws DaoException {
		Object obj =  super.createCriteria(TSysConfig.class).add(Restrictions.eq("status", "1")).add(Restrictions.eq("keyname", key)).uniqueResult();
		if(obj!=null){
			return (TSysConfig) obj;
		}
		return null;
	}
	
	
}
