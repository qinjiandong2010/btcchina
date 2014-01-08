package com.btcoin.common;

import redis.clients.jedis.Jedis;

import com.btcoin.utils.PropertiesUtil;

public class JredisManager {
	
	private Jedis jedis;
	
	private static JredisManager jredisManager;
	
	private static final Object lock = new Object();
	
	private JredisManager(){
		String host = PropertiesUtil.getProperty(EnumConfig.FILE_SYSCONF_PROPERTIES.getName(),EnumConfig.redis_host.getName(), "127.0.0.1");
		int port = PropertiesUtil.getPropertyInt(EnumConfig.FILE_SYSCONF_PROPERTIES.getName(),EnumConfig.redis_port.getName(), 6379);
		jedis = new Jedis(host,port);
	}
	
	public void setJedis(Jedis jedis){
		this.jedis = jedis;
	}
	
	public Jedis getJedis(){
		return this.jedis;
	}
	
	/**
	 * 创建Jredis管理实例
	 * @return
	 */
	public static JredisManager getInstance(){
		if( null == jredisManager ){
			synchronized (lock) {
				if( null == jredisManager ){
					jredisManager = new JredisManager();
				}
			}
		}
		return jredisManager;
	}
}
