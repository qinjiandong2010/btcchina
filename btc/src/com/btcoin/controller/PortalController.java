package com.btcoin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PortalController{
	
	@RequestMapping("/")
	public String dashboard() {
		return "portal/index";
	}
	@RequestMapping(value="/trade/buy.html", method=RequestMethod.GET)
	public String buyOrder(){
		return "portal/buy_order";
	}
	@RequestMapping(value="/trade/sell.html", method=RequestMethod.GET)
	public String sellOrder(){
		return "portal/sell_order";
	}
	@RequestMapping(value="/trade/orders.html", method=RequestMethod.GET)
	public String getOrders(){
		return "portal/get_orders";
	}
	@RequestMapping(value="/data/depth.html", method=RequestMethod.GET)
	public String getDepth(){
		return "portal/depth";
	}
	@RequestMapping(value="/data/ticker.html", method=RequestMethod.GET)
	public String getTicker(){
		return "portal/ticker";
	}
}
