package com.btcoin.service;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.btcoin.common.Resp;
import com.btcoin.service.impl.BTCChinaWeb;

public class BTCChinaWebTest {
	private static Logger log = Logger.getLogger(BTCChinaWebTest.class);
/*	@Test
	public void login() throws IOException{
		BTCChinaWeb btcchinaWeb = new BTCChinaWeb();
		Resp result = btcchinaWeb.login("smallbeetle", "smallbeetle");
		log.info(result.getMessage());
		Assert.assertSame(result.getRecode(), "0");
	}
	@Test
	public void getMarketDepth() throws IOException{
		JSONObject params = new JSONObject();
		params.put("username", "btcchinatest");
		
		AbstractBTCWeb web = new BTCChinaWeb();
		Resp result = web.getMarketDepth(params);
		log.info(result.toString());
	}
	@Test
	public void getOrders() throws IOException{
		JSONObject params = new JSONObject();
		params.put("username", "smallbeetle");
		
		AbstractBTCWeb web = new BTCChinaWeb();
		Resp result = web.getOrders(false,params);
		log.info(result.toString());
	}
	@Test
	public void cancelOrder() throws IOException{
		JSONObject params = new JSONObject();
		params.put("username", "smallbeetle");
		
		AbstractBTCWeb web = new BTCChinaWeb();
		Resp result = web.cancelOrder(123123213,params);
		log.info(result.toString());
	}
	@Test
	public void getTicker() throws IOException{
		AbstractBTCWeb web = new BTCChinaWeb();
		Resp result = web.getTicker();
		log.info(result.getResult());
	}*/
	@Test
	public void buyOrder() throws IOException{
		JSONObject params = new JSONObject();
		params.put("username", "smallbeetle");
		params.put("password", "smallbeetle");
		params.put("tradepwd", "smallbeetle");
		//params.put("ordertype", "market"); //
		
		AbstractBTCWeb btcchinaWeb = new BTCChinaWeb();
		Resp result = btcchinaWeb.buyOrder(5225, 1,params);
		log.info(result.getMessage());
	}
	@Test
	public void login() throws IOException{
		BTCChinaWeb btcchinaWeb = new BTCChinaWeb();
		Resp result = btcchinaWeb.login("smallbeetle", "smallbeetle");
		log.info(result.getMessage());
		Assert.assertSame(result.getRecode(), "0");
	}
}
