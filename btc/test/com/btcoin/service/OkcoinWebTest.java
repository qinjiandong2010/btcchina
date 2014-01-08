package com.btcoin.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.btcoin.common.Resp;
import com.btcoin.service.impl.OKCoinWeb;

public class OKCoinWebTest {
	private static Logger log = Logger.getLogger(OKCoinWebTest.class);
	@Test
	public void login() throws IOException{
		AbstractBTCWeb web = new OKCoinWeb();
		Resp result = web.login("smallbeetle@163.com", "smallbeetle");
		log.info(result.getMessage());
		Assert.assertSame(result.getRecode(), 0);
	}
	@Test
	public void getTicker() throws IOException{
		AbstractBTCWeb web = new OKCoinWeb();
		Resp result = web.getTicker();
		log.info(result.getResult());
	}
	@Test
	public void getMarketDepth() throws IOException{
		AbstractBTCWeb web = new OKCoinWeb();
		Resp result = web.getMarketDepth(null);
		log.info(result.getResult());
	}
}
