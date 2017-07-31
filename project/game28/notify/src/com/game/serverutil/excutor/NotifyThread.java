package com.game.serverutil.excutor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.game.pojo.NotifyVo;
import com.game.service.PayService;
import com.game.utils.common.dao.support.Page;


public class NotifyThread {
	
	public static DelayQueue<NotifyTask> tasks = new DelayQueue<NotifyTask>();
	 
	 
	private ThreadPoolTaskExecutor taskExecutor ; 
	
	 Logger logger = Logger.getLogger(NotifyThread.class);
	
	public  void startThread() {
        logger.error("notify-thread-startThread");
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setKeepAliveSeconds(300);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        taskExecutor.execute(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(50);//50毫秒执行一次
                        // 如果当前活动线程等于最大线程，那么不执行
                        if (taskExecutor.getActiveCount() < taskExecutor.getMaxPoolSize()) {
                        	 logger.error("noitfy-thread-task 队列长度:"+ tasks.size()+"--");
                            final NotifyTask task = tasks.poll();
                            if (task != null) {
                            	taskExecutor.execute(new Runnable() {
                                    public void run() {
                                        logger.error("noitfy-thread-"+taskExecutor.getActiveCount() + "---------");
                                        tasks.remove(task);
                                        task.run();
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                	logger.error("noitfy-thread-系统异常",e);
                    e.printStackTrace();
                }
            }
        });
        startFromDb();
    }

	private void startFromDb() {
		
		   int pageNum = 1;
		   int numPerPage = 200;
		   PayService payService  = (PayService)SpringContext.getBean("payService");
		   NotifyQueue notifyQueue = (NotifyQueue)SpringContext.getBean("notifyQueue");
		   Page page =  payService.findNotify2Send("notify", pageNum, numPerPage);
		    int totalSize = (page.getTotalPageCount()-1)/page.getPageSize()+1;//总页数
	        while (pageNum <= totalSize) {
	            List<NotifyVo> list = page.getResult();
	            for (int i = 0; i < list.size(); i++) {
	                NotifyVo notifyRecord = list.get(i);
	                notifyRecord.setLastNotifyTime(new Date());
	                notifyQueue.addElementToList(notifyRecord);
	            }
	            pageNum++;
	            if(logger.isInfoEnabled()) {
	            	logger.info(String.format("调用通知服务.payService.findNotify2Send(%s, %s,)", pageNum, numPerPage));
	            }
	            page = payService.findNotify2Send("notify",pageNum,numPerPage);
	        }
	}
}
