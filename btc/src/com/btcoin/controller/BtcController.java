package com.btcoin.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.btcoin.common.Resp;
import com.btcoin.service.AbstractBtcWeb;
import com.btcoin.service.BtcWebFactory;

@Controller
@RequestMapping("/rest")
public class BtcController {

	@RequestMapping(value="/{market}/ticker",method=RequestMethod.GET)
	public @ResponseBody Resp getTicker(@PathVariable String market)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		return btcWeb.getTicker();
	}
	@RequestMapping(value="{market}/buyOrder",method=RequestMethod.GET)
	public @ResponseBody Resp buyOrder(@PathVariable String market)throws IOException{
		return null;
	}
	@RequestMapping(value="{market}/sellOrder",method=RequestMethod.GET)
	public @ResponseBody Resp sellOrder(@PathVariable String market)throws IOException{
		return null;
	}
	@RequestMapping(value="{market}/cancelOrder",method=RequestMethod.GET)
	public @ResponseBody Resp cancelOrder(@PathVariable String market)throws IOException{
		return null;
	}
	@RequestMapping(value="{market}/getMarketDepth",method=RequestMethod.GET)
	public @ResponseBody Resp getMarketDepth(@PathVariable String market)throws IOException{
		return null;
	}
	@RequestMapping(value="{market}/getOrder",method=RequestMethod.GET)
	public @ResponseBody Resp getOrder(@PathVariable String market)throws IOException{
		return null;
	}
	@RequestMapping(value="{market}/getOrders",method=RequestMethod.GET)
	public @ResponseBody Resp getOrders(@PathVariable String market)throws IOException{
		return null;
	}
}
