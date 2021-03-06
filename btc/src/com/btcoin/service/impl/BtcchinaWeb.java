package com.btcoin.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.btcoin.common.ErrorCode;
import com.btcoin.common.JredisManager;
import com.btcoin.common.Resp;
import com.btcoin.exception.BTCException;
import com.btcoin.service.AbstractBTCWeb;
import com.btcoin.utils.StringUtil;

/**
 * btcchina
 * @author Administrator
 *
 */
public class BTCChinaWeb extends AbstractBTCWeb{

	private static final Logger log = Logger.getLogger(BTCChinaWeb.class);
	private static final String login_url = "https://vip.btcchina.com/bbs/ucp.php?mode=login&change_lang=zh_cmn_hans";
	private static final String buyOrder_url = "https://vip.btcchina.com/trade/buy";
	private static final String sellOrder_url = "https://vip.btcchina.com/trade/sell";
	private static final String cancelOrder_url = "https://vip.btcchina.com/trade/cancel/id/%s";
	private static final String getMarketDepth_url = "https://vip.btcchina.com/trade/depth";
	private static final String getTicker_url = "https://data.btcchina.com/data/ticker";
	private static final String getOrders_url = "https://vip.btcchina.com/trade/order";
	public static final String WEB_SERVICE_NAME = "btcchina";
	
	public JredisManager redisManager = JredisManager.getInstance();
	
	@Override
	public Resp login(final String username, String password) throws IOException,BTCException{
		
		if( StringUtil.isEmpty(username) ){
			return new Resp(ErrorCode.un_require,"username is require.");
		}
		if( StringUtil.isEmpty(password) ){
			return new Resp(ErrorCode.pwd_require,"password is require.");
		}
		
		final BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient= super.getHttpClient(cookieStore);
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("username", username));  
			nvps.add(new BasicNameValuePair("password", password));  
			nvps.add(new BasicNameValuePair("login", ""));
			nvps.add(new BasicNameValuePair("redirect", "/")); 
			
			HttpPost httpPost = (HttpPost)super.createHttpRequest(HttpMethod.POST, login_url, true );
			httpPost.setEntity(new UrlEncodedFormEntity(nvps)); 
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("Cookie","Hm_lvt_4b4a9e41d8a28c344a964dfa3baf4f6d=1386908362,1387156187,1387246322,1387345399; _ga=GA1.2.1334210409.1386908364; style_cookie=null; visid_incap_88065=3EulVzKxSfiytppzmpoDkuiyr1IAAAAAQUIPAAAAAACY0AtAv9QbHq3EPSLQ4uIZ; PHPSESSID=ohs8gk2n375igkhe6trb2115o0; Hm_lpvt_4b4a9e41d8a28c344a964dfa3baf4f6d=1387345399"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0 ");  
			httpPost.setHeader("Host","vip.btcchina.com");  
			httpPost.setHeader("Referer","https://vip.btcchina.com/");  
			
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() > 302) {  
				        	log.error(String.format("HTTP response:status code=%s, status message=[%s],用户[%s]登录失败。", 
        							status.getStatusCode(),
        							status.getReasonPhrase(),
        							username));
				        	return new Resp(ErrorCode.login_error,"Did not receive successful HTTP response: status code = "  
		                            + status.getStatusCode() + ", status message = ["  
		                            + status.getReasonPhrase() + "]");
				        }else{
							Header[] locations = response.getHeaders("Location");
							if( locations.length == 0 ){
					        	log.error(String.format("HTTP response:status code=%s, status message=[%s],用户[%s]登录失败。", 
	        							status.getStatusCode(),
	        							status.getReasonPhrase(),
	        							username));
								return new Resp(ErrorCode.login_error,String.format("用户[%s]登录失败。", username));
							}
							log.info("Login form get: " + response.getStatusLine());

							log.info("Post logon cookies:");
			                List<Cookie> cookies = cookieStore.getCookies();
			                if (cookies.isEmpty()) {
			                	log.error(String.format("HTTP response:status code=%s, status message=[%s],用户[%s]登录失败。", 
	        							status.getStatusCode(),
	        							status.getReasonPhrase(),
	        							username));
			                	return new Resp(ErrorCode.login_error,"login failed.");
			                } else {
				                //保存用户cookies 到redis
				                redisManager.getJedis().set(WEB_SERVICE_NAME+String.format(":u:%s:cookies", username),JSONArray.fromObject(cookies).toString());
				                log.info("save cookies to redis successful.");
			                	for (int i = 0; i < cookies.size(); i++) {
			                    	log.info("- " + cookies.get(i).toString());
			                    }
			                }
				        }
				        return new Resp(ErrorCode.SUCCESS,"Login success.");
					}finally{
						//response.close();
					}
				}
			});
		}finally{
			httpclient.close();
		}
	}

	@Override
	public Resp buyOrder(double price, double amount,JSONObject params) throws IOException {
		
		if( !StringUtil.isJSONObjectOk(params,"username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空.");
		}
		if( !StringUtil.isJSONObjectOk(params,"password") ){
			return new Resp(ErrorCode.pwd_require, "password is require.");
		}
		if( !StringUtil.isJSONObjectOk(params,"tradepwd") ){
			return new Resp(ErrorCode.tradepwd_require, "交易密码不能为空.");
		}
		if( !StringUtil.isJSONObjectOk(params,"ordertype") ){
			params.put("ordertype", "limit");
			//return new Resp(ErrorCode.ordertype_require, "ordertype is require.");
		}
		CloseableHttpClient httpclient = this.getHttpClient();
		try{
			final String username = params.getString("username");
			final String tradepwd = params.getString("tradepwd");
			final String ordertype = params.getString("ordertype");
			
			HttpGet httpGet = (HttpGet)createHttpRequest(HttpMethod.GET, buyOrder_url, true);
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","vip.btcchina.com");  
			httpGet.setHeader("Referer",buyOrder_url);
			//读取redis用户Cookie
			String cookie = super.getCookie(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( cookie != null ){
				httpGet.setHeader("Cookie",cookie);
			}else{
				 return new Resp(ErrorCode.session_timeout,"会话已过期.");
			}
			
			Resp csrf = httpclient.execute(httpGet, new ResponseHandler<Resp>(){

				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户cookie已过期请重新登录!");
				        	return new Resp(ErrorCode.session_timeout,"The user cookie has expired, please login again.");
				        }
				        HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element usernameElement = doc.getElementById("username");
						Element passwordElement = doc.getElementById("password");
						//登录页面，cookie已经过期重新登录
						if( usernameElement != null && passwordElement != null){
							log.info("用户cookie已过期请重新登录!");
							return new Resp(ErrorCode.session_timeout,"用户cookie已过期请重新登录!");
						}
						return new Resp(ErrorCode.SUCCESS,"Success.",doc.getElementById("no_csrf_buybtc").val());
					}finally{
						response.close();
					}
				}
			});
			if(ErrorCode.SUCCESS != csrf.getRecode()){
				return csrf;
			}
			
			HttpPost httpPost = (HttpPost)super.createHttpRequest(HttpMethod.POST, buyOrder_url, true );
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","vip.btcchina.com");  
			httpPost.setHeader("Referer",buyOrder_url);
			httpPost.setHeader("Cookie",cookie);
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("amount", amount+""));  
			nvps.add(new BasicNameValuePair("no_csrf_buybtc", csrf.getResult()+""));  
			nvps.add(new BasicNameValuePair("ordertype", ordertype));  
			nvps.add(new BasicNameValuePair("price", price+"")); 
			nvps.add(new BasicNameValuePair("tradepwd", tradepwd)); 
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户cookie已过期请重新登录!");
				        	return new Resp(ErrorCode.session_timeout,"The user cookie has expired, please login again.");
				        }
				        HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element element = doc.select("div[class=alert alert-success]").first();
						if( null != element){
							return new Resp(ErrorCode.SUCCESS,element.text());
						}else{
							element = doc.select("div[class=alert alert-danger]").first();
							return new Resp(ErrorCode.buy_error,element.text());
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
	public Resp sellOrder(double price, double amount,JSONObject params) throws IOException,BTCException{
		if( !StringUtil.isJSONObjectOk(params,"username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空.");
		}
		if( !StringUtil.isJSONObjectOk(params,"password") ){
			return new Resp(ErrorCode.pwd_require, "密码不能为空.");
		}
		if( !StringUtil.isJSONObjectOk(params,"tradepwd") ){
			return new Resp(ErrorCode.tradepwd_require, "交易密码不能为空.");
		}
		if( !StringUtil.isJSONObjectOk(params,"ordertype") ){
			params.put("ordertype", "limit");
			//return new Resp(ErrorCode.ordertype_require, "ordertype is require.");
		}
		CloseableHttpClient httpclient = this.getHttpClient();
		try{
			final String username = params.getString("username");
			final String tradepwd = params.getString("tradepwd");
			final String ordertype = params.getString("ordertype");
			
			HttpGet httpGet = (HttpGet)createHttpRequest(HttpMethod.GET, sellOrder_url, true);
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","vip.btcchina.com");  
			httpGet.setHeader("Referer",sellOrder_url);
			//读取redis用户Cookie
			String cookie = super.getCookie(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( cookie != null ){
				httpGet.setHeader("Cookie",cookie);
			}else{
				 return new Resp(ErrorCode.session_timeout,"会话已过期.");
			}
			
			Resp csrf = httpclient.execute(httpGet, new ResponseHandler<Resp>(){

				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户cookie已过期请重新登录!");
				        	return new Resp(ErrorCode.session_timeout,"用户cookie已过期请重新登录!");
				        }
				        HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						Document doc = Jsoup.parse(htmlDocument.toString());
						try{
							Element usernameElement = doc.getElementById("username");
							Element passwordElement = doc.getElementById("password");
							//登录页面，cookie已经过期重新登录
							if( usernameElement != null && passwordElement != null){
								log.info("用户cookie已过期请重新登录!");
								return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
							}
							return new Resp(ErrorCode.SUCCESS,"Success.",doc.getElementById("no_csrf_sellbtc").val());
						}catch (Exception e) {
							log.error("get no_csrf_sellbtc error:"+e.getMessage());
							return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
						}
					}finally{
						response.close();
					}
				}
			});
			if(ErrorCode.SUCCESS != csrf.getRecode()){
				return csrf;
			}
			
			HttpPost httpPost = (HttpPost)super.createHttpRequest(HttpMethod.POST, sellOrder_url, true );
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","vip.btcchina.com");  
			httpPost.setHeader("Referer",sellOrder_url);
			httpPost.setHeader("Cookie",cookie);
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("amount", amount+""));  
			nvps.add(new BasicNameValuePair("no_csrf_sellbtc",csrf.getResult()+""));  
			nvps.add(new BasicNameValuePair("ordertype", ordertype));  
			nvps.add(new BasicNameValuePair("price", price+"")); 
			nvps.add(new BasicNameValuePair("tradepwd", tradepwd)); 
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户会话已过期,请重新登录!");
				        	return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
				        }
				        HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element element = doc.select("div[class=alert alert-success]").first();
						if( null != element){
							return new Resp(ErrorCode.SUCCESS,element.text());
						}else{
							element = doc.select("span[class=help-inline]").first();
							return new Resp(ErrorCode.sell_error,element.text());
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
	public Resp getMarketDepth(JSONObject params) throws IOException,BTCException{
		
		if( !StringUtil.isJSONObjectOk(params,"username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空.");
		}
		final String storeKey = WEB_SERVICE_NAME+":getMarketDepth";
		if( redisManager.getJedis().exists(storeKey) ){
			String depthData = redisManager.getJedis().get(storeKey);
			JSONObject dataJson = JSONObject.fromObject(depthData);
			return new Resp(ErrorCode.SUCCESS, "查询成功.",dataJson);
		}
		CloseableHttpClient httpclient = this.getHttpClient();
		try{
			final String username = params.getString("username");

			HttpGet httpGet = (HttpGet)createHttpRequest(HttpMethod.GET, getMarketDepth_url, true);
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","vip.btcchina.com");  
			httpGet.setHeader("Referer","https://vip.btcchina.com");
			//读取redis用户Cookie
			String cookie = super.getCookie(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( cookie != null ){
				httpGet.setHeader("Cookie",cookie);
			}else{
				 return new Resp(ErrorCode.session_timeout,"会话已过期.");
			}
			return httpclient.execute(httpGet, new ResponseHandler<Resp>(){
				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户会话已过期,请重新登录!");
				        	return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
				        }
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element usernameElement = doc.getElementById("username");
						Element passwordElement = doc.getElementById("password");
						//登录页面，cookie已经过期重新登录
						if( usernameElement != null && passwordElement != null){
							log.info("用户会话已过期,请重新登录!");
							return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
						}
						Elements tables = doc.select("div table[class=table table-striped table-hover]");
						
						JSONObject dataJson = new JSONObject();
						for (int i = 0,j = tables.size(); i < j; i ++) {
							Elements trs = tables.get(i).select("tbody tr");
							JSONArray rowsJson = new JSONArray();
							for (Element tr : trs) {
								JSONArray rowJson = new JSONArray();
								Elements tds = tr.select("td");
								for (Element td : tds) {
									rowJson.add(Double.parseDouble(td.text().replaceAll(",|¥|฿", "")));
								}
								rowsJson.add(rowJson);
							}
							String tradeType = (i == 0 ? "bids":"asks");
							dataJson.put(tradeType, rowsJson.toString());
						}
						redisManager.getJedis().set(storeKey, dataJson.toString());
						redisManager.getJedis().expire(storeKey, 60);
						
						return new Resp(ErrorCode.SUCCESS,"success",dataJson);
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
	public Resp getTicker() throws IOException, BTCException {
		CloseableHttpClient httpclient = this.getHttpClient();
		try{
			HttpGet httpGet = (HttpGet)createHttpRequest(HttpMethod.GET, getTicker_url, true);
			httpGet.setHeader("Accept","application/json, text/javascript, */*; q=0.01"); 
			httpGet.setHeader("Accept-Encoding","gzip,deflate,sdch"); 
			httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");  
			httpGet.setHeader("Host","vip.btcchina.com");  
			httpGet.setHeader("Referer","https://vip.btcchina.com/");
			
			return httpclient.execute(httpGet, new ResponseHandler<Resp>(){

				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent()));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						JSONObject resultJson = JSONObject.fromObject(htmlDocument.toString());
						JSONObject ticker = resultJson.getJSONObject("ticker");
		                return new Resp(ErrorCode.SUCCESS,"success",ticker);
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
	public Resp cancelOrder(long id,JSONObject params) throws IOException,BTCException{
		if( !StringUtil.isJSONObjectOk(params,"username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空.");
		}
		CloseableHttpClient httpclient = this.getHttpClient();
		try{
			final String username = params.getString("username");

			HttpGet httpGet = (HttpGet)createHttpRequest(HttpMethod.GET, String.format(cancelOrder_url, id), true);
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","vip.btcchina.com");  
			httpGet.setHeader("Referer","https://vip.btcchina.com/trade/order");
			//读取redis用户Cookie
			String cookie = super.getCookie(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( cookie != null ){
				httpGet.setHeader("Cookie",cookie);
			}else{
				 return new Resp(ErrorCode.session_timeout,"会话已过期.");
			}
			return httpclient.execute(httpGet, new ResponseHandler<Resp>(){
				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户会话已过期,请重新登录!");
				        	return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
				        }
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						return new Resp(ErrorCode.SUCCESS,"撤单成功");
					}finally{
						response.close();
					}
				}
			});
		}finally{
			httpclient.close();
		}
	}
	/*
	 * 
		type	 string	 挂单类型。可能值：bid 或 ask
		price	 number	 价格
		currency	 string	 货币代码。可能值：CNY 或 BTC
		amount	 number	 挂单剩余数量。如果此值小于 amount_original，说明此挂单仅有部分成交
		amount_original	 number	 初始挂单数量
		date	 integer	 Unix 时间戳。自1970年1月1日以来的秒数
		status	 string	 挂单状态。可能值：open、closed 或 cancelled 
	 */
	@Override
	public Resp getOrders(boolean openOnly,JSONObject params) throws IOException,BTCException{

		if( !StringUtil.isJSONObjectOk(params,"username") ){
			return new Resp(ErrorCode.un_require, "用户名不能为空.");
		}
		CloseableHttpClient httpclient = this.getHttpClient();
		try{
			final String username = params.getString("username");

			HttpGet httpGet = (HttpGet)createHttpRequest(HttpMethod.GET, getOrders_url, true);
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","vip.btcchina.com");  
			httpGet.setHeader("Referer","https://vip.btcchina.com");
			//读取redis用户Cookie
			String cookie = super.getCookie(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( cookie != null ){
				httpGet.setHeader("Cookie",cookie);
			}else{
				 return new Resp(ErrorCode.session_timeout,"会话已过期.");
			}
			return httpclient.execute(httpGet, new ResponseHandler<Resp>(){
				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户会话已过期,请重新登录!");
				        	return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
				        }
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = null;
						try{
							br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
		                    String line = "";
		                    while(null != (line=br.readLine())){
		                    	htmlDocument.append(line);
		                    }
						}finally{
							//关闭流
		                    if ( null != br )br.close();
						}
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element usernameElement = doc.getElementById("username");
						Element passwordElement = doc.getElementById("password");
						//登录页面，cookie已经过期重新登录
						if( usernameElement != null && passwordElement != null){
							log.info("用户会话已过期,请重新登录!");
							return new Resp(ErrorCode.session_timeout,"用户会话已过期,请重新登录!");
						}
						Element table = doc.select("div[class=table-responsive] table").first();
						if( table == null ){
							return new Resp(ErrorCode.FAILURE,"您暂无挂单。");
						}
						Elements trs = table.select("tbody tr");
						JSONArray rowsJson = new JSONArray();
						String[] columns = {"date","type","amount_original","price","amount","unfillamount","status"};
						for (Element tr : trs) {
							JSONObject rowJson = new JSONObject();
							Elements tds = tr.select("td");
							for (int j = 0; j < tds.size(); j++) {
								Element cancelEle = tds.get(j).select("a").first();
								//撤单操作
								if( null != cancelEle ){
									String[] href = cancelEle.attr("href").split("/");
									rowJson.put(columns[j], "<a href=\"javascript:void();\" onclick=\"cancelOrder('"+href[href.length-1]+"')\">撤单</a>");
								}else{
									rowJson.put(columns[j], tds.get(j).html());
								}
							}
							rowsJson.add(rowJson);
						}
						return new Resp(ErrorCode.SUCCESS,"success",rowsJson);
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
