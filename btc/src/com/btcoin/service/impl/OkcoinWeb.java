package com.btcoin.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.btcoin.common.ErrorCode;
import com.btcoin.common.JredisManager;
import com.btcoin.common.Resp;
import com.btcoin.service.AbstractBtcWeb;
import com.btcoin.utils.StringUtil;

public class OkcoinWeb extends AbstractBtcWeb{
	
	private static final Logger log = Logger.getLogger(OkcoinWeb.class);
	private static final String login_url = "https://www.okcoin.com/login/index.do?random=13";
	private static final String buyOrder_url = "https://www.okcoin.com/trade/buyBtcSubmit.do";
	private static final String sellOrder_url = "https://www.okcoin.com/trade/sellBtcSubmit.do";
	private static final String cancelOrder_url = "";
	private static final String getMarketDepth_url = "";
	private static final String getOrders_url = "";
	private static final String getTicker_url = "https://www.okcoin.com/ticker.do";
	public static final String WEB_SERVICE_NAME = "okcoin";

	@Override
	public Resp login(final String username, String password) throws IOException {
		final BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient= this.getHttpClient(cookieStore);
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("loginName", username));  
			nvps.add(new BasicNameValuePair("password", password));  
			
			HttpPost httpPost = new HttpPost( login_url );
			httpPost.setEntity(new UrlEncodedFormEntity(nvps)); 
			httpPost.setHeader("Accept","*/*"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("Cookie","coin_session_id_o=5e9255ce-0830-4f73-8eab-18054b790901okcoin; Hm_lvt_45e8f68df9bb8a9fc29ce78c80080330=1387155926,1388116089; JSESSIONID=3C9DD6C653B4C6C31B95A348E9650CB5; Hm_lpvt_45e8f68df9bb8a9fc29ce78c80080330=1388116147; coin_session_nikename=qinjiandong2010; coin_session_logininfo="); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0 ");  
			httpPost.setHeader("Host","www.okcoin.com");  
			httpPost.setHeader("Referer","https://www.okcoin.com/");  
			
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {  
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }else{
							log.info("Login form get: " + response.getStatusLine());
							Scanner scan = new Scanner(response.getEntity().getContent());
							String result = "";
							try{ result = scan.nextLine(); }finally{scan.close(); }
							
							if( !StringUtil.isNullOrEmpty(result)){
								JSONObject jsonObject = JSONObject.fromObject(result);
								int resultCode =  StringUtil.isJSONObjectIsInt(jsonObject, "resultCode") ? jsonObject.getInt("resultCode") : -100;
								int errorNum =  StringUtil.isJSONObjectIsInt(jsonObject, "errorNum") ? jsonObject.getInt("errorNum") : -1;
								//登录成功
								if( 0 == jsonObject.getInt("resultCode")){
									log.info("Post logon cookies:");
					                List<Cookie> cookies = cookieStore.getCookies();
					                if (cookies.isEmpty()) {
					                	log.info("None");
					                } else {
						                //保存用户cookies 到redis
						                JredisManager redisManager = JredisManager.getInstance();
						                redisManager.getJedis().set(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username),JSONArray.fromObject(cookies).toString());
						                log.info("save cookies to redis successful.");
					                	for (int i = 0; i < cookies.size(); i++) {
					                    	log.info("- " + cookies.get(i).toString());
					                    }
					                }
					                return new Resp(ErrorCode.SUCCESS,"登录成功.");
								}else if( -1 == resultCode){
									log.info("用户名或密码错误");
									return new Resp(ErrorCode.FAILURE,"用户名或密码错误");
								}else if( -2 == resultCode){
									log.info("此ip登录频繁，请2小时后再试");
									return new Resp(ErrorCode.FAILURE,"此ip登录频繁，请2小时后再试");
								}else if( -3 == resultCode){
									if(errorNum == 0){
										log.info("此ip登录频繁，请2小时后再试");
										return new Resp(ErrorCode.FAILURE,"此ip登录频繁，请2小时后再试");
									}else if(errorNum == -1){
										log.info("外部系统返回结果异常");
										return new Resp(ErrorCode.FAILURE,"外部系统返回结果异常");
									}else{
										log.info("用户名或密码错误，您还有"+errorNum+"次机会");
										return new Resp(ErrorCode.FAILURE,"用户名或密码错误，您还有"+errorNum+"次机会");
									}
								}else if( -4 == resultCode){
									log.info("请设置启用COOKIE功能");
									return new Resp(ErrorCode.FAILURE,"请设置启用COOKIE功能");
								}else if( 2 == resultCode){
									log.info("账户出现安全隐患被冻结，请尽快联系客服。");
									return new Resp(ErrorCode.FAILURE,"账户出现安全隐患被冻结，请尽快联系客服。");
								}else if( -100 == resultCode ){
									log.info("外部系统返回结果异常");
									return new Resp(ErrorCode.FAILURE,"外部系统返回结果异常");
								}
							}
							log.info("外部系统返回结果异常");
							return new Resp(ErrorCode.FAILURE,"外部系统返回结果异常");
				        }
					}finally{
						response.close();
					}
				}
			});
		}finally{
			httpclient.close();
		}
	}

	@Override
	public Resp buyOrder(double price, double amount, JSONObject params)
			throws IOException {
		if(!StringUtil.isJSONObjectOk(params, "username")){
			log.info("用户名不能为空。");
			return new Resp(ErrorCode.FAILURE,"用户名不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "password")){
			log.info("用户密码不能为空。");
			return new Resp(ErrorCode.FAILURE,"用户密码不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "tradePwd")){
			log.info("交易密码不能为空。");
			return new Resp(ErrorCode.FAILURE,"交易密码不能为空。");
		}
		
		CloseableHttpClient httpclient = this.getHttpClient();
		HttpPost httpPost = new HttpPost(String.format("%s?random=%s", buyOrder_url,Math.round(Math.random()*100)));
		try{
			//{tradeAmount:tradeAmount,tradeCnyPrice:tradeCnyPrice,tradePwd:tradePwd,symbol:symbol};
			int symbol = 0;
			String tradePwd = params.getString("tradePwd");
			final String username = params.getString("username");
			final String password = params.getString("password");
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("tradeAmount", amount+""));  
			nvps.add(new BasicNameValuePair("symbol", symbol+""));  
			nvps.add(new BasicNameValuePair("tradeCnyPrice", price+"")); 
			nvps.add(new BasicNameValuePair("tradePwd", tradePwd)); 
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","www.okcoin.com");  
			httpPost.setHeader("Referer","https://www.okcoin.com/");
			
			//读取redis用户Cookie
			JredisManager redisManager = JredisManager.getInstance();
			String data = redisManager.getJedis().get(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( data != null ){
		        JSONArray cookies = JSONArray.fromObject(data);
		        String cookieHeader = "";
		        for (Object obj : cookies) {
					JSONObject cookie = (JSONObject) obj;
					cookieHeader += (cookie.getString("name")+"="+cookie.getString("value"))+";";
				}
		        httpPost.setHeader("Cookie",cookieHeader);
			}
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						log.info("Login form get: " + response.getStatusLine());
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {  
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }
						Scanner scan = new Scanner(response.getEntity().getContent());
						String result = "";
						try{ result = scan.nextLine(); }finally{scan.close(); }
						
						if( !StringUtil.isNullOrEmpty(result)){
							JSONObject jsonObject = JSONObject.fromObject(result);
							int resultCode =  StringUtil.isJSONObjectIsInt(jsonObject, "resultCode") ? jsonObject.getInt("resultCode") : -100;
							int errorNum =  StringUtil.isJSONObjectIsInt(jsonObject, "errorNum") ? jsonObject.getInt("errorNum") : -1;
							if(resultCode == 1){
								log.info("用户会话已过期,请重新登录。");
								return new Resp(ErrorCode.FAILURE,"用户会话已过期,请重新登录。");
							}else if (resultCode == -1) {
								log.info("最小购买数量为：0.01BTC！");
								return new Resp(ErrorCode.FAILURE,"最小购买数量为：0.01BTC！");
							} else if (resultCode == -2) {
								if (errorNum == 0) {
									log.info("交易密码错误五次，请2小时后再试！");
									return new Resp(ErrorCode.FAILURE,"交易密码错误五次，请2小时后再试！");
								} else {
									log.info("交易密码不正确！您还有" + errorNum + "次机会");
									return new Resp(ErrorCode.FAILURE,"交易密码不正确！您还有" + errorNum + "次机会");
								}
							} else if (resultCode == -3) {
								log.info("出价不能为0！");
								return new Resp(ErrorCode.FAILURE,"出价不能为0！");
							} else if (resultCode == -4) {
								log.info("余额不足！");
								return new Resp(ErrorCode.FAILURE,"余额不足！");
							} else if (resultCode == -5) {
								log.info("您未设置交易密码，请设置交易密码。");
								return new Resp(ErrorCode.FAILURE,"您未设置交易密码，请设置交易密码。");
							} else if (resultCode == -6) {
								log.info("您输入的价格与最新成交价相差太大，请检查是否输错");
								return new Resp(ErrorCode.FAILURE,"您输入的价格与最新成交价相差太大，请检查是否输错");
							} else if (resultCode == -7) {
								log.info("交易密码免输超时，请刷新页面输入交易密码后重新激活。");
								return new Resp(ErrorCode.FAILURE,"交易密码免输超时，请刷新页面输入交易密码后重新激活。");
							} else if (resultCode == -8) {
								log.info("请输入交易密码");
								return new Resp(ErrorCode.FAILURE,"请输入交易密码");
							} else if (resultCode == 0) {
								log.info("购买成功");
								return new Resp(ErrorCode.SUCCESS,"购买成功");
							} else if (resultCode == 2) {
								log.info("账户出现安全隐患已被冻结，请尽快联系客服。");
								return new Resp(ErrorCode.FAILURE,"账户出现安全隐患已被冻结，请尽快联系客服。");
							}
						}
						log.info("外部系统返回结果异常");
						return new Resp(ErrorCode.FAILURE,"外部系统返回结果异常");
					}finally{
						response.close();
					}
				}
			});
		}finally{
			httpclient.close();
		}
	}

	@Override
	public Resp sellOrder(double price, double amount, JSONObject params)
			throws IOException {
		if(!StringUtil.isJSONObjectOk(params, "username")){
			log.info("用户名不能为空。");
			return new Resp(ErrorCode.FAILURE,"用户名不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "password")){
			log.info("用户密码不能为空。");
			return new Resp(ErrorCode.FAILURE,"用户密码不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "tradePwd")){
			log.info("交易密码不能为空。");
			return new Resp(ErrorCode.FAILURE,"交易密码不能为空。");
		}
		CloseableHttpClient httpclient = this.getHttpClient();
		HttpPost httpPost = new HttpPost(String.format("%s?random=%s", sellOrder_url,Math.round(Math.random()*100)));
		try{
			//{tradeAmount:tradeAmount,tradeCnyPrice:tradeCnyPrice,tradePwd:tradePwd,symbol:symbol};
			int symbol = 0;
			String tradePwd = params.getString("tradePwd");
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("tradeAmount", amount+""));  
			nvps.add(new BasicNameValuePair("symbol", symbol+""));  
			nvps.add(new BasicNameValuePair("tradeCnyPrice", price+"")); 
			nvps.add(new BasicNameValuePair("tradePwd", tradePwd)); 
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","www.okcoin.com");  
			httpPost.setHeader("Referer","https://www.okcoin.com/");
			
			final String username = params.getString("username");
			final String password = params.getString("password");
			
			//读取redis用户Cookie
			JredisManager redisManager = JredisManager.getInstance();
			String data = redisManager.getJedis().get(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( data != null ){
		        JSONArray cookies = JSONArray.fromObject(data);
		        String cookieHeader = "";
		        for (Object obj : cookies) {
					JSONObject cookie = (JSONObject) obj;
					cookieHeader += (cookie.getString("name")+"="+cookie.getString("value"))+";";
				}
		        httpPost.setHeader("Cookie",cookieHeader);
			}
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						log.info("Login form get: " + response.getStatusLine());
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {  
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }
						Scanner scan = new Scanner(response.getEntity().getContent());
						String result = "";
						try{ result = scan.nextLine(); }finally{scan.close(); }
						
						if( !StringUtil.isNullOrEmpty(result)){
							JSONObject jsonObject = JSONObject.fromObject(result);
							int resultCode =  StringUtil.isJSONObjectIsInt(jsonObject, "resultCode") ? jsonObject.getInt("resultCode") : -100;
							int errorNum =  StringUtil.isJSONObjectIsInt(jsonObject, "errorNum") ? jsonObject.getInt("errorNum") : -1;
							if(resultCode == 1){
								log.info("用户会话已过期,请重新登录。");
								return new Resp(ErrorCode.FAILURE,"用户会话已过期,请重新登录。");
							}else if (resultCode == -1) {
								log.info("最小购买数量为：0.01BTC！");
								return new Resp(ErrorCode.FAILURE,"最小卖出数量为：0.01BTC！");
							} else if (resultCode == -2) {
								if (errorNum == 0) {
									log.info("交易密码错误五次，请2小时后再试！");
									return new Resp(ErrorCode.FAILURE,"交易密码错误五次，请2小时后再试！");
								} else {
									log.info("交易密码不正确！您还有" + errorNum + "次机会");
									return new Resp(ErrorCode.FAILURE,"交易密码不正确！您还有" + errorNum + "次机会");
								}
							} else if (resultCode == -3) {
								log.info("出价不能为0！");
								return new Resp(ErrorCode.FAILURE,"出价不能为0！");
							} else if (resultCode == -4) {
								log.info("余额不足！");
								return new Resp(ErrorCode.FAILURE,"余额不足！");
							} else if (resultCode == -5) {
								log.info("您未设置交易密码，请设置交易密码。");
								return new Resp(ErrorCode.FAILURE,"您未设置交易密码，请设置交易密码。");
							} else if (resultCode == -6) {
								log.info("您输入的价格与最新成交价相差太大，请检查是否输错");
								return new Resp(ErrorCode.FAILURE,"您输入的价格与最新成交价相差太大，请检查是否输错");
							} else if (resultCode == -7) {
								log.info("交易密码免输超时，请刷新页面输入交易密码后重新激活。");
								return new Resp(ErrorCode.FAILURE,"交易密码免输超时，请刷新页面输入交易密码后重新激活。");
							} else if (resultCode == -8) {
								log.info("请输入交易密码");
								return new Resp(ErrorCode.FAILURE,"请输入交易密码");
							} else if (resultCode == 0) {
								log.info("卖出成功");
								return new Resp(ErrorCode.SUCCESS,"卖出成功");
							} else if (resultCode == 2) {
								log.info("账户出现安全隐患已被冻结，请尽快联系客服。");
								return new Resp(ErrorCode.FAILURE,"账户出现安全隐患已被冻结，请尽快联系客服。");
							}
						}
						log.info("外部系统返回结果异常");
						return new Resp(ErrorCode.FAILURE,"外部系统返回结果异常");
					}finally{
						response.close();
					}
				}
			});
		}finally{
			httpclient.close();
		}
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
	public Resp getTicker() throws IOException {
		CloseableHttpClient httpclient = this.getHttpClient();
		HttpGet httpGet = new HttpGet(String.format("%s?random=%s", getTicker_url,Math.round(Math.random()*100)));
		try{
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","www.okcoin.com");  
			httpGet.setHeader("Referer","https://www.okcoin.com");
			
			return httpclient.execute(httpGet, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {  
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }
						Scanner scan = new Scanner(response.getEntity().getContent());
						String result = "";
						try{ result = scan.nextLine(); }finally{scan.close(); }
						if(StringUtil.isNullOrEmpty(result)){
							return new Resp(ErrorCode.FAILURE,"无返回结果");
						}
						JSONObject dataJson = JSONObject.fromObject(result);
						Map<String,Object> ticker = new HashMap<String, Object>();
						ticker.put("vol", dataJson.get("vol"));
						ticker.put("last", dataJson.get("last"));
						ticker.put("buy", dataJson.get("buy"));
						ticker.put("sell", dataJson.get("sell"));
						ticker.put("high", dataJson.get("high"));
						ticker.put("low", dataJson.get("low"));
						
						return new Resp(ErrorCode.SUCCESS,"查询成功",ticker);
					}finally{
						response.close();
					}
				}
			});
		}finally{
			httpclient.close();
		}
	}

}
