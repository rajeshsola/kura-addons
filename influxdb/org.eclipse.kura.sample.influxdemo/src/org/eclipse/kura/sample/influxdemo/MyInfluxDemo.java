package org.eclipse.kura.sample.influxdemo;

import org.eclipse.kura.tsdb.influxdb.InfluxDbClient;
import org.eclipse.kura.tsdb.influxdb.InfluxService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyInfluxDemo {
	
	private static final Logger s_logger = LoggerFactory.getLogger(MyInfluxDemo.class);

	private static final String APP_ID = "org.eclipse.kura.sample.MyInfluxDemo";
	
	private InfluxService m_influxService;
	private InfluxDbClient m_influxDbClient;

	
	protected void activate(ComponentContext componentContext) {

		s_logger.info("Bundle " + APP_ID + " has started!");

		s_logger.debug(APP_ID + ": This is a debug message.");
		m_influxDbClient = m_influxService.getNewInfluxDbClient("localhost", 8086);
		testInflux();
	}
	protected void deactivate(ComponentContext componentContext) {
		if(m_influxDbClient.isBatchEnabled())
			m_influxDbClient.disbaleBatch();
		s_logger.info("Bundle " + APP_ID + " has stopped!");
	}
	public void setInfluxService(InfluxService influxService)
	{
		m_influxService=influxService;
	}
	public void unsetInfluxService(InfluxService inlfuxService)
	{
		m_influxService=null;
	}
	public void testInflux()
	{
		if(m_influxDbClient.ping())
			s_logger.info("Pong from InfluxDb server");
		else
			s_logger.error("InfluxDb server not reachable");
		s_logger.info("InfluxDb version is:"+m_influxDbClient.getVersion());
		
		m_influxDbClient.createDatabase("kuradb");
		m_influxDbClient.setDatabaseName("kuradb");
		m_influxDbClient.setRetentionPolicy("autogen");
		
		if(!m_influxDbClient.isBatchEnabled())
			m_influxDbClient.enableBatch();
		
		m_influxDbClient.writeLine("weather", "temperature=25,humidity=72");
		m_influxDbClient.writeLine("weather", "temperature=25,humidity=72", "city=pune");
		
		m_influxDbClient.prepareLineProtocol("weather");
		m_influxDbClient.addFieldEntry("temperature", 28);
		m_influxDbClient.addFieldEntry("humidity", 70);
		m_influxDbClient.writeLine();
		
		m_influxDbClient.prepareLineProtocol("weather");
		m_influxDbClient.addFieldEntry("temperature", 18);
		m_influxDbClient.addFieldEntry("humidity", 75);
		m_influxDbClient.addTagEntry("city", "mumabi");
		m_influxDbClient.addTagEntry("sensor", "dht");
		m_influxDbClient.writeLine();
	}
}
