package org.eclipse.kura.tsdb.influxdb;

import org.influxdb.dto.Pong;

public interface InfluxService {
	public InfluxDbClient getInfluxDbClient(String host,int port);
		
}
	
