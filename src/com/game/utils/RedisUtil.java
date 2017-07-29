/******************************
 * 版权所有： 
 * 创建日期:  2017年5月17日
 * 创建作者： wrong1111
 * 文件名称：RedisUtil.java
 * 版本： V1.0
 * 功能：  
 * 最后修改时间：
 * 修改记录：
 *****************************************/
package com.game.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author wrong1111
 *
 */
public class RedisUtil {
	public static Logger log = Logger.getLogger(RedisUtil.class);
	public static final int MAX_EXPRIE_TMP = 10 *60;
	public static Integer MAX_EXPRIE = 24 * 60 * 60;// 失效最大时间，2小时
	
	private static int DEFAULT_DB_INDEX = 0;
	private static int DB_INDEX_1 = 1;
	private static JedisPool jedisPool = null;

	private static Gson gson = new Gson();

	private static final String ClassName = "CN";
	private static final String ObjectKey = "OBJK";
	private static final String EXPIRE_SECONDS = "expireSeconds";
	private static final String TIMESTAMP = "timestamp";

	/**
	 * 初始化redis连接池
	 */
	public synchronized static void init() {
		try {
			if (jedisPool == null) {

				// 配置如下的4个参数就够了。
				JedisPoolConfig config = new JedisPoolConfig();
				// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
				// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
				config.setMaxTotal(2048);
				// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
				config.setMaxIdle(10);
				// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
				config.setMaxWaitMillis(10000L);
				// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
				config.setTestOnBorrow(true);
				String host = PropertiesUtil.getValue("redis.host");
				String port = PropertiesUtil.getValue("redis.port");
				String password = PropertiesUtil.getValue("redis.password");
				jedisPool = new JedisPool(config, host, Integer.valueOf(port), 10000, password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得redis实例
	 */
	public static synchronized Jedis getJedis() {
		if(jedisPool == null){
			init();
		}
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				resource.select(DEFAULT_DB_INDEX);
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}

	private static String objectToJSONString(Object val, Integer seconds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ObjectKey, gson.toJson(val));
		map.put(ClassName, val.getClass().getName());
		map.put(EXPIRE_SECONDS, seconds);
		map.put(TIMESTAMP, new Date().getTime());
		return gson.toJson(map);
	}

	@SuppressWarnings("unchecked")
	private static Object jsonStringToObject(String value) throws Exception {
		Map<String, Object> map = gson.fromJson(value, new HashMap<String, Object>().getClass());
		Object obj = map.get(ObjectKey);
		if (obj == null) {
			return null;
		}
		Integer seconds = ((Double) map.get(EXPIRE_SECONDS)).intValue();
		if (seconds != null) {
			Long timestamp = ((Double) map.get(TIMESTAMP)).longValue();
			Long now = new Date().getTime();
			if ((timestamp + (seconds.longValue() * 1000)) < now) {
				// 过期
				throw new Exception("the value has expire,but not expire in redis...");
			}
		}

		String objStr = (String) obj;
		String className = (String) map.get(ClassName);
		return gson.fromJson(objStr, Class.forName(className));
	}

	// 删除
	public static void delete(String... keys) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				if (keys != null) {
					jedis.del(keys);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	/**
	 * 值设置到redis中
	 * 
	 * @param key
	 * @param val
	 * @param seconds
	 *            单位秒
	 */
	public static void putString(String key, String val, Integer seconds) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				jedis.set(key, val);
				if (seconds != null) {
					jedis.expire(key, seconds);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	/**
	 * 值设置到redis中
	 * 
	 * @param key
	 * @param val
	 * @param seconds
	 *            单位秒
	 */
	public static void set(String key, Object val, Integer seconds) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				jedis.set(key, objectToJSONString(val, seconds));
				if (seconds != null) {
					jedis.expire(key, seconds);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 新增将hash值设置到redis中
	 * 
	 * @param key
	 * @param val
	 * @param seconds
	 *            单位秒
	 */
	public static void hset(String key, String field, String val, Integer seconds) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				jedis.select(DB_INDEX_1);
				jedis.hset(key, field, val);
				if (seconds != null) {
					jedis.expire(key, seconds);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	/**
	 * 获取值
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				String str = jedis.get(key);
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				return str;
			}
			return null;
		} catch (Exception e) {
			if (jedis.exists(key)) {
				delete(key);
			}
			return null;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 获取值
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				String str = jedis.get(key);
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				return jsonStringToObject(str);
			}
			return null;
		} catch (Exception e) {
			if (jedis.exists(key)) {
				delete(key);
			}
			return null;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 根据key field 获取hash值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static String hget(String key, String field) {
		Jedis jedis = getJedis();
		try {
			if (jedis != null) {
				jedis.select(DB_INDEX_1);
				String str = jedis.hget(key, field);
				if (StringUtils.isEmpty(str)) {
					return null;
				}
				return str;
			}
			return null;
		} catch (Exception e) {
			if (jedis.exists(key)) {
				delete(key);
			}
			return null;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 获取hash值
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, String> hgetAll(String key) {
		Jedis jedis = getJedis();
		Map<String, String> str = new HashMap<String, String>();
		try {
			if (jedis != null) {
				jedis.select(DB_INDEX_1);
				str = jedis.hgetAll(key);
				if (StringUtils.isEmpty(str.toString())) {
					return null;
				}
				return str;
			}
			return str;
		} catch (Exception e) {
			if (jedis.exists(key)) {
				delete(key);
			}
			return null;
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 获取key模糊查询得到的数组
	 * 
	 * @param key
	 * @return
	 */
	public static Set<String> keys(String key) {
		Jedis jedis = getJedis();
		Set<String> str = new HashSet<String>();
		try {
			if (jedis != null) {
				jedis.select(DB_INDEX_1);
				str = jedis.keys(key);
				if (null == str || str.size() == 0) {
					return null;
				}
				return str;
			}
			return str;
		} catch (Exception e) {
			if (jedis.exists(key)) {
				delete(key);
			}
			return null;
		} finally {
			returnResource(jedis);
		}
	}
	 
}
