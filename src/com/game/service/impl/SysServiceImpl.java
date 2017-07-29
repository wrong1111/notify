package com.game.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.dao.SysDAO;
import com.game.entity.TSysConfig;
import com.game.entity.TSysPartner;
import com.game.service.SysService;
import com.game.utils.common.exception.DaoException;
import com.game.utils.common.exception.ServiceException;

@Service("sysService")
public class SysServiceImpl implements SysService {

	@Autowired	
	SysDAO sysDao;
 

	private final static Logger logger = Logger.getLogger(SysServiceImpl.class);

	public final Map<String, TSysPartner> findAll() throws ServiceException {
		Map<String, TSysPartner> rmap = new HashMap<String, TSysPartner>();
		try {
			List<TSysPartner> rlist = sysDao.findAll();
			if (null != rlist && !rlist.isEmpty()) {
				for (TSysPartner p : rlist) {
					rmap.put(p.getPartnername(), p);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return rmap;
	}

	@Override
	public TSysConfig findByKey(String key) throws ServiceException {
			try {
				return sysDao.findByKey(key);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}

}
