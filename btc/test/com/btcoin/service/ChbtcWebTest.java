package com.btcoin.service;

import java.io.IOException;

import junit.framework.Assert;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Test;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.btcoin.common.Resp;
import com.btcoin.exception.BtcoinException;
import com.btcoin.service.impl.ChbtcWeb;

public class ChbtcWebTest {
	private static Logger log = Logger.getLogger(ChbtcWebTest.class);
	@Test
	public void login(){
		AbstractBtcWeb web = new ChbtcWeb();
		try {
			Resp result = web.login("smallbeetle", "smallbeetle");
			log.info(result.getMessage());
			Assert.assertSame(result.getRecode(), 0);
		} catch (BtcoinException err) {
			log.error("BTC API内部异常:"+err.getMessage());
		} catch (JedisConnectionException err){
			log.error("Redis 连接异常:"+err.getMessage());
		}catch (IOException err) {
			log.error("未知异常",err);
		}
	}
	@Test
	public void buyOrder() throws IOException{
		JSONObject params = new JSONObject();
		params.put("username", "smallbeetle");
		params.put("password", "smallbeetle");
		params.put("tradepwd", "smallbeetle");
		
		AbstractBtcWeb web = new ChbtcWeb();
		Resp result = web.buyOrder(5225, 1,params);
		log.info(result.getMessage());
	}
	@Test
	public void sellOrder() throws IOException{
		JSONObject params = new JSONObject();
		params.put("username", "smallbeetle");
		params.put("password", "smallbeetle");
		params.put("tradepwd", "smallbeetle");
		
		AbstractBtcWeb web = new ChbtcWeb();
		Resp result = web.sellOrder(5225, 1,params);
		log.info(result.getMessage());
	}
	@Test
	public void getTicker() throws IOException{
		AbstractBtcWeb web = new ChbtcWeb();
		Resp result = web.getTicker();
		log.info(result.getResult());
	}
	@Test
	public void getMarketDepth() throws IOException{
		AbstractBtcWeb web = new ChbtcWeb();
		Resp result = web.getMarketDepth(null);
		log.info(result.getResult());
	}
}
