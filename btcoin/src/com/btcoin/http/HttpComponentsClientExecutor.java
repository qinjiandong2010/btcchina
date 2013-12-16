package com.btcoin.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

public class HttpComponentsClientExecutor {
	private static final int    DEFAULT_MAX_TOTAL_CONNECTIONS     = 100;  
	  
    private static final int    DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;                 //notice IE 6,7,8  
  
    private static final int    DEFAULT_CONN_TIMEOUT_MILLISECONDS = 5 * 1000;  
  
    private static final int    DEFAULT_READ_TIMEOUT_MILLISECONDS = 60 * 1000;  
  
    private static final String HTTP_HEADER_CONTENT_ENCODING      = "Content-Encoding";  
    private static final String ENCODING_GZIP                     = "gzip";  
  
    private HttpClient          httpClient;  
  
    /** 
     * Create a new instance of the HttpComponentsClient with a default 
     * {@link HttpClient} that uses a default 
     * {@link org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager}. 
     */  
    public HttpComponentsClientExecutor() {  
        SchemeRegistry schemeRegistry = new SchemeRegistry();  
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));  
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));  
  
        ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(  
                schemeRegistry);  
        connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);  
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);  
        this.httpClient = new DefaultHttpClient(connectionManager);  
  
        setConnectTimeout(DEFAULT_CONN_TIMEOUT_MILLISECONDS);  
        setReadTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS);  
    }  
  
    /** 
     * Create a new instance of the HttpComponentsClient with the given 
     * {@link HttpClient} instance. 
     *  
     * @param httpClient the HttpClient instance to use for this request 
     */  
    public HttpComponentsClientExecutor(HttpClient httpClient) {  
        //Validate.notNull(httpClient, "HttpClient must not be null");  
        //notice: if you want to custom exception recovery mechanism   
        //you should provide an implementation of the HttpRequestRetryHandler interface.  
        this.httpClient = httpClient;  
    }  
  
    /** 
     * Set the {@code HttpClient} used by this request. 
     */  
    public void setHttpClient(HttpClient httpClient) {  
        this.httpClient = httpClient;  
    }  
  
    /** 
     * Return the {@code HttpClient} used by this request. 
     */  
    public HttpClient getHttpClient() {  
        return this.httpClient;  
    }  
  
    /** 
     * Set the connection timeout for the underlying HttpClient. A timeout value 
     * of 0 specifies an infinite timeout. 
     *  
     * @param timeout the timeout value in milliseconds 
     */  
    public void setConnectTimeout(int timeout) {  
        //Validate.isTrue(timeout >= 0, "Timeout must be a non-negative value");  
        getHttpClient().getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,  
                timeout);  
    }  
  
    /** 
     * Set the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout 
     * for waiting for data or, put differently, a maximum period inactivity 
     * between two consecutive data packets.A timeout value of 0 specifies an 
     * infinite timeout. 
     *  
     * @param timeout the timeout value in milliseconds 
     */  
    public void setReadTimeout(int timeout) {  
        //Validate.isTrue(timeout >= 0, "Timeout must be a non-negative value");  
        getHttpClient().getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);  
    }  
  
    /** 
     * Create a Commons HttpMethodBase object for the given HTTP method and URI 
     * specification. 
     *  
     * @param httpMethod the HTTP method 
     * @param uri the URI 
     * @return the Commons HttpMethodBase object 
     */  
    protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri,List <NameValuePair> nvps) {  
        switch (httpMethod) {  
            case GET:  
                return new HttpGet(uri);  
            case DELETE:  
                return new HttpDelete(uri);  
            case HEAD:  
                return new HttpHead(uri);  
            case OPTIONS:  
                return new HttpOptions(uri);  
            case POST:  
            	HttpPost httpPost = new HttpPost(uri);
            	httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            	return httpPost;
            case PUT:  
                return new HttpPut(uri);  
            case TRACE:  
                return new HttpTrace(uri);  
            default:  
                throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);  
        }  
    }  
  
    /** 
     * Execute the given method on the provided URI. 
     *  
     * @param method the HTTP method to execute (GET, POST, etc.) 
     * @param url the fully-expanded URL to connect to 
     * @param responseHandler httpClient will automatically take care of 
     *            ensuring release of the connection back to the connection 
     *            manager regardless whether the request execution succeeds or 
     *            causes an exception,if using this response handler 
     * @return an response object's string representation 
     * @throws IOException 
     * @throws ClientProtocolException 
     */  
    public String doExecuteRequest(HttpMethod httpMethod, URI uri,  
                                   ResponseHandler<String> responseHandler)  
            throws ClientProtocolException, IOException {  
        return httpClient.execute(createHttpUriRequest(httpMethod, uri,null), responseHandler);  
    }  
  
    public InputStream doExecuteRequest(HttpMethod httpMethod, URI uri)  
            throws ClientProtocolException, IOException {  
        //1.  
        HttpUriRequest httpUriRequest = createHttpUriRequest(httpMethod, uri,null);  
        //2.  
        HttpResponse response = httpClient.execute(httpUriRequest);  
        //3.  
        validateResponse(response);  
        //4.  
        return getResponseBody(response);  
    }
    
    public InputStream doExecuteRequest(HttpMethod httpMethod, URI uri,List <NameValuePair> nvps)  
            throws ClientProtocolException, IOException {  
        //1.  
        HttpUriRequest httpUriRequest = createHttpUriRequest(httpMethod, uri,nvps);
        //2.  
        HttpResponse response = httpClient.execute(httpUriRequest);  
        //3.  
        validateResponse(response);  
        //4.  
        return getResponseBody(response);  
    }
    
    /** 
     * Validate the given response, throwing an exception if it does not 
     * correspond to a successful HTTP response. 
     * <p> 
     * Default implementation rejects any HTTP status code beyond 2xx, to avoid 
     * parsing the response body and trying to deserialize from a corrupted 
     * stream. 
     *  
     * @param config the HTTP invoker configuration that specifies the target 
     *            service 
     * @param response the resulting HttpResponse to validate 
     * @throws NoHttpResponseException 
     * @throws java.io.IOException if validation failed 
     */  
    protected void validateResponse(HttpResponse response) throws IOException {  
  
        StatusLine status = response.getStatusLine();  
        if (status.getStatusCode() >= 300) {  
            throw new NoHttpResponseException(  
                    "Did not receive successful HTTP response: status code = "  
                            + status.getStatusCode() + ", status message = ["  
                            + status.getReasonPhrase() + "]");  
        }  
    }  
  
    /** 
     * Extract the response body 
     * <p> 
     * The default implementation simply fetches the response body stream. If 
     * the response is recognized as GZIP response, the InputStream will get 
     * wrapped in a GZIPInputStream. 
     *  
     * @param httpResponse the resulting HttpResponse to read the response body 
     *            from 
     * @return an InputStream for the response body 
     * @throws java.io.IOException if thrown by I/O methods 
     * @see #isGzipResponse 
     * @see java.util.zip.GZIPInputStream 
     */  
    protected InputStream getResponseBody(HttpResponse httpResponse) throws IOException {  
  
        if (isGzipResponse(httpResponse)) {  
            return new GZIPInputStream(httpResponse.getEntity().getContent());  
        } else {  
            return httpResponse.getEntity().getContent();  
        }  
    }  
  
    /** 
     * Determine whether the given response indicates a GZIP response. 
     * <p> 
     * The default implementation checks whether the HTTP "Content-Encoding" 
     * header contains "gzip" (in any casing). 
     *  
     * @param httpResponse the resulting HttpResponse to check 
     * @return whether the given response indicates a GZIP response 
     */  
    protected boolean isGzipResponse(HttpResponse httpResponse) {  
        Header encodingHeader = httpResponse.getFirstHeader(HTTP_HEADER_CONTENT_ENCODING);  
        return (encodingHeader != null && encodingHeader.getValue() != null && encodingHeader  
                .getValue().toLowerCase().contains(ENCODING_GZIP));  
    }  
  
    /** 
     * Shutdown hook that closes the underlying 
     * {@link org.apache.http.conn.ClientConnectionManager 
     * ClientConnectionManager}'s connection pool, if any. 
     */  
    public void destroy() {  
        getHttpClient().getConnectionManager().shutdown();  
    }  
  
    enum HttpMethod {  
        GET,  
        POST,  
        HEAD,  
        OPTIONS,  
        PUT,  
        DELETE,  
        TRACE  
    }  
    
    public static void main(String[] args) throws Exception {
		HttpComponentsClientExecutor client = new HttpComponentsClientExecutor();
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "btcchinatest"));
		nvps.add(new BasicNameValuePair("password", "123456789"));
		
		Scanner scanner = new Scanner(client.doExecuteRequest(HttpMethod.POST, new URI("https://vip.btcchina.com/bbs/ucp.php?mode=login&amp;sid=564658da6b26b872a09a29cedf572f19"),nvps));
		
		while( scanner.hasNext() ){
			System.out.println(new String(scanner.nextLine().getBytes(),"utf-8"));
		}
		
	}
}
