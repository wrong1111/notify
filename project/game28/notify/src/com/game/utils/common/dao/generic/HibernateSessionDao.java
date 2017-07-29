package com.game.utils.common.dao.generic;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public abstract class HibernateSessionDao  {

	private SessionFactory sessionFacotry;

	@Autowired
	public void setSessionFacotry(SessionFactory sessionFacotry) {
		this.sessionFacotry = sessionFacotry;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFacotry;
	}
}
