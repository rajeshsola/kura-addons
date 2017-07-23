package org.eclipse.kura.sample.influxdemo;

import org.eclipse.kura.tsdb.influxdb.InfluxDbClient;
import org.eclipse.kura.tsdb.influxdb.InfluxService;
import org.eclipse.kura.tsdb.influxdb.KuraPoint;
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
		m_influxDbClient = m_influxService.getInfluxDbClient("localhost", 8086);
		testInflux();
	}
	protected void deactivate(ComponentContext componentContext) {
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
		
		//m_influxDbClient.createDatabase("kuradb");
		m_influxDbClient.setDatabaseName("kuradb");
		m_influxDbClient.setRetentionPolicy("autogen");
		
		m_influxDbClient.enableBatch();
		m_influxDbClient.writeLine("weather", "temperature=25,humidity=72");
		m_influxDbClient.writeLine("weather", "temperature=25,humidity=72", "city=pune");
		
		KuraPoint p1=new KuraPoint();
		p1.setMeasurement("weather");
		p1.addFieldEntry("temperature", 28);
		p1.addFieldEntry("humidity", 70);
		s_logger.info("KuraPoint::"+p1.toString());
		m_influxDbClient.writePoint(p1);
		
		KuraPoint p2=new KuraPoint();
		p2.setMeasurement("weather");
		p2.addFieldEntry("temperature", 18);
		p2.addFieldEntry("humidity", 75);
		p2.addTagEntry("city", "mumabi");
		p2.addTagEntry("sensor", "dht");
		s_logger.info("KuraPoint::"+p2.toString());
		m_influxDbClient.writePoint(p2);
	}
}
