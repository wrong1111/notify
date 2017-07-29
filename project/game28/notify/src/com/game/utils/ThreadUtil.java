package com.game.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

	public static ExecutorService  pool = Executors.newFixedThreadPool(8);
}
