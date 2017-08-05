package org.eclipse.kura.configurable.influx;

import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.tsdb.influxdb.InfluxService;
import org.eclipse.kura.tsdb.influxdb.InfluxDbClient;
import org.eclipse.kura.tsdb.influxdb.KuraPoint;
import org.osgi.service.component.ComponentContext;
import org.slf4j.LoggerFactory;

public class ConfigurableInflux implements ConfigurableComponent {

	private static Logger s_logger = LoggerFactory.getLogger(ConfigurableInflux.class);
	
	private static final String APP_ID="org.eclipse.kura.configurable.influx";
	
	private InfluxService m_influxService;
	private InfluxDbClient m_influxDbClient;
	
	private String HostAddress, HostUsername, HostPassword, HostDbName, HostMeasurementName, HostRetentionPolicy;
	private Boolean InfluxEnabled;
	private int HostPort;
	
	private Map<String, Object> properties;
	
	protected void activate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has started!");
	}
	
    protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
        s_logger.info("Bundle " + APP_ID + " has started with config!");
        updated(properties);
    }
    
    protected void deactivate(ComponentContext componentContext) {
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
    
    public void updated(Map<String, Object> properties) {
        this.properties = properties;
        if(properties != null && !properties.isEmpty()) {
            Iterator<Entry<String, Object>> it = properties.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Object> entry = it.next();
                s_logger.info("New property - " + entry.getKey() + " = " +
                entry.getValue() + " of type " + entry.getValue().getClass().toString());
                
                switch(entry.getKey())
                {
                case "param.username":
                	HostUsername = (String) entry.getValue();
                	s_logger.info(HostUsername);
                	break;
                case "param.password":
                	HostPassword = (String) entry.getValue();
                	s_logger.info(HostPassword);
                	break;
                case "param.hostAddr":
                	HostAddress=(String) entry.getValue();
                	s_logger.info(HostAddress);
                	break;
                case "param.port":
                	HostPort=(Integer) entry.getValue();
                	s_logger.info(""+HostPort);
                	break;
                case "param.dbName":
                	HostDbName=(String) entry.getValue();
                	s_logger.info(HostDbName);
                	break;
                case "param.measurementName":
                	HostMeasurementName=(String) entry.getValue();
                	s_logger.info(HostMeasurementName);
                	break;
                case "param.Enable":
                	InfluxEnabled = (Boolean) entry.getValue();
                	s_logger.debug(""+InfluxEnabled);
                	break;
                case "param.retentionPolicy":
                	HostRetentionPolicy=(String) entry.getValue();
                	s_logger.debug(HostRetentionPolicy);
                	break;
                default:
                	s_logger.error("Wrong Property");
                }
            }
         //Checking if Influx Client is enabled or not.
            if(InfluxEnabled == true)
            {
            	m_influxDbClient = m_influxService.getInfluxDbClient(HostAddress, HostPort);
            	s_logger.info("Client Created");
            	postData();
            }
              
        }
    }
    
	public void postData()
	{
		if(m_influxDbClient.ping())
			s_logger.info("Pong from InfluxDb server");
		else
			s_logger.error("InfluxDb server not reachable");
		s_logger.info("InfluxDb version is:"+m_influxDbClient.getVersion());
		
		//m_influxDbClient.createDatabase("kuradb");
		m_influxDbClient.setDatabaseName(HostDbName);
		m_influxDbClient.setRetentionPolicy("autogen");
		
//		m_influxDbClient.enableBatch();


		KuraPoint kurapoint=new KuraPoint();
		kurapoint.setMeasurement(HostMeasurementName);
		kurapoint.addFieldEntry("temperature", 18);
		kurapoint.addFieldEntry("humidity", 75);
		kurapoint.addTagEntry("city", "mumabi");
		kurapoint.addTagEntry("sensor", "dht");
		s_logger.info("KuraPoint::"+kurapoint.toString());
		m_influxDbClient.writePoint(kurapoint);
	}
    
	
}
