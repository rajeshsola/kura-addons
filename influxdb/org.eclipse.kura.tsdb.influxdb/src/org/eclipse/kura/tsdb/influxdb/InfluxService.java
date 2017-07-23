package org.eclipse.kura.tsdb.influxdb;

import org.influxdb.dto.Pong;

public interface InfluxService {
	public InfluxDbClient getNewInfluxDbClient(String host,int port);
		
}
	
