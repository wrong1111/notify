package com.game.utils.common.dao.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.game.utils.common.dao.IEntityDao;
import com.game.utils.common.dao.generic.MybatisGenericDao;
import com.game.utils.common.dao.support.ModelSetup;
import com.game.utils.common.dao.support.Page;
import com.game.utils.common.dao.support.mybatis.MybatisModelSetup;
import com.game.utils.common.domain.entity.Entity;
import com.game.utils.common.clazz.GenericsUtils;
import com.game.utils.common.clazz.MybatisUtils;
/**
 * dao实现类的基类
 * @author XiaYong
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class MybatisEntityDao<T> extends MybatisGenericDao implements IEntityDao<T> {

    private static final String INSERT = ".insertSelective";
    private static final String UPDATE = ".updateByPrimaryKeySelective";
    //private static final String DELETE = ".delete";
    private static final String DELETEBYID = ".deleteByPrimaryKey";
    private static final String GET = ".selectByPrimaryKey";
    private static final String GETALL = ".selectByExample";

    public Class<T> getEntityClass() {
        if (entityClass == null)
            entityClass = (Class<T>) GenericsUtils.getSuperClassGenricType(getClass());
        return entityClass;
    }

    protected Class<T> entityClass;

    protected String getMapperName() {
        return MybatisUtils.getMapperName(getEntityClass());
    }

    public T get(Serializable id) {
        String name = getMapperName() + GET;
        return (T) super.get(name, id);
    }

    public List<T> getAll() {

        String name = getMapperName() + GETALL;
        return this.getAll(name);
    }

    public List<T> getAll(ModelSetup modelSetup) {
        MybatisModelSetup model = (MybatisModelSetup) modelSetup;
        String name = model.getSqlName();
        return this.find(name, model.getParameters());
    }

    public int update(Object o) {
        String name = getMapperName() + UPDATE;
        return this.update(name, o);
    }

    public int create(Object o) {
        String name = getMapperName() + INSERT;
        return this.insert(name, o);
    }
    
  //返回的是插入成功的条数
    public Serializable save(Object o) {
    	String name = getMapperName() + INSERT;
    	return this.save(name, o);
    }

    public int remove(Object o) {
        return this.removeById((Serializable) ((Entity)o).getId());
    }

    public int removeById(Serializable id) {
        String name = getMapperName() + DELETEBYID;
        return this.removeById(name, id);
    }
    
    /**
     * 根据mapper中select的id去查询
     * @param id
     * @return
     */
    public List<T> findBySelectId(String id) {
    	Map map = null;
    	return this.find(getMapperName() + "." + id,map);
    }

    /**
     * 请子类重写返回domain的ID的name
     *
     * @param clazz
     * @return
     */
    public String getIdName(Class clazz) {
        return "id";
    }

    /**
     * 通过ModelSetup查询count
     *
     * @param modelSetup
     * @return
     */
    public Integer getCount(ModelSetup modelSetup) {
        MybatisModelSetup model = (MybatisModelSetup) modelSetup;
        String countSqlName = model.getCountName();
        Assert.hasText(countSqlName, "count sql name not null");
        return super.getCount(countSqlName, model.getParameters());
    }

    /**
     * 通过ModelSetup  page
     *
     * @param modelSetup
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page pagedQuery(ModelSetup modelSetup, int pageNo, int pageSize) {
    	MybatisModelSetup model = (MybatisModelSetup) modelSetup;
        String countSqlName = model.getCountName();
        Assert.hasText(countSqlName, "count sql name not null");
        String sqlName = model.getSqlName();
        Assert.hasText(sqlName, "sql name not null");
        return super.pagedQuery(countSqlName, sqlName, ((MybatisModelSetup) modelSetup).getParameters(), pageNo, pageSize);
    }
    
    
    public Page pagedQuery(String sql, int pageNo, int pageSize, boolean isHql) {
    	return null;
    }

	public <T> void saveAll(List<T> objects) {
		//空实现
		
	}
	
	public void evit(Object entity) {
		//空实现
	}
	
	public void clear() {
		//空实现
	}
	
	public <T> void saveOrUpdateAll(List<T> objects) {
		//空实现
		
	}

	public <T> void updateAll(List<T> objects) {
		//空实现
		
	}
}
