package com.btcoin.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.btcoin.common.ErrorCode;
import com.btcoin.common.JredisManager;
import com.btcoin.common.Resp;
import com.btcoin.exception.BtcoinException;
import com.btcoin.service.AbstractBtcWeb;
import com.btcoin.utils.StringUtil;

public class ChbtcWeb extends AbstractBtcWeb {
	private static final Logger log = Logger.getLogger(ChbtcWeb.class);
	private static final String login_url = "https://www.chbtc.com/user/doLogin";
	private static final String buyOrder_url = "https://www.chbtc.com/u/transaction/entrust/doEntrust";
	private static final String sellOrder_url = "https://www.chbtc.com/u/transaction/entrust/doEntrust";
	private static final String cancelOrder_url = "https://vip.btcchina.com/bbs/ucp.php?mode=login";
	private static final String getMarketDepth_url = "http://api.chbtc.com/data/ltc/depth";
	private static final String getTicker_url = "http://api.chbtc.com/data/ticker";
	private static final String getOrders_url = "https://vip.btcchina.com/bbs/ucp.php?mode=login";
	public static final String WEB_SERVICE_NAME = "chbtc";
	
	public JredisManager redisManager = JredisManager.getInstance();

	@Override
	public Resp login(final String username, String password) throws IOException {
		final BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient= this.getHttpClient(cookieStore);
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("nike", username));  
			nvps.add(new BasicNameValuePair("pwd", password));  
			nvps.add(new BasicNameValuePair("remember", 12+""));  
			nvps.add(new BasicNameValuePair("safe", 1+""));
			
			HttpPost httpPost = new HttpPost( login_url );
			httpPost.setEntity(new UrlEncodedFormEntity(nvps)); 
			httpPost.setHeader("Accept","*/*"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("Cookie","SessionID=b67a5bf6-4226-4ad2-b1cc-c291cdea2691; CNZZDATA30086321=cnzz_eid%3D1802718714-1388645174-%26ntime%3D1388645174%26cnzz_a%3D6%26ltime%3D1388645174920; on=0; JSESSIONID=258853DA2EA3A142178CA9A81F64D10F"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0 ");  
			httpPost.setHeader("Host","www.chbtc.com");  
			httpPost.setHeader("Referer","https://www.chbtc.com/");  
			
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0)
						throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {
				        	log.error(String.format("HTTP response:status code=%s, status message=[%s],用户[%s]登录失败。", 
        							status.getStatusCode(),
        							status.getReasonPhrase(),
        							username));
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }else{
							log.info("Login form get: " + response.getStatusLine());
							Scanner scan = new Scanner(response.getEntity().getContent());
							BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
							String result = "";
							try{ result = br.readLine(); }finally{br.close(); }
							Document document = Jsoup.parse(result);
							boolean isSuccess = Boolean.parseBoolean(document.select("State").text());
							if( isSuccess ){
								log.info("Post logon cookies:");
				                List<Cookie> cookies = cookieStore.getCookies();
				                if (cookies.isEmpty()) {
				                	log.info("None");
				                } else {
					                //保存用户cookies 到redis
					                redisManager.getJedis().set(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username),JSONArray.fromObject(cookies).toString());
					                log.info("save cookies to redis successful.");
				                	for (int i = 0; i < cookies.size(); i++) {
				                    	log.info("- " + cookies.get(i).toString());
				                    }
				                }
				                return new Resp(ErrorCode.SUCCESS,"登录成功.");
							}else{
								return new Resp(ErrorCode.login_error,document.select("Des").text());
							}
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
			return new Resp(ErrorCode.un_require,"用户名不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "password")){
			log.info("用户密码不能为空。");
			return new Resp(ErrorCode.pwd_require,"用户密码不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "tradepwd")){
			log.info("交易密码不能为空。");
			return new Resp(ErrorCode.tradepwd_require,"交易密码不能为空。");
		}
		
		CloseableHttpClient httpclient = this.getHttpClient();
		HttpPost httpPost = new HttpPost(String.format("%s?random=%s", buyOrder_url,Math.round(Math.random()*100)));
		try{
			String tradePwd = params.getString("tradepwd");
			final String username = params.getString("username");
			final String password = params.getString("password");
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("btcNumber", amount+""));  
			nvps.add(new BasicNameValuePair("isBuy", 1+""));  
			nvps.add(new BasicNameValuePair("unitPrice", price+"")); 
			//nvps.add(new BasicNameValuePair("realAccount", 4.3746+"")); 
			nvps.add(new BasicNameValuePair("safePassword", tradePwd)); 
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","www.chbtc.com");  
			httpPost.setHeader("Referer","https://www.chbtc.com/u/");
			
			//读取redis用户Cookie
			String cookie = super.getCookie(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( cookie != null ){
		        httpPost.setHeader("Cookie",cookie);
			}else{
				 return new Resp(ErrorCode.session_timeout,"会话已过期.");
			}
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						log.info("Login form get: " + response.getStatusLine());
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {
				        	log.error(String.format("HTTP response:status code=%s, status message=[%s],用户[%s]购买比特币失败。", 
        							status.getStatusCode(),
        							status.getReasonPhrase(),
        							username));
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }
				        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
						String result = "";
						try{ result = br.readLine(); }finally{br.close(); }
						Document document = Jsoup.parse(result);
						boolean isSuccess = Boolean.parseBoolean(document.select("State").text());
						if( isSuccess ){
			                return new Resp(ErrorCode.SUCCESS,"购买成功.");
						}else{
							return new Resp(ErrorCode.buy_error,document.select("Des").text());
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
	public Resp sellOrder(double price, double amount, JSONObject params)
			throws IOException {
		if(!StringUtil.isJSONObjectOk(params, "username")){
			log.info("用户名不能为空。");
			return new Resp(ErrorCode.un_require,"用户名不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "password")){
			log.info("用户密码不能为空。");
			return new Resp(ErrorCode.pwd_require,"用户密码不能为空。");
		}
		if(!StringUtil.isJSONObjectOk(params, "tradepwd")){
			log.info("交易密码不能为空。");
			return new Resp(ErrorCode.tradepwd_require,"交易密码不能为空。");
		}
		
		CloseableHttpClient httpclient = this.getHttpClient();
		HttpPost httpPost = new HttpPost(String.format("%s?random=%s", sellOrder_url,Math.round(Math.random()*100)));
		try{
			String tradePwd = params.getString("tradepwd");
			final String username = params.getString("username");
			final String password = params.getString("password");
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("btcNumber", amount+""));  
			nvps.add(new BasicNameValuePair("isBuy", "0"));  
			nvps.add(new BasicNameValuePair("unitPrice", price+"")); 
			//nvps.add(new BasicNameValuePair("realAccount", 4.3746+"")); 
			nvps.add(new BasicNameValuePair("safePassword", tradePwd)); 
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpPost.setHeader("Accept-Encoding","gzip, deflate"); 
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpPost.setHeader("Connection","keep-alive"); 
			httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpPost.setHeader("Host","www.chbtc.com");  
			httpPost.setHeader("Referer","https://www.chbtc.com/u/");
			
			//读取redis用户Cookie
			String cookie = super.getCookie(String.format("%s:u:%s:cookies", WEB_SERVICE_NAME,username));
			if( cookie != null ){
		        httpPost.setHeader("Cookie",cookie);
			}else{
				 return new Resp(ErrorCode.session_timeout,"会话已过期.");
			}
			return httpclient.execute(httpPost, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						log.info("Login form get: " + response.getStatusLine());
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {  
				        	log.error(String.format("HTTP response:status code=%s, status message=[%s],用户[%s]卖出比特币失败。", 
        							status.getStatusCode(),
        							status.getReasonPhrase(),
        							username));
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }
				        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
						String result = "";
						try{ result = br.readLine(); }finally{br.close(); }
						Document document = Jsoup.parse(result);
						boolean isSuccess = Boolean.parseBoolean(document.select("State").text());
						if( isSuccess ){
			                return new Resp(ErrorCode.SUCCESS,"卖出成功.");
						}else{
							return new Resp(ErrorCode.buy_error,document.select("Des").text());
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
	public Resp cancelOrder(long id, JSONObject params) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp getMarketDepth(JSONObject params)
			throws IOException {
		CloseableHttpClient httpclient = this.getHttpClient();
		HttpGet httpGet = new HttpGet(getMarketDepth_url);
		try{
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","www.chbtc.com");  
			httpGet.setHeader("Referer","https://www.chbtc.com/");
			
			return httpclient.execute(httpGet, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {  
				        	log.error(String.format("HTTP response:status code=%s, status message=[%s],获取深度行情失败。", 
        							status.getStatusCode(),
        							status.getReasonPhrase()));
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }
				        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
						String result = "";
						try{ result = br.readLine(); }finally{br.close(); }
						if(StringUtil.isNullOrEmpty(result)){
							return new Resp(ErrorCode.FAILURE,"无返回结果");
						}
						JSONObject dataJson = JSONObject.fromObject(result);
						return new Resp(ErrorCode.SUCCESS,"查询成功",dataJson);
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
	public Resp getOrders(double openOnly, JSONObject params)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resp getTicker() throws IOException, BtcoinException {
		CloseableHttpClient httpclient = this.getHttpClient();
		HttpGet httpGet = new HttpGet(getTicker_url);
		try{
			httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
			httpGet.setHeader("Accept-Encoding","gzip, deflate"); 
			httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3"); 
			httpGet.setHeader("Connection","keep-alive"); 
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");  
			httpGet.setHeader("Host","www.chbtc.com");  
			httpGet.setHeader("Referer","https://www.chbtc.com/");
			
			return httpclient.execute(httpGet, new ResponseHandler<Resp>() {
				@Override
				public Resp handleResponse(HttpResponse arg0) throws ClientProtocolException, IOException {
					CloseableHttpResponse response = (CloseableHttpResponse)arg0;
					try{
						StatusLine status = response.getStatusLine();  
				        if (status.getStatusCode() != 200) {  
				        	log.error(String.format("HTTP response:status code=%s, status message=[%s],获取实时行情失败。", 
        							status.getStatusCode(),
        							status.getReasonPhrase()));
				            throw new NoHttpResponseException(  
				                    "Did not receive successful HTTP response: status code = "  
				                            + status.getStatusCode() + ", status message = ["  
				                            + status.getReasonPhrase() + "]");  
				        }
				        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
						String result = "";
						try{ result = br.readLine(); }finally{br.close(); }
						if(StringUtil.isNullOrEmpty(result)){
							return new Resp(ErrorCode.FAILURE,"无返回结果");
						}
						JSONObject dataJson = JSONObject.fromObject(result).getJSONObject("ticker");
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
