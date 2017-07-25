package org.eclipse.kura.sample.restdemo;

import org.eclipse.kura.cloud.rest.KuraRequest;
import org.eclipse.kura.cloud.rest.RESTClient;
import org.eclipse.kura.cloud.rest.RESTService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyRESTDemo {
	
	private static final Logger s_logger = LoggerFactory.getLogger(MyRESTDemo.class);
	private static final String APP_ID = "org.eclipse.kura.sample.restdemo.MyRESTDemo";
	
	private RESTService m_restService;
	private RESTClient m_restClient;
	
	public MyRESTDemo()
	{
		
	}
	
	public void activate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has started!");
		s_logger.debug(APP_ID + ": This is a debug message.");
		m_restClient = m_restService.newRESTClient(APP_ID);
		//testPOST();
		testQuery();
	}
	public void deactivate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has stopped!");
	}
	public void setRESTService(RESTService restService)
	{
		m_restService=restService;
	}
	public void unsetRESTService(RESTService restService)
	{
		m_restService=null;
	}
	public void testQuery()
	{
		m_restClient = m_restService.newRESTClient(APP_ID);
		KuraRequest m_request=new KuraRequest();
		//m_request.setProtocol("http");
		m_request.setHost("api.thingspeak.com");
		m_request.setPort(80);
		m_request.setPath("/update");
		//m_request.setMethod("GET");
		m_request.addQueryStringEntry("api_key","TC0VBHU8XESW76ZB");
		m_request.addQueryStringEntry("field1","20");
		m_request.addQueryStringEntry("field2","80");
		s_logger.info("RESTDemo::"+m_request.getBaseURL());
		m_restClient.execute(m_request);		
	}
	public void testPOST()
	{
		m_restClient = m_restService.newRESTClient(APP_ID);
		KuraRequest m_request=new KuraRequest();
		m_request.setProtocol("http");
		m_request.setHost("api.thingspeak.com");
		m_request.setPort(80);
		m_request.setPath("/update.json");
		m_request.setMethod("POST");
		m_request.setContentType("application/x-www-form-urlencoded");
		m_request.setRequestBody("api_key=TC0VBHU8XESW76ZB&field1=23&field2=74");
		m_request.addHeader("User-Agent","Mozilla/5.0");
		m_request.addHeader("Content-Type","application/x-www-form-urlencoded");	
		s_logger.info("RESTDemo::"+m_request.getBaseURL());
		s_logger.info("RESTDemo::"+m_request.getHeaders());
		m_restClient.execute(m_request);		
	}
}
