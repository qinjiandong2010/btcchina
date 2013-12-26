package com.btcoin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.btcoin.common.Resp;
import com.btcoin.service.impl.BtcchinaWeb;

@Controller
@RequestMapping("/rest")
public class BtccoinController {
	
	@RequestMapping(value="/{market}/ticker",method=RequestMethod.GET)
	public @ResponseBody Map<String,String> getTicker(@PathVariable String market)throws IOException{
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", "0");
		map.put("message", "成功!");
		return map;
	}
	
	@RequestMapping(value="/{market}/buyOrder",method=RequestMethod.POST)
	public @ResponseBody Resp buyOrder(@PathVariable String market)throws IOException{
		
		return null;
	}
}
