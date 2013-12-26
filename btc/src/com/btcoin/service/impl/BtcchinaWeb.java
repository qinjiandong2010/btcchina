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

import com.btcoin.BtcoinException;
import com.btcoin.common.ErrorCode;
import com.btcoin.common.JredisManager;
import com.btcoin.common.Resp;
import com.btcoin.service.AbstractBtcWeb;
import com.btcoin.utils.StringUtil;

/**
 * btcchina
 * @author Administrator
 *
 */
public class BtcchinaWeb extends AbstractBtcWeb{

	private static final Logger log = Logger.getLogger(BtcchinaWeb.class);
	private static final String login_url = "https://vip.btcchina.com/bbs/ucp.php?mode=login&change_lang=zh_cmn_hans";
	private static final String buyOrder_url = "https://vip.btcchina.com/trade/buy";
	private static final String sellOrder_url = "https://vip.btcchina.com/trade/sell";
	private static final String cancelOrder_url = "https://vip.btcchina.com/bbs/ucp.php?mode=login";
	private static final String getMarketDepth_url = "https://vip.btcchina.com/trade/depth";
	private static final String getTicker_url = "https://data.btcchina.com/data/ticker";
	private static final String getOrders_url = "https://vip.btcchina.com/bbs/ucp.php?mode=login";
	public static final String redisKey = "btcchina";
	
	public JredisManager redisManager = JredisManager.getInstance();
	
	@Override
	public Resp login(final String username, String password) throws IOException,BtcoinException{
		
		if( StringUtil.isEmpty(username) ){
			return new Resp(ErrorCode.un_require$10100,"username is require.");
		}
		if( StringUtil.isEmpty(password) ){
			return new Resp(ErrorCode.pwd_require$10101,"password is require.");
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
				        	return new Resp(ErrorCode.login_error$10103,"Did not receive successful HTTP response: status code = "  
		                            + status.getStatusCode() + ", status message = ["  
		                            + status.getReasonPhrase() + "]");
				        }else{
							Header[] locations = response.getHeaders("Location");
							if( locations.length == 0 ){
								log.error("Login failed.");
								return new Resp(ErrorCode.FAILURE,"login failed.");
							}
							log.info("Login form get: " + response.getStatusLine());

							log.info("Post logon cookies:");
			                List<Cookie> cookies = cookieStore.getCookies();
			                if (cookies.isEmpty()) {
			                	log.info("None");
			                	return new Resp(ErrorCode.login_error$10103,"login failed.");
			                } else {
				                //保存用户cookies 到redis
				                redisManager.getJedis().set(redisKey+String.format(":u:%s:sid", username),JSONArray.fromObject(cookies).toString());
				                log.info("save cookies to redis successful.");
			                	for (int i = 0; i < cookies.size(); i++) {
			                    	log.info("- " + cookies.get(i).toString());
			                    }
			                }
				        }
				        return new Resp(ErrorCode.SUCCESS,"Login success.");
					}finally{
						//这里加上会报sockie is close异常
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
			throw new BtcoinException(ErrorCode.un_require$10000, "username is require.");
		}
		if( !StringUtil.isJSONObjectOk(params,"password") ){
			throw new BtcoinException(ErrorCode.pwd_require$10001, "password is require.");
		}
		if( !StringUtil.isJSONObjectOk(params,"tradepwd") ){
			throw new BtcoinException(ErrorCode.tradepwd_require$10002, "tradepwd is require.");
		}
		if( !StringUtil.isJSONObjectOk(params,"ordertype") ){
			throw new BtcoinException(ErrorCode.ordertype_require$10003, "ordertype is require.");
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
			httpGet.setHeader("Cookie",super.getCookie(redisKey+String.format(":u:%s:sid", username)));
			
			String no_csrf_buybtc = httpclient.execute(httpGet, new ResponseHandler<String>(){

				@Override
				public String handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户cookie已过期请重新登录!");
				        	throw new BtcoinException(ErrorCode.session_timeout$10005,"The user cookie has expired, please login again.");
				        }
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
	                    String line = "";
	                    while(null != (line=br.readLine())){
	                    	htmlDocument.append(line);
	                    }
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element usernameElement = doc.getElementById("username");
						Element passwordElement = doc.getElementById("password");
						//登录页面，cookie已经过期重新登录
						if( usernameElement != null && passwordElement != null){
							log.info("用户cookie已过期请重新登录!");
							throw new BtcoinException(ErrorCode.session_timeout$10005,"用户cookie已过期请重新登录!");
						}
						return doc.getElementById("no_csrf_buybtc").val();
					}finally{
						response.close();
					}
				}
			});
			
			HttpPost httpPost = (HttpPost)super.createHttpRequest(HttpMethod.POST, buyOrder_url, true );
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","vip.btcchina.com");  
			httpPost.setHeader("Referer",buyOrder_url);
			httpPost.setHeader("Cookie",super.getCookie(redisKey+String.format(":u:%s:sid", username)));
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("amount", amount+""));  
			nvps.add(new BasicNameValuePair("no_csrf_buybtc", no_csrf_buybtc));  
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
				        	throw new BtcoinException(ErrorCode.session_timeout$10005,"The user cookie has expired, please login again.");
				        }
						
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
	                    String line = "";
	                    while(null != (line=br.readLine())){
	                    	htmlDocument.append(line);
	                    }
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element element = doc.select("div[class=alert alert-success]").first();
						if( null != element){
							return new Resp(ErrorCode.SUCCESS,element.text());
						}else{
							element = doc.select("div[class=alert alert-danger]").first();
							throw new BtcoinException(ErrorCode.buy_error$10004,element.text());
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
	public Resp sellOrder(double price, double amount,JSONObject params) throws IOException,BtcoinException{
		if( !StringUtil.isJSONObjectOk(params,"username") ){
			throw new BtcoinException(ErrorCode.un_require$10200, "username is require.");
		}
		if( !StringUtil.isJSONObjectOk(params,"password") ){
			throw new BtcoinException(ErrorCode.pwd_require$10201, "password is require.");
		}
		if( !StringUtil.isJSONObjectOk(params,"tradepwd") ){
			throw new BtcoinException(ErrorCode.tradepwd_require$10202, "tradepwd is require.");
		}
		if( !StringUtil.isJSONObjectOk(params,"ordertype") ){
			throw new BtcoinException(ErrorCode.ordertype_require$10203, "ordertype is require.");
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
			httpGet.setHeader("Cookie",super.getCookie(redisKey+String.format(":u:%s:sid", username)));
			
			String no_csrf_sellbtc = httpclient.execute(httpGet, new ResponseHandler<String>(){

				@Override
				public String handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户cookie已过期请重新登录!");
				        	throw new BtcoinException(ErrorCode.session_timeout$10205,"用户cookie已过期请重新登录!");
				        }
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
	                    String line = "";
	                    while(null != (line=br.readLine())){
	                    	htmlDocument.append(line);
	                    }
						Document doc = Jsoup.parse(htmlDocument.toString());
						try{
							Element usernameElement = doc.getElementById("username");
							Element passwordElement = doc.getElementById("password");
							//登录页面，cookie已经过期重新登录
							if( usernameElement != null && passwordElement != null){
								log.info("用户cookie已过期请重新登录!");
								throw new BtcoinException(ErrorCode.session_timeout$10205,"用户cookie已过期请重新登录!");
							}
							return doc.getElementById("no_csrf_sellbtc").val();
						}catch (Exception e) {
							log.error("get no_csrf_sellbtc error:"+e.getMessage());
							throw new BtcoinException(ErrorCode.session_timeout$10205,"用户cookie已过期请重新登录!");
						}
					}finally{
						response.close();
					}
				}
			});
			HttpPost httpPost = (HttpPost)super.createHttpRequest(HttpMethod.POST, sellOrder_url, true );
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","vip.btcchina.com");  
			httpPost.setHeader("Referer",sellOrder_url);
			httpPost.setHeader("Cookie",super.getCookie(redisKey+String.format(":u:%s:sid", username)));
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("amount", amount+""));  
			nvps.add(new BasicNameValuePair("no_csrf_sellbtc",no_csrf_sellbtc));  
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
				        	throw new BtcoinException(ErrorCode.session_timeout$10205,"用户cookie已过期请重新登录!");
				        }
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
	                    String line = "";
	                    while(null != (line=br.readLine())){
	                    	htmlDocument.append(line);
	                    }
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element element = doc.select("div[class=alert alert-success]").first();
						if( null != element){
							return new Resp(ErrorCode.SUCCESS,element.text());
						}else{
							element = doc.select("span[class=help-inline]").first();
							throw new BtcoinException(ErrorCode.sell_error$10204,element.text());
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
	public Resp getMarketDepth(final long limit,JSONObject params) throws IOException,BtcoinException{
		
		if( !StringUtil.isJSONObjectOk(params, "tradetype")){
			throw new BtcoinException(ErrorCode.tradetype_require$10303,"tradettype is require.");
		}
		String tradeType = params.getString("tradetype");
		if( !"buy".equals(tradeType) && !"sell".equals(tradeType) ){
			throw new BtcoinException(ErrorCode.tradetype_require$10303,"tradettype ranges [buy,sell].");
		}
		final String dataKey = redisKey+":getMarketDepth:"+tradeType;
		boolean exists = redisManager.getJedis().exists(dataKey);
		//如果redis有数据，从redis返回数据
		if( exists ){
			List<String> resList = redisManager.getJedis().lrange(dataKey, 0, limit);
			return new Resp(ErrorCode.SUCCESS,"success",resList,null,null);
		}
		
		if( !StringUtil.isJSONObjectOk(params,"username") ){
			throw new BtcoinException(ErrorCode.un_require$10200, "username is require.");
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
			httpGet.setHeader("Cookie",super.getCookie(redisKey+String.format(":u:%s:sid", username)));
			
			return httpclient.execute(httpGet, new ResponseHandler<Resp>(){

				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() >= 302) {
				        	log.info("用户cookie已过期请重新登录!");
				        	throw new BtcoinException(ErrorCode.session_timeout$10205,"用户cookie已过期请重新登录!");
				        }
						HttpEntity entity = response.getEntity();
						StringBuilder htmlDocument = new StringBuilder();
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
	                    String line = "";
	                    while(null != (line=br.readLine())){
	                    	htmlDocument.append(line);
	                    }
						Document doc = Jsoup.parse(htmlDocument.toString());
						Element usernameElement = doc.getElementById("username");
						Element passwordElement = doc.getElementById("password");
						//登录页面，cookie已经过期重新登录
						if( usernameElement != null && passwordElement != null){
							log.info("用户cookie已过期请重新登录!");
							throw new BtcoinException(ErrorCode.session_timeout$10205,"用户cookie已过期请重新登录!");
						}
						Elements tables = doc.select("div table[class=table table-striped table-hover]");
						
						for (int i = 0,j = tables.size(); i < j; i ++) {
							Elements trs = tables.get(i).select("tbody tr");
							String key = redisKey+":getMarketDepth:"+(i == 0 ? "buy":"sell");
							for (Element tr : trs) {
								JSONArray rowJson = new JSONArray();
								Elements tds = tr.select("td");
								for (Element td : tds) {
									rowJson.add(td.text());
								}
								redisManager.getJedis().rpush(key, rowJson.toString());
							}
							redisManager.getJedis().expire(key, 60);
						}
						List<String> resList = redisManager.getJedis().lrange(dataKey, 0, limit);
						return new Resp(ErrorCode.SUCCESS,"success",resList,null,null);
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
	public Resp getTicker() throws IOException, BtcoinException {
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
						BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
	                    String line = "";
	                    while(null != (line=br.readLine())){
	                    	htmlDocument.append(line);
	                    }
						JSONObject tickerJson = JSONObject.fromObject(htmlDocument.toString());
						redisManager.getJedis().set(redisKey+":ticker",tickerJson.toString());
		                return new Resp(ErrorCode.SUCCESS,"success",null,null,tickerJson);
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
	public Resp cancelOrder(long id,JSONObject params) throws IOException,BtcoinException{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Resp getOrders(double openOnly,JSONObject params) throws IOException,BtcoinException{
		return null;
	}
}
