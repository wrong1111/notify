package com.game.serverutil.excutor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.pojo.NotifyVo;
import com.game.service.PayService;
import com.game.utils.PropertiesUtil;


@Component("notifyQueue")
public class NotifyQueue implements Serializable {
 
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	PayService payService;
	
	@Autowired
	NotifyParam notifyParam;
	
	private static final long serialVersionUID = 1L;
	/**
     * 将传过来的对象进行通知次数判断，之后决定是否放在任务队列中
     *
     * @param notifyRecord
     * @throws Exception
     */
    public void addElementToList(NotifyVo notifyRecord) {
        if (payService == null) {
            return;
        }
        Integer notifyTimes = notifyRecord.getNotifyTimes(); // 通知次数
        Integer maxNotifyTime = 0;
        try {
            maxNotifyTime = notifyParam.getMaxNotifyTime();
        } catch (Exception e) {
            logger.error(e);
        }
        if(notifyRecord.getNotifyTimes() == null) {
        	notifyRecord.setNotifyTimes(0);
        }
        if (notifyRecord.getNotifyTimes() == 0) {// 刚刚接收到的数据
            notifyRecord.setLastNotifyTime(new Date());
        }
        long time = notifyRecord.getLastNotifyTime().getTime();
        Map<Integer, Integer> timeMap = notifyParam.getNotifyParams();
        if (notifyTimes < maxNotifyTime) {
            Integer nextKey = notifyTimes + 1;
            Integer next = timeMap.get(nextKey);
            if (next != null) {
                time += 1000 * 60 * next + 1;
                notifyRecord.setLastNotifyTime(new Date(time));
                NotifyThread.tasks.put(new NotifyTask(payService,notifyRecord, this, notifyParam));
            }
        } else {
            try {
                // 持久化到数据库中
                payService.updateNotify(notifyRecord.getMerchantOrderNo(), notifyRecord.getNotifyTimes(),notifyRecord.getLastNotifyTime(),"FAIL");
                logger.error("notify--NotifyRecord failed,merchantNo:" + notifyRecord.getMerchantNo() + ",merchantOrderNo:"
                        + notifyRecord.getMerchantOrderNo());
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
}
