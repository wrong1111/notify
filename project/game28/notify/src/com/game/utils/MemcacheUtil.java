package com.game.utils;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

public class MemcacheUtil {
	public static Log log = LogFactory.getLog(MemcacheUtil.class);
	private static MemCachedClient cachedClient = new MemCachedClient();
	public static final int MAX_EXPRIE_TMP = 10 *60;
	public static String[] address = null;
	public static Integer MAX_EXPRIE = 24 * 60 * 60;// 失效最大时间，2小时。
	public static String memcached_url_key = "memcached.serverlist"; // memcached服务器地址
	public synchronized static void init() {
		if (address == null) {
			String strs = Constants.getConfigkey(memcached_url_key);;
			//String strs="183.60.143.199:11211,183.60.143.199:11211";
			address = strs.split("\\,");
		}
		// 获取连接池的实例
		SockIOPool pool = SockIOPool.getInstance();
		// 服务器列表及其权重
		String[] servers = address;
		if (servers != null) {
			Integer[] weights = { 3 };

			// 设置服务器信息
			pool.setServers(servers);
			pool.setWeights(weights);

			// 设置初始连接数、最小连接数、最大连接数、最大处理时间
			pool.setInitConn(10);
			pool.setMinConn(10);
			pool.setMaxConn(10000);
			pool.setMaxIdle(1000 * 60 * 60);

			// 设置连接池守护线程的睡眠时间
			pool.setMaintSleep(60);

			// 设置TCP参数，连接超时
			pool.setNagle(false);
			pool.setSocketTO(60);
			pool.setSocketConnectTO(0);

			// 初始化并启动连接池
			pool.initialize();
		}

	}

	public MemcacheUtil() {
	}

	public  static boolean add(String key, Object value) {
		if (address == null) {
			init();
		}
		return cachedClient.add(key, value);
	}

	public  static boolean add(String key, Object value, Integer expire) {
		if (address == null) {
			init();
		}
		return cachedClient.add(key, value, expire);
	}

	public  static boolean put(String key, Object value) {
		if (address == null) {
			init();
		}
		long a = System.currentTimeMillis();
		boolean is = cachedClient.set(key, value,MAX_EXPRIE);
		log.info("address is " + address[0] + "向Memcached存值,key:" + key + "成功与否:"+is+",用时"
				+ (System.currentTimeMillis() - a));
		return is;
	}
	public  static boolean put(String key, Object value,Date expire) {
		if (address == null) {
			init();
		}
		long a = System.currentTimeMillis();
		boolean is = cachedClient.set(key, value,expire);
		log.info("address is " + address[0] + "向Memcached存值,key:" + key + "成功与否:"+is+",用时"
				+ (System.currentTimeMillis() - a));
		return is;
	}
	public  static boolean put(String key, Object value, Integer expire) {
		if (address == null) {
			init();
		}
		long a = System.currentTimeMillis();
		boolean is  = cachedClient.set(key, value, expire);
		log.info("address is " + address[0] + "向Memcached存值,key:" + key + "成功与否:"+is+",用时"+ (System.currentTimeMillis() - a));
		return is;
	}

	 
	public  static boolean replace(String key, Object value) {
		if (address == null) {
			init();
		}
		return cachedClient.replace(key, value,MAX_EXPRIE);
	}

	public  static boolean replace(String key, Object value, Integer expire) {
		if (address == null) {
			init();
		}
		return cachedClient.replace(key, value, expire);
	}
	
	public  synchronized static Object getSyn(String key) {
		long a = System.currentTimeMillis();
		if (address == null) {
			init();
		}
		Object obj = cachedClient.get(key);
		log.info("address is" + address[0] + "从Memcached中取值,key:" + key + " 成功与否:"+(null==obj?"false":"true")+",用时"
				+ (System.currentTimeMillis() - a));
		return obj;
	}
	public  static Object get(String key) {
		long a = System.currentTimeMillis();
		if (address == null) {
			init();
		}
		Object obj = cachedClient.get(key);
		log.info("address is" + address[0] + "从Memcached中取值,key:" + key + " 成功与否:"+(null==obj?"false":"true")+",用时"
				+ (System.currentTimeMillis() - a));
		return obj;
	}

	public  static boolean delete(String key) {
		if (address == null) {
			init();
		}
		return cachedClient.delete(key);
	}

	public  static void main(String[] s) {

		//MemcacheUtil.put("aa", "ddd",new Date(30*1000));
		//MemcacheUtil.put("bb", "dddd");
		//System.out.println(MemcacheUtil.get("aa"));
		//System.out.println(MemcacheUtil.get("bb"));
		//MemcacheUtil.put("f54634f1959279d5230d77832505b537_base", "abb");
		//System.out.println(MemcacheUtil.get("f54634f1959279d5230d77832505b537_base"));
		// MemcacheUtil.delete("f54634f1959279d5230d77832505b537_base");

	}
}
