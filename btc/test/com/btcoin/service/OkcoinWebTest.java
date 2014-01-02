package com.btcoin.service;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.btcoin.common.Resp;
import com.btcoin.service.impl.ChbtcWeb;
import com.btcoin.service.impl.OkcoinWeb;

public class OkcoinWebTest {
	private static Logger log = Logger.getLogger(OkcoinWebTest.class);
	@Test
	public void login() throws IOException{
		AbstractBtcWeb web = new OkcoinWeb();
		Resp result = web.login("smallbeetle@163.com", "smallbeetle");
		log.info(result.getMessage());
		Assert.assertSame(result.getRecode(), 0);
	}
	@Test
	public void getTicker() throws IOException{
		AbstractBtcWeb web = new OkcoinWeb();
		Resp result = web.getTicker();
		log.info(result.getResult());
	}
	@Test
	public void getMarketDepth() throws IOException{
		AbstractBtcWeb web = new OkcoinWeb();
		Resp result = web.getMarketDepth(null);
		log.info(result.getResult());
	}
}
