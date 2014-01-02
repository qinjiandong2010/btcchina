package com.btcoin.service;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.btcoin.common.Resp;
import com.btcoin.service.impl.ChbtcWeb;

public class ChbtcWebTest {
	private static Logger log = Logger.getLogger(ChbtcWebTest.class);
	@Test
	public void login() throws IOException{
		AbstractBtcWeb web = new ChbtcWeb();
		Resp result = web.login("smallbeetle", "smallbeetle");
		log.info(result.getMessage());
		Assert.assertSame(result.getRecode(), 0);
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
}
