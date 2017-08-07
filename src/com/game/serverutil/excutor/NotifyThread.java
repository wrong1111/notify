package com.game.serverutil.excutor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.game.pojo.NotifyVo;
import com.game.service.PayService;
import com.game.utils.common.dao.support.Page;

@Component("notifyThread")
public class NotifyThread {
	Logger logger = Logger.getLogger(NotifyThread.class);
	
	public static DelayQueue<NotifyTask> tasks = new DelayQueue<NotifyTask>();
 
	@Autowired
	PayService payService;
	
	@Autowired
	NotifyQueue notifyQueue;
	
	 /**
	  * 守护线程
	  */
	private Thread daemonThread;
	public void init() {
        daemonThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						System.out.println("DeamonThread is sleeping 5s...");
						Thread.sleep(10000);
						runTask();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
        	
        });
        daemonThread.setDaemon(true);
        daemonThread.setName("DelayQueueTask");
        daemonThread.start();
	}
	
	private void runTask() {
		System.out.println("runTask-----");
		ThreadPoolTaskExecutor taskE = (ThreadPoolTaskExecutor) SpringContext.getBean("taskExecutor");
		 // 如果当前活动线程等于最大线程，那么不执行
        if (taskE.getActiveCount() < taskE.getMaxPoolSize()) {
        	System.out.println("noitfy-thread-task>>>>>>>>> 队列长度:"+ tasks.size()+"--");
            final NotifyTask task = tasks.poll();
            if (task != null) {
            	taskE.execute(new Runnable() {
                    public void run() {
                        logger.error("noitfy-thread-"+taskE.getActiveCount() + "---------");
                        tasks.remove(task);
                        task.run();
                    }
                });
            }
        }
	}
	public  void startThread() {
        logger.error("notify-thread-startThread");
        init();
        startFromDb();
    }

	private void startFromDb() {
		   int pageNum = 1;
		   int numPerPage = 200;
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
