package com.btcoin.service.impl;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.btcoin.common.Resp;
import com.btcoin.exception.BtcoinException;
import com.btcoin.service.AbstractBtcWeb;

public class ChbtcWeb extends AbstractBtcWeb {

	@Override
	public Resp login(String username, String password) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp buyOrder(double price, double amount, JSONObject params)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp sellOrder(double price, double amount, JSONObject params)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp cancelOrder(long id, JSONObject params) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp getMarketDepth(long limit, JSONObject params)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp getOrders(double openOnly, JSONObject params)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp getTicker() throws IOException, BtcoinException {
		// TODO Auto-generated method stub
		return null;
	}

}
