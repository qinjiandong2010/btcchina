package com.btcoin.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ClientFormLogin {
	/**
	 * 
	 * @param httpClient
	 * @return
	 */
	public static DefaultHttpClient useTrustingTrustManager(
			DefaultHttpClient httpClient) {
		try {
			// First create a trust manager that won't care.
			X509TrustManager trustManager = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// Don't do anything.
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// Don't do anything.
				}

				public X509Certificate[] getAcceptedIssuers() {
					// Don't do anything.
					return null;
				}
			};

			// Now put the trust manager into an SSLContext.
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { trustManager }, null);

			// Use the above SSLContext to create your socket factory
			// (I found trying to extend the factory a bit difficult due to a
			// call to createSocket with no arguments, a method which doesn't
			// exist anywhere I can find, but hey-ho).
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf
					.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			// If you want a thread safe client, use the ThreadSafeConManager,
			// but
			// otherwise just grab the one from the current client, and get hold
			// of its
			// schema registry. THIS IS THE KEY THING.
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry schemeRegistry = ccm.getSchemeRegistry();

			// Register our new socket factory with the typical SSL port and the
			// correct protocol name.
			schemeRegistry.register(new Scheme("https", sf, 443));

			// Finally, apply the ClientConnectionManager to the Http Client
			// or, as in this example, create a new one.
			return new DefaultHttpClient(ccm, httpClient.getParams());
		} catch (Throwable t) {
			// AND NEVER EVER EVER DO THIS, IT IS LAZY AND ALMOST ALWAYS WRONG!
			t.printStackTrace();
			return null;
		}
	}

	public static void test() throws Exception{
		System.setProperty("javax.net.ssl.trustStore", "D:/jiandong/workspace/btcoin/btcchina_ssl");
		
		BasicCookieStore cookieStore = new BasicCookieStore();
		 KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
	        FileInputStream instream = new FileInputStream(new File("D:/jiandong/workspace/btcoin/btcchina_ssl"));
	        try {
	            trustStore.load(instream, "1".toCharArray());
	        } finally {
	            instream.close();
	        }
	        SSLContext sslcontext = SSLContexts.custom()
	                .loadTrustMaterial(trustStore)
	                .build();

	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", "btcchinatest"));
			nvps.add(new BasicNameValuePair("password", "123456789"));
			nvps.add(new BasicNameValuePair("kf5", "0"));
			nvps.add(new BasicNameValuePair("login", "µÇÂ¼"));
			nvps.add(new BasicNameValuePair("redirect", "index.php"));
			nvps.add(new BasicNameValuePair("sid",
					"d0a575738ca0330fcab463ccd1824b11"));
			nvps.add(new BasicNameValuePair("time", "0"));

			HttpPost httpPost = new HttpPost(
					"https://vip.btcchina.com/bbs/ucp.php?mode=login");
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpPost
					.setHeader("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpPost.setHeader("Accept-Encoding", "gzip, deflate");
			httpPost.setHeader("Accept-Language",
					"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			httpPost.setHeader("Connection", "keep-alive");
			httpPost
					.setHeader(
							"Cookie",
							"Hm_lvt_4b4a9e41d8a28c344a964dfa3baf4f6d=1386908362; Hm_lpvt_4b4a9e41d8a28c344a964dfa3baf4f6d=1386912079; _ga=GA1.2.1334210409.1386908364; style_cookie=null; phpbb3_ja5fa_u=1; phpbb3_ja5fa_k=; phpbb3_ja5fa_sid=d0a575738ca0330fcab463ccd1824b11; PHPSESSID=hcejpctoeihsp6e6k1qocmbf26");
			httpPost
					.setHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0 ");
			httpPost.setHeader("Host", "vip.btcchina.com");
			httpPost.setHeader("Referer", "https://vip.btcchina.com/");

			HttpHost proxy = new HttpHost("127.0.0.1", 8087);
			// set proxy
			RequestConfig requestConfig = RequestConfig.custom()
					.setProxy(proxy).build();
			httpPost.setConfig(requestConfig);

			// httpPost.setHeader("Cookie", cookie);
			HttpResponse response = httpclient.execute(httpPost);

			System.out.println(Arrays.toString(httpPost.getAllHeaders()));

			HttpEntity entity = response.getEntity();
			Scanner scanner = new Scanner(entity.getContent());
			while (scanner.hasNext()) {
				System.out.println(new String(scanner.nextLine().getBytes(),
						"utf-8"));
			}
			System.out.println("Login form get: " + response.getStatusLine());
			EntityUtils.consume(entity);

			System.out.println("Post logon cookies:");
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}
		} finally {
			httpclient.close();
		}
	}

	public static void main(String[] args) throws Exception {
		test();
		/*
		 * BasicCookieStore cookieStore = new BasicCookieStore();
		 * CloseableHttpClient httpclient =
		 * HttpClients.custom().setDefaultCookieStore(cookieStore).build(); try {
		 * HttpParams params = httpclient.getParams();
		 * params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
		 * 
		 * HttpPost httpost = new HttpPost("http://localhost:8081/login.html");
		 * List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		 * nvps.add(new BasicNameValuePair("userName", "spadmin")); nvps.add(new
		 * BasicNameValuePair("password", "123456")); httpost.setEntity(new
		 * UrlEncodedFormEntity(nvps, Consts.UTF_8));
		 * 
		 * CloseableHttpResponse response2 = httpclient.execute(httpost); try {
		 * HttpEntity entity = response2.getEntity();
		 * 
		 * System.out.println("Login form get: " + response2.getStatusLine());
		 * EntityUtils.consume(entity);
		 * 
		 * System.out.println("Post logon cookies:"); List<Cookie> cookies =
		 * cookieStore.getCookies(); if (cookies.isEmpty()) {
		 * System.out.println("None"); } else { for (int i = 0; i <
		 * cookies.size(); i++) { System.out.println("- " +
		 * cookies.get(i).toString()); } }
		 *  } finally { response2.close(); } } finally { httpclient.close(); }
		 */
	}
}
