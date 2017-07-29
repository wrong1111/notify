package com.game.utils.common.dao.support.hibernate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.CollectionUtils;

import com.game.utils.common.dao.support.ModelSetup;


public class HibernateModelSetup implements ModelSetup {
    /**
     * 由业务传入多个查询条件
     * 由Restrictions类实现查询条件封装到List对象中
     */
    private List<Criterion> criterions = new ArrayList<Criterion>();

    /**
     * A List<Order> variable :排序条件
     */
    private List<Order> sorts = new ArrayList<Order>();

    /**
     * 简单的过虑条件
     */
    Map<String, Object> filters = new HashMap<String, Object>();
    
    /**
     * hql语句
     */
    String hql = null;
    
    /**
     * sql语句
     */
    String sql = null;
    
    /**
     * hql或者sql语句的可变参数
     */
    Object[] params = {};


    public void setup(Map<String, Object> params) {
        this.setFilters(params);
    }

    public void setup(Criteria criteria) {

        //得到简单条件
        if (filters != null && !filters.isEmpty()) {
            Set<String> keys = filters.keySet();
            for (String key : keys) {
                Object value = filters.get(key);
                if (StringUtils.isNotBlank(value.toString()))
                    criteria.add(Restrictions.eq(key, value));
            }
        }

        //多个查询条件
        List<Criterion> lsCris = getCriterions();
        if (lsCris != null && lsCris.size() > 0) {
            for (Criterion criter : lsCris) {
                criteria.add(criter);
            }
        }

        //多个排序条件
        List<Order> sorts = getSorts();
        if (!CollectionUtils.isEmpty(sorts)) {
            for (Order sort : sorts) {
                criteria.addOrder(sort);
            }
        }
    }

    public List<Criterion> getCriterions() {
        return criterions;
    }

    public void setCriterions(List<Criterion> criterions) {
        this.criterions = criterions;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public List<Order> getSorts() {
        return sorts;
    }

    public void setSorts(List<Order> sorts) {
        this.sorts = sorts;
    }

    public void addCriterion(Criterion criterion) {
        this.getCriterions().add(criterion);
    }

    public void addSort(Order sort) {
        this.getSorts().add(sort);
    }

    public void addFilter(String key, Object value) {
        this.getFilters().put(key, value);
    }

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
}
