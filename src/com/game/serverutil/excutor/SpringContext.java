package com.game.serverutil.excutor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SpringContext implements ApplicationContextAware{

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	SpringContext.applicationContext=applicationContext;
    }

    public static Object getBean(String bean){
        return SpringContext.applicationContext.getBean(bean);
    }

}
