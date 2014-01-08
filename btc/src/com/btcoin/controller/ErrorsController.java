package com.btcoin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorsController{
	
	@RequestMapping(value="/errors/404.html", method=RequestMethod.GET)
	public String error404(){
		return "portal/errors/404";
	}
	@RequestMapping(value="/errors/500.html", method=RequestMethod.GET)
	public String error505(){
		return "portal/errors/500";
	}
}
