package org.eclipse.kura.tsdb.influxdb;

import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.moshi.Moshi.Builder;

public class InfluxServiceFactory implements InfluxService
{
	private static final Logger s_logger = LoggerFactory.getLogger(InfluxServiceFactory.class);
	private static final String APP_ID = "org.eclipse.kura.tsdb.influxdb.InfluxServiceImpl";

	@SuppressWarnings("unused")
	private ComponentContext m_ctx;

	
	public void activate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has started!");
		s_logger.debug(APP_ID + ": This is a debug message.");		
	}
	public void deactivate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has stopped!");
	}

	public InfluxDbClient getInfluxDbClient(String host,int port)
	{
		//TODO: host,port from config
		return new InfluxDbClientImpl(host,port);
	}
}