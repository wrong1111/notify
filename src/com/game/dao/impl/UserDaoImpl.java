package com.game.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.game.entity.TPayRecord;
import com.game.pojo.PayVo;
import com.game.utils.common.dao.entity.HibernateEntityDao;
import com.game.utils.common.dao.support.Page;
@Repository("userDao")
public class UserDaoImpl extends HibernateEntityDao<Serializable> implements UserDao {
	
	 
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

	@Override
	public TPayRecord findByOrder(String orderno) {
		return super.findUniqueBy(TPayRecord.class,"orderno",orderno);
	}

	@Override
	public Page findNotify2Send(TPayRecord record,int page ,int pagesize) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(" select  memno,money,orderno,noticeurl,qrcode,qrcodeurl,noticetimes,noticedatetime,noticelastdatetime,noticestr,paytime createtime from t_pay_record r where ");
		List<Object> param  = new ArrayList<Object>();
		if(record!=null) {
			if(StringUtils.isNotBlank(record.getOrderno())) {
				strBuilder.append(" and r.orderno = ? ");
				param.add(record.getOrderno());
			}
			if(StringUtils.isNotBlank(record.getPayresult())) {
				strBuilder.append(" and r.payresult = ? ");
				param.add(record.getPayresult());
			}
			if(StringUtils.isNotBlank(record.getNoticeresult())) {
				strBuilder.append(" and r.noticeresult = ? ");
				param.add(record.getNoticeresult());
			}
			if(record.getNoticetimes()!=null) {
				strBuilder.append(" and r.noticetimes <= ?  ");
				param.add(record.getNoticetimes());
			}
			if(StringUtils.isNotBlank(record.getReturnpayresult())) {
				if("FAIL".equals(record.getReturnpayresult())) {
					strBuilder.append(" and ( r.returnpayresult is null  or r.returnpayresult != ? )");
					param.add(record.getReturnpayresult());
				}else {
					strBuilder.append(" and r.returnpayresult = ?  ");
					param.add(record.getReturnpayresult());
				}
			}
		}
		return super.pagedQuerySql(strBuilder.toString(), page, pagesize, PayVo.class,param.toArray());
	}
}
