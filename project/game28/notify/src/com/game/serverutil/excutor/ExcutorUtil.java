package com.game.serverutil.excutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcutorUtil {
	
	public static ExecutorService pool = Executors.newFixedThreadPool(4);
	
	public static void exec(Runnable runnable){
		pool.submit(runnable);
	}

}
