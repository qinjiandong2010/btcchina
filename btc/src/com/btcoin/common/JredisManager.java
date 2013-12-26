package com.btcoin.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

public class JredisManager {
	
	private Jedis jedis;
	
	private static JredisManager jredisManager;
	
	private static final Object lock = new Object();
	
	private JredisManager(){
		jedis = new Jedis("localhost",6379);
	}
	
	public void setJedis(Jedis jedis){
		this.jedis = jedis;
	}
	
	public Jedis getJedis(){
		return this.jedis;
	}
	
	/**
	 * ��ȡһ��redist clientl��
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
	public static void main(String []s){
		JredisManager redisManager = JredisManager.getInstance();
        String result = redisManager.getJedis().hget("btcchina", "user:btcchinatest:cookies");
        JSONArray cookies = JSONArray.fromObject(result);
        for (Object obj : cookies) {
			JSONObject cookie = (JSONObject) obj;
			System.out.println(cookie.getString("name")+"="+cookie.getString("value"));
		}
	}
}
