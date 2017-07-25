package org.eclipse.kura.cloud.rest;

import okhttp3.OkHttpClient;

public interface RESTClient {
	
	/*
	 * private String protocol;
	 * private String host;
	 * private int port;
	 */
	public void execute(KuraRequest request);	
	//public void executeASync(KuraRequest request);
}

