package com.btcoin.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.btcoin.common.ErrorCode;
import com.btcoin.common.Resp;
import com.btcoin.exception.BTCException;
import com.btcoin.factory.BTCWebFactory;
import com.btcoin.service.AbstractBTCWeb;
import com.btcoin.utils.StringUtil;

@Controller
@RequestMapping("/api")
public class BTCApiController {
	
	public static Logger log = Logger.getLogger(BTCApiController.class);
	
	@SuppressWarnings("unchecked")
	private JSONObject getRequestParameters(HttpServletRequest request){
		Enumeration<String> names = request.getParameterNames();
		JSONObject params = new JSONObject();
		while( names.hasMoreElements() ){
			String name = names.nextElement();
			params.put(name, request.getParameter(name));
		}
		return params;
	}
	
	@RequestMapping(value="data/ticker",method=RequestMethod.POST)
	public @ResponseBody Resp getTicker(HttpServletRequest request){
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectOk(params, "market") ){
			return new Resp(ErrorCode.market_require, "交易市场不能为空");
		}
		String market = params.getString("market");
		AbstractBTCWeb btcWeb = BTCWebFactory.getInstance(market);
		String errMsg = "系统发生异常。";
		try {
			return btcWeb.getTicker();
		} catch (BTCException err) {
			errMsg = String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (JedisConnectionException err){
			errMsg = String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (NotImplementedException err) {
			errMsg = String.format("错误：",market,err.getMessage());
			log.error(errMsg,err);
		}catch (Exception err) {
			errMsg = String.format("market[%s]:%s",market,"未知异常");
			log.error(errMsg,err);
		}
		return new Resp(ErrorCode.sys_err, errMsg);
	}
	@RequestMapping(value="trade/buy",method=RequestMethod.POST)
	public @ResponseBody Resp buyOrder(HttpServletRequest request)throws IOException{
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectOk(params, "market") ){
			return new Resp(ErrorCode.market_require, "交易市场不能为空");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "price") ){
			return new Resp(ErrorCode.price_require, "价格不能为空");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "amount") ){
			return new Resp(ErrorCode.amount_require, "购买数额不能为空");
		}
		if( !StringUtil.isJSONObjectOk(params, "username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空");
		}
		if( !StringUtil.isJSONObjectOk(params, "password") ){
			return new Resp(ErrorCode.pwd_require, "密码不能为空");
		}
		double price = params.getDouble("price");
		double amount = params.getDouble("amount");
		String username = params.getString("username");
		String password = params.getString("password");
		String market = params.getString("market");
		String errMsg = "系统发生异常。";
		try {
			AbstractBTCWeb btcWeb = BTCWebFactory.getInstance(market);
			Resp resp = btcWeb.buyOrder(price, amount, params);
			//会话超时，重新登录
			if( resp.getRecode() == ErrorCode.session_timeout ){
				resp = btcWeb.login(username, password);
				//重新登录成功
				if( ErrorCode.SUCCESS == resp.getRecode() ){
					return btcWeb.buyOrder(price, amount, params);
				}
			}
			return resp;
		} catch (BTCException err) {
			errMsg = String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (JedisConnectionException err){
			errMsg = String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (NotImplementedException err) {
			errMsg = String.format("错误",market,err.getMessage());
			log.error(errMsg,err);
		}catch (Exception err) {
			errMsg = String.format("market[%s]:%s",market,"未知异常");
			log.error(errMsg,err);
		}
		return new Resp(ErrorCode.sys_err, errMsg);
	}
	@RequestMapping(value="trade/sell",method=RequestMethod.POST)
	public @ResponseBody Resp sellOrder(HttpServletRequest request)throws IOException{
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectOk(params, "market") ){
			return new Resp(ErrorCode.market_require, "交易市场不能为空");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "price") ){
			return new Resp(ErrorCode.price_require, "价格不能为空或不正确");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "amount") ){
			return new Resp(ErrorCode.amount_require, "购买数额不能为空或不正确");
		}
		if( !StringUtil.isJSONObjectOk(params, "username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空");
		}
		if( !StringUtil.isJSONObjectOk(params, "password") ){
			return new Resp(ErrorCode.pwd_require, "密码不能为空");
		}
		double price = params.getDouble("price");
		double amount = params.getDouble("amount");
		String username = params.getString("username");
		String password = params.getString("password");
		String market = params.getString("market");
		String errMsg = "系统发生异常。";
		try {
			AbstractBTCWeb btcWeb = BTCWebFactory.getInstance(market);
			Resp resp = btcWeb.sellOrder(price, amount, params);
			//会话超时，重新登录
			if( resp.getRecode() == ErrorCode.session_timeout ){
				resp = btcWeb.login(username, password);
				//重新登录成功
				if( ErrorCode.SUCCESS == resp.getRecode() ){
					return btcWeb.sellOrder(price, amount, params);
				}
			}
			return resp; 
		} catch (BTCException err) {
			errMsg = String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (JedisConnectionException err){
			errMsg = String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (NotImplementedException err) {
			errMsg = String.format("错误",market,err.getMessage());
			log.error(errMsg,err);
		}catch (Exception err) {
			errMsg = String.format("market[%s]:%s",market,"未知异常");
			log.error(errMsg,err);
		}
		return new Resp(ErrorCode.sys_err, errMsg);
	}
	@RequestMapping(value="data/depth",method=RequestMethod.POST)
	public @ResponseBody Resp getMarketDepth(HttpServletRequest request)throws IOException{
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectOk(params, "market") ){
			return new Resp(ErrorCode.market_require, "交易市场不能为空");
		}
		String market = params.getString("market");
		String errMsg = "系统发生异常。";
		try {
			AbstractBTCWeb btcWeb = BTCWebFactory.getInstance(market);
			Resp resp = btcWeb.getMarketDepth(params);
			if( ErrorCode.session_timeout == resp.getRecode() ){
				if( !StringUtil.isJSONObjectOk(params, "username") ){
					return new Resp(ErrorCode.un_require, "用户名不能为空");
				}
				if( !StringUtil.isJSONObjectOk(params, "password") ){
					return new Resp(ErrorCode.pwd_require, "密码不能为空");
				}
				String username = params.getString("username");
				String password = params.getString("password");
				resp = btcWeb.login(username, password);
				//重新登录成功
				if( ErrorCode.SUCCESS == resp.getRecode() ){
					return btcWeb.getMarketDepth(params);
				}
			}
			return resp;
		} catch (BTCException err) {
			errMsg = String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (JedisConnectionException err){
			errMsg = String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (NotImplementedException err) {
			errMsg = String.format("错误:"+market,err.getMessage());
			log.error(errMsg,err);
		}catch (Exception err) {
			errMsg = String.format("market[%s]:%s",market,"未知异常");
			log.error(errMsg,err);
		}
		return new Resp(ErrorCode.sys_err, errMsg);
	}
	@RequestMapping(value="trade/cancel/id/{id}",method=RequestMethod.POST)
	public @ResponseBody Resp cancelOrder(@PathVariable long id,HttpServletRequest request)throws IOException{
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectOk(params, "market") ){
			return new Resp(ErrorCode.market_require, "交易市场不能为空");
		}
		if( !StringUtil.isJSONObjectOk(params, "username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空");
		}
		if( !StringUtil.isJSONObjectOk(params, "password") ){
			return new Resp(ErrorCode.pwd_require, "密码不能为空");
		}
		String market = params.getString("market");
		String errMsg = "系统发生异常。";
		try {
			AbstractBTCWeb btcWeb = BTCWebFactory.getInstance(market);
			Resp resp = btcWeb.cancelOrder(id, params);
			if( ErrorCode.session_timeout == resp.getRecode() ){
				String username = params.getString("username");
				String password = params.getString("password");
				resp = btcWeb.login(username, password);
				//重新登录成功
				if( ErrorCode.SUCCESS == resp.getRecode() ){
					return btcWeb.cancelOrder(id, params);
				}
			}
			return resp;
		} catch (BTCException err) {
			errMsg = String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (JedisConnectionException err){
			errMsg = String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (NotImplementedException err) {
			errMsg = String.format("错误:"+market,err.getMessage());
			log.error(errMsg,err);
		}catch (Exception err) {
			errMsg = String.format("market[%s]:%s",market,"未知异常");
			log.error(errMsg,err);
		}
		return new Resp(ErrorCode.sys_err, errMsg);
	}
	@RequestMapping(value="trade/orders",method=RequestMethod.POST)
	public @ResponseBody Resp getOrders(HttpServletRequest request)throws IOException{
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectOk(params, "market") ){
			return new Resp(ErrorCode.market_require, "交易市场不能为空");
		}
		if( !StringUtil.isJSONObjectOk(params, "username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空");
		}
		if( !StringUtil.isJSONObjectOk(params, "password") ){
			return new Resp(ErrorCode.pwd_require, "密码不能为空");
		}
		String market = params.getString("market");
		String errMsg = "系统发生异常。";
		try {
			AbstractBTCWeb btcWeb = BTCWebFactory.getInstance(market);
			Resp resp = btcWeb.getOrders(false,params);
			if( ErrorCode.session_timeout == resp.getRecode() ){
				String username = params.getString("username");
				String password = params.getString("password");
				resp = btcWeb.login(username, password);
				//重新登录成功
				if( ErrorCode.SUCCESS == resp.getRecode() ){
					return btcWeb.getOrders(false, params);
				}
			}
			return resp;
		} catch (BTCException err) {
			errMsg = String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (JedisConnectionException err){
			errMsg = String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage());
			log.error(errMsg,err);
		} catch (NotImplementedException err) {
			errMsg = String.format(err.getMessage());
			log.error(errMsg,err);
		}catch (Exception err) {
			errMsg = String.format("market[%s]:%s",market,"未知异常");
			log.error(errMsg,err);
		}
		return new Resp(ErrorCode.sys_err, errMsg);
	}
}
