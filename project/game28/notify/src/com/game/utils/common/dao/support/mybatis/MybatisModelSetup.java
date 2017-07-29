package com.game.utils.common.dao.support.mybatis;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.game.utils.common.dao.support.ModelSetup;


public class MybatisModelSetup implements ModelSetup {
    private String sqlName;//namedquery name  by query

    private String countName;// namedquery name by count
    private Map<String,Object> parameters=new HashMap<String,Object>();

    public void addParameter(String namedParameter,Object value){
        this.parameters.put(namedParameter,value);
    }
    public Map getParameters(){
        return this.parameters.size() > 0 ? this.parameters : null;
    }
    public void setup(Map<String,Object> params){
        Set<String> keys = params.keySet();
            for (String key : keys) {
                Object value = params.get(key);
                if (value!=null)
                    parameters.put(key,value);


            }

    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }


    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

    
}