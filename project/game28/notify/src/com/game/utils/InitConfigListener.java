package com.game.utils;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class InitConfigListener extends ContextLoaderListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("--------contextInitialized......");
		super.contextInitialized(event);
		try {
			 
				
			DBhelper.init();
			System.out.println("----------config&parameter....inited-["+Constants.configmap.size()+"|"+Constants.parametermap.size()+"]");
			
			MemcacheUtil.init();
			System.out.println("-------TSysPartner....inited-["+Constants.partnermap.size()+"]");
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
