package com.btcoin.service;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.btcoin.common.EnumConfig;
import com.btcoin.common.JredisManager;
import com.btcoin.common.Resp;
import com.btcoin.common.SystemConfig;
import com.btcoin.exception.BTCException;
import com.btcoin.utils.PropertiesUtil;

public abstract class AbstractBTCWeb {
	
	private static final Logger log = Logger.getLogger(AbstractBTCWeb.class);
	
	/**
	 * 从redist获取cookies
	 * @return
	 * @throws BTCException 
	 */
	protected String getCookie(String key) throws BTCException {
		//读取redis用户Cookie
		JredisManager redisManager = JredisManager.getInstance();
		String data = redisManager.getJedis().get(key);
		if( data != null ){
	        JSONArray cookies = JSONArray.fromObject(data);
	        String cookieHeader = "";
	        for (Object obj : cookies) {
				JSONObject cookie = (JSONObject) obj;
				cookieHeader += (cookie.getString("name")+"="+cookie.getString("value"))+";";
			}
	        return cookieHeader;
		}
		return null;
	}
	protected CloseableHttpClient getHttpClient() {
		return getHttpClient(new BasicCookieStore());
	}
	protected CloseableHttpClient getHttpClient(BasicCookieStore cookieStore) {
		try{
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null,new TrustManager[]{ new X509TrustManager() {
				
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
			}},new SecureRandom());
	        return HttpClients.custom().setSSLSocketFactory(new SSLSocketFactory(sslContext)).setDefaultCookieStore(cookieStore).build();
		}catch(Exception err){
			log.error("Create SSL HttpClient error:"+err.getMessage());
			return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		}
	}
	/** 
     * Create a Commons HttpMethodBase object for the given HTTP method and URI 
     * specification. 
     *  
     * @param httpMethod the HTTP method 
     * @param uri the URI 
     * @return the Commons HttpMethodBase object 
     */  
    protected HttpRequestBase createHttpRequest(HttpMethod httpMethod, String uri, boolean proxy) {  
        HttpRequestBase requestBase = null;
    	switch (httpMethod) {  
            case GET:  
            	requestBase = new HttpGet(uri);
            	break;
            case DELETE:  
            	requestBase = new HttpDelete(uri); 
            	break;
            case HEAD:  
            	requestBase = new HttpHead(uri);
            	break;
            case OPTIONS:  
            	requestBase = new HttpOptions(uri);
            	break;
            case POST:  
            	requestBase = new HttpPost(uri);
            	break;
            case PUT:  
            	requestBase = new HttpPut(uri); 
            	break;
            case TRACE:  
            	requestBase = new HttpTrace(uri);  
            	break;
            default:  
                throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);  
        }
    	//是否启用proxy代理连接
    	if( requestBase != null && proxy && SystemConfig.ENABLED_PROXY ){
    		String host = PropertiesUtil.getProperty(EnumConfig.FILE_SYSCONF_PROPERTIES.getName(), EnumConfig.proxy_host.getName(),"127.0.0.1");
    		int port = PropertiesUtil.getPropertyInt(EnumConfig.FILE_SYSCONF_PROPERTIES.getName(), EnumConfig.proxy_port.getName(),8070);
    		String protocol = PropertiesUtil.getProperty(EnumConfig.FILE_SYSCONF_PROPERTIES.getName(), EnumConfig.proxy_protocol.getName(),"http");
    		//设置proxy代理连接
    		HttpHost proxyHost = new HttpHost(host, port, protocol);
            RequestConfig config = RequestConfig.custom().setProxy(proxyHost).build();
            requestBase.setConfig(config);
    	}
    	return requestBase;
    }
    public enum HttpMethod {  
        GET,  
        POST,  
        HEAD,  
        OPTIONS,  
        PUT,  
        DELETE,  
        TRACE  
    } 
	/**
	 * 登录
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public abstract Resp login(String username,String password) throws IOException,BTCException;
	
	/**
	 * 下比特币买单
	 * @param price
	 * @param amount
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws BTCException
	 */
	public abstract Resp buyOrder(double price,double amount,JSONObject params) throws IOException,BTCException;
	
	/**
	 * 下比特币卖单。
	 * @param price
	 * @param amount
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws BTCException
	 */
	public abstract Resp sellOrder(double price,double amount,JSONObject params) throws IOException,BTCException;
	
	/**
	 * 取消一个还未完全成交的挂单，其状态应该为“open”。
	 * @param id
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws BTCException
	 */
	public abstract Resp cancelOrder(long id,JSONObject params) throws IOException,BTCException;
	
	/**
	 * 获得完整的市场深度。返回全部尚未成交的买单和卖单。
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws BTCException
	 */
	public abstract Resp getMarketDepth(JSONObject params) throws IOException,BTCException;
	
	/**
	 * 获得全部挂单的状态。
	 * @param openOnly
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws BTCException
	 */
	public abstract Resp getOrders(boolean openOnly,JSONObject params) throws IOException,BTCException;
	/**
	 * 获取实时行情。
	 * @return
	 * @throws IOException
	 * @throws BTCException
	 */
	public abstract Resp getTicker()throws IOException,BTCException;
}
