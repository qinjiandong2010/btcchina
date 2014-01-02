package com.btcoin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.btcoin.common.ErrorCode;
import com.btcoin.common.Resp;
import com.btcoin.service.AbstractBtcWeb;
import com.btcoin.service.BtcWebFactory;
import com.btcoin.utils.StringUtil;

@Controller
@RequestMapping("/rest")
public class BtcController {

	@RequestMapping(value="/{market}/ticker",method=RequestMethod.GET)
	public @ResponseBody Resp getTicker(@PathVariable String market)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		return btcWeb.getTicker();
	}
	@RequestMapping(value="{market}/buyOrder",method=RequestMethod.POST)
	public @ResponseBody Resp buyOrder(@PathVariable String market,HttpServletRequest request)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		JSONObject params = JSONObject.fromObject(request.getParameterMap());
		if( !StringUtil.isJSONObjectIsDouble(params, "price") ){
			new Resp(ErrorCode.price_require, "价格是必须项");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "amount") ){
			new Resp(ErrorCode.amount_require, "数量是必须项");
		}
		double price = (Double)params.remove("price");
		double amount = (Double)params.remove("amount");
		return btcWeb.buyOrder(price, amount, params);
	}
	@RequestMapping(value="{market}/sellOrder",method=RequestMethod.POST)
	public @ResponseBody Resp sellOrder(@PathVariable String market,HttpServletRequest request)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		JSONObject params = JSONObject.fromObject(request.getParameterMap());
		if( !StringUtil.isJSONObjectIsDouble(params, "price") ){
			new Resp(ErrorCode.price_require, "价格是必须项");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "amount") ){
			new Resp(ErrorCode.amount_require, "数量是必须项");
		}
		double price = (Double)params.remove("price");
		double amount = (Double)params.remove("amount");
		return btcWeb.sellOrder(price, amount, params);
	}
	@RequestMapping(value="{market}/getMarketDepth",method=RequestMethod.GET)
	public @ResponseBody Resp getMarketDepth(@PathVariable String market,HttpServletRequest request)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		JSONObject params = JSONObject.fromObject(request.getParameterMap());
		return btcWeb.getMarketDepth(params);
	}
	@RequestMapping(value="{market}/cancelOrder",method=RequestMethod.POST)
	public @ResponseBody Resp cancelOrder(@PathVariable String market)throws IOException{
		return null;
	}
	@RequestMapping(value="{market}/getOrders",method=RequestMethod.GET)
	public @ResponseBody Resp getOrders(@PathVariable String market)throws IOException{
		return null;
	}
}
