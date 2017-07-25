package org.eclipse.kura.cloud.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
//import static okhttp3.curl.Main.fromArgs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*public interface KuraRequest
{
	public void setMethod(String metthod);
	public void setProtocol(String protocol);
	public void setHost(String host);
	public void setPort(int port);
	public void setPath(String path);
	public void setURL(String url);
	public void addHeader(String name,Object value);
	public void addQueryStringEntry(String name,String value);
	public void setData(String data);
	public void build();
	public void execute();
}*/
public class KuraRequest
{
	
	private String method="GET";
	private String protocol="http";
	private String host;
	private int port;
	private String path="";
	private StringBuilder query;
	private String baseURL;
	private String body;
	private String contentType;
	private Map<String,Object> headers=new HashMap<String,Object>();
	
	
	private final OkHttpClient client = new OkHttpClient();
	Request request;
	
	public KuraRequest()
	{
		query=new StringBuilder();
	}
		
	public void setMethod(String method) {
		this.method=method;		
	}
	public void setProtocol(String protocol) {
		this.protocol=protocol;
		
	}
	public void setHost(String host) {
		this.host=host;
		
	}
	public void setPort(int port) {
		this.port=port;
		
	}
	public void setPath(String path) {
		this.path=path;		
	}
	public void addHeader(String name, Object value) {
		// TODO Auto-generated method stub
		
	}
	public void addQueryStringEntry(String name, String value) {
		if(query.length()>0)
			query.append("&");
		query.append(String.format("%s=%s",URLEncoder.encode(name),URLEncoder.encode(value)));		
	}
	public void setURL(String url) {
		this.baseURL=url;		
	}
	public String getBaseURL()
	{
		if(query.length()>0) //TODO
			baseURL = protocol+"://"+host+":"+port + path + "?" + query.toString();
		else
			baseURL=protocol+"://"+host+":"+port + path;
		return this.baseURL;
	}
	public Map<String,Object> getHeaders()
	{
		return headers;
	}
	public String getMethod()
	{
		return method;
	}
	public void setRequestBody(String body)
	{
		this.body=body;
	}
	public void setContentType(String type)
	{
		contentType=type;
	}
	public String getContentType()
	{
		return contentType;
	}
	public String getBody()
	{
		return body;
	}
}
