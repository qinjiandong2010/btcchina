package com.btcoin.service;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.btcoin.common.Resp;
import com.btcoin.service.impl.BtcchinaWeb;

public class BtcchinaWebTest {
	private static Logger log = Logger.getLogger(BtcchinaWebTest.class);
	@Test
	public void login() throws IOException{
		BtcchinaWeb btcchinaWeb = new BtcchinaWeb();
		Resp result = btcchinaWeb.login("btcchinatest", "123456789");
		log.info(result.toJSONString());
		Assert.assertSame(result.getResult(), "0");
	}
	
	@Test
	public void buyOrder() throws IOException{
		JSONObject params = new JSONObject();
		params.put("username", "btcchinatest");
		params.put("password", "123456789");
		params.put("tradepwd", "123456789");
		params.put("ordertype", "market"); //
		
		BtcchinaWeb btcchinaWeb = new BtcchinaWeb();
		Resp result = btcchinaWeb.buyOrder(5225, 1,params);
		log.info(result.toJSONString());
	}
}
