package com.game.utils;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class InitConfigListener extends ContextLoaderListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		long a = System.currentTimeMillis();
		System.out.println("--------contextInitialized......");
		super.contextInitialized(event);
		try {
			
			DBhelper.init();
			System.out.println("----------config&parameter....inited-["+Constants.configmap.size()+"|"+Constants.parametermap.size()+"]");
			
			MemcacheUtil.init();
			System.out.println("-------memcacheUtil....inited ");
			 
//			new NotifyThread().startThread();
			System.out.println("-------notifyThread....inited----->cost:"+(System.currentTimeMillis()-a)/1000+"s ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
