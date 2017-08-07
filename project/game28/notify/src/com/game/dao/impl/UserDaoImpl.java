package com.game.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.game.dao.UserDao;
import com.game.entity.TPayRecord;
import com.game.pojo.NotifyVo;
import com.game.utils.common.dao.entity.HibernateEntityDao;
import com.game.utils.common.dao.support.Page;

@Repository("userDao")
public class UserDaoImpl extends HibernateEntityDao<Serializable> implements UserDao {

	@Override
	public Map<String, String> findLastPayCompanyCount(Date lastTime, String compancode) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("channel"));
		projectionList.add(Projections.rowCount());
		HashMap<String, String> result = new HashMap<String, String>();
		Criteria criteria = super.createCriteria(TPayRecord.class).add(Restrictions.eq("payresult", "SUCCESS"))
				.add(Restrictions.eq("returncode", "0000"));
		if (lastTime != null) {
			criteria = criteria.add(Restrictions.between("paytime", lastTime, new Date()));
		}
		if (StringUtils.isNotBlank(compancode)) {
			criteria = criteria.add(Restrictions.eq("channel", compancode));
		}
		// criteria.addOrder(Order.desc("paytime"));
		criteria.setProjection(projectionList);

		List<Object[]> objects = criteria.list();
		Iterator<Object[]> iterator = objects.iterator();
		while (iterator.hasNext()) {
			Object[] o = (Object[]) iterator.next();
			result.put(o[0].toString(), o[1] == null ? "0" : o[1].toString());
		}
		return result;
	}

	@Override
	public TPayRecord findByOrder(String orderno) {
		 return super.findUniqueBy(TPayRecord.class,"orderno",orderno);
	}

	@Override
	public Page findNotify2Send(String status, int page, int pagesize) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(
				" select  id notifyId,memno,orderno merchantOrderNo,noticeurl url,noticetimes notifyTimes,noticelastdatetime lastNotifyTime,noticestr from t_pay_record r where 1=1 ");
		List<Object> param = new ArrayList<Object>();
		if (StringUtils.isNotBlank(status)) {
			if ("notify".equals(status)) {
				strBuilder.append(" and r.noticeresult in ('HTTPOK','HTTPERR')  and r.noticetimes <= 4 ");
			} else {
				strBuilder.append(" and r.noticeresult = ? ");
				param.add(status);
			}
		}
		return super.pagedQuerySql(strBuilder.toString(), page, pagesize, NotifyVo.class, param.toArray());
	}

	@Override
	public TPayRecord findByNotifyId(Integer id) {
		return (TPayRecord) super.findBy(TPayRecord.class, "id", id);
	}
}
