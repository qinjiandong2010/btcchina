package com.btcoin.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.btcoin.common.ErrorCode;
import com.btcoin.common.Resp;
import com.btcoin.exception.BtcoinException;
import com.btcoin.factory.BtcWebFactory;
import com.btcoin.service.AbstractBtcWeb;
import com.btcoin.utils.StringUtil;

@Controller
@RequestMapping("/api")
public class BtcController {
	
	public static Logger log = Logger.getLogger(BtcController.class);
	
	private JSONObject getRequestParameters(HttpServletRequest request){
		Enumeration<String> names =request.getParameterNames();
		JSONObject params = new JSONObject();
		while( names.hasMoreElements() ){
			String name = names.nextElement();
			params.put(name, request.getParameter(name));
		}
		return params;
	}
	
	@RequestMapping(value="/{market}/ticker",method=RequestMethod.GET)
	public @ResponseBody Resp getTicker(@PathVariable String market){
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		try {
			return btcWeb.getTicker();
		} catch (BtcoinException err) {
			log.error(String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage()));
		} catch (JedisConnectionException err){
			log.error(String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage()));
		}catch (Exception err) {
			log.error(String.format("market[%s]:%s",market,"未知异常",err));
		}
		return new Resp(ErrorCode.sys_err, "系统发生异常。");
	}
	@RequestMapping(value="{market}/buy",method=RequestMethod.POST)
	public @ResponseBody Resp buyOrder(@PathVariable String market,HttpServletRequest request)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectIsDouble(params, "price") ){
			return new Resp(ErrorCode.price_require, "价格是必须项");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "amount") ){
			return new Resp(ErrorCode.amount_require, "数量是必须项");
		}
		if( !StringUtil.isJSONObjectOk(params, "username") ){
			return new Resp(ErrorCode.un_require, "用户名是必须项");
		}
		if( !StringUtil.isJSONObjectOk(params, "password") ){
			return new Resp(ErrorCode.pwd_require, "密码是必须项");
		}
		double price = (Double)params.remove("price");
		double amount = (Double)params.remove("amount");
		String username = params.getString("username");
		String password = params.getString("password");
		try {
			Resp resp = btcWeb.buyOrder(price, amount, params);
			//会话超时，重新登录
			if( resp.getRecode() == ErrorCode.session_timeout ){
				//重新登录成功
				if( ErrorCode.SUCCESS == btcWeb.login(username, password).getRecode() ){
					return btcWeb.buyOrder(price, amount, params);
				}
			}
			return resp;
		} catch (BtcoinException err) {
			log.error(String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage()));
		} catch (JedisConnectionException err){
			log.error(String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage()));
		}catch (Exception err) {
			log.error(String.format("market[%s]:%s",market,"未知异常",err));
		}
		return new Resp(ErrorCode.sys_err, "系统发生异常。");
	}
	@RequestMapping(value="{market}/sell",method=RequestMethod.POST)
	public @ResponseBody Resp sellOrder(@PathVariable String market,HttpServletRequest request)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		JSONObject params = getRequestParameters(request);
		if( !StringUtil.isJSONObjectIsDouble(params, "price") ){
			return new Resp(ErrorCode.price_require, "价格是必须项");
		}
		if( !StringUtil.isJSONObjectIsDouble(params, "amount") ){
			return new Resp(ErrorCode.amount_require, "数量是必须项");
		}
		if( !StringUtil.isJSONObjectOk(params, "username") ){
			return new Resp(ErrorCode.un_require, "用户名是必须项");
		}
		if( !StringUtil.isJSONObjectOk(params, "password") ){
			return new Resp(ErrorCode.pwd_require, "密码是必须项");
		}
		double price = (Double)params.remove("price");
		double amount = (Double)params.remove("amount");
		String username = params.getString("username");
		String password = params.getString("password");
		try {
			Resp resp = btcWeb.sellOrder(price, amount, params);
			//会话超时，重新登录
			if( resp.getRecode() == ErrorCode.session_timeout ){
				//重新登录成功
				if( ErrorCode.SUCCESS == btcWeb.login(username, password).getRecode() ){
					return btcWeb.sellOrder(price, amount, params);
				}
			}
			return resp; 
		} catch (BtcoinException err) {
			log.error(String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage()));
		} catch (JedisConnectionException err){
			log.error(String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage()));
		}catch (Exception err) {
			log.error(String.format("market[%s]:%s",market,"未知异常",err));
		}
		return new Resp(ErrorCode.sys_err, "系统发生异常。");
	}
	@RequestMapping(value="{market}/depth",method=RequestMethod.GET)
	public @ResponseBody Resp getMarketDepth(@PathVariable String market,HttpServletRequest request)throws IOException{
		AbstractBtcWeb btcWeb = BtcWebFactory.getInstance(market);
		JSONObject params = getRequestParameters(request);
		try {
			Resp resp = btcWeb.getMarketDepth(params);
			if( ErrorCode.session_timeout == resp.getRecode() ){
				//如果是btcchina验证参数
				if( !StringUtil.isJSONObjectOk(params, "username") ){
					return new Resp(ErrorCode.un_require, "用户名是必须项");
				}
				if( !StringUtil.isJSONObjectOk(params, "password") ){
					return new Resp(ErrorCode.pwd_require, "密码是必须项");
				}
				String username = params.getString("username");
				String password = params.getString("password");
				//重新登录成功
				if( ErrorCode.SUCCESS == btcWeb.login(username, password).getRecode() ){
					return btcWeb.getMarketDepth(params);
				}
			}
			return resp;
		} catch (BtcoinException err) {
			log.error(String.format("market[%s]:%s",market,"BTC API内部异常:"+err.getMessage()));
		} catch (JedisConnectionException err){
			log.error(String.format("market[%s]:%s",market,"Redis 连接异常:"+err.getMessage()));
		}catch (Exception err) {
			log.error(String.format("market[%s]:%s",market,"未知异常",err));
		}
		return new Resp(ErrorCode.sys_err, "系统发生异常。");
	}
	@RequestMapping(value="{market}/cancelorder",method=RequestMethod.POST)
	public @ResponseBody Resp cancelOrder(@PathVariable String market)throws IOException{
		return null;
	}
	@RequestMapping(value="{market}/orders",method=RequestMethod.GET)
	public @ResponseBody Resp getOrders(@PathVariable String market)throws IOException{
		return null;
	}
}
