package com.game.serverutil.excutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author wrong1111
 *项目起动时，初始化一些数据。
 */
public class InitBeanFactory implements ApplicationListener<ContextRefreshedEvent>{

	Logger logger  = Logger.getLogger(getClass());
	private static volatile boolean initialled = false;  

	@Autowired
	NotifyThread notifyThread;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		synchronized(InitBeanFactory.class) {  
	        if(event.getApplicationContext().getParent() != null && initialled) {  
	            return;  
	        }  
	        initialled = true;  
	    }  
		notifyThread.startThread();
	}
	 
	  
	 

}
