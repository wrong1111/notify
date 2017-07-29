package com.game.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.game.dao.UserDao;
import com.game.entity.TMemMoneyRecord;
import com.game.utils.common.dao.entity.HibernateEntityDao;
@Repository("userDao")
public class UserDaoImpl extends HibernateEntityDao<Integer> implements UserDao {
	
	@Override
	public Map<String, String> findLastPayCompanyCount(Date lastTime, String compancode) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("bankcode"));
		projectionList.add(Projections.rowCount());
		HashMap<String, String> result = new HashMap<String,String>();
		Criteria criteria = super.createCriteria(TMemMoneyRecord.class).add(Restrictions.eq("type", "1")).add(Restrictions.eqOrIsNull("flag", "2")).add(Restrictions.eq("platcode", "9997"));
		if(lastTime !=null){
			criteria = criteria.add(Restrictions.between("createtime", lastTime, new Date()));
		}
		if(StringUtils.isNotBlank(compancode)){
			criteria = criteria.add(Restrictions.eq("bankcode",compancode));
		}
		criteria.addOrder(Order.desc("bankcode"));
		criteria.setProjection(projectionList);
		
		List<Object[]> objects = criteria.list();
		Iterator<Object[]> iterator = objects.iterator();
		while(iterator.hasNext()) {
		    Object[] o = (Object[]) iterator.next();
		    result.put(o[0].toString(), o[1] == null? "0": o[1].toString());
		}
		return result;
	}
}
