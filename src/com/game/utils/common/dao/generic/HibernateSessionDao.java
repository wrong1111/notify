package com.game.utils.common.dao.generic;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

public abstract class HibernateSessionDao  {

	@Resource
	private SessionFactory sessionFacotry;

	
	public void setSessionFacotry(SessionFactory sessionFacotry) {
		this.sessionFacotry = sessionFacotry;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFacotry;
	}
}
