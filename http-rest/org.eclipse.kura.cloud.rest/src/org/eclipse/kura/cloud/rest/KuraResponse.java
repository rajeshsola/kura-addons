package org.eclipse.kura.cloud.rest;

import okhttp3.Headers;

public class KuraResponse {
	int statusCode;
	String body;
	Headers headers;
	public KuraResponse()
	{
		
	}
	public void setHeaders(Headers headers)
	{
		this.headers=headers;
	}
	public void setStatusCode()
	{
		
	}
}
