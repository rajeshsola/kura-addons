package org.eclipse.kura.tsdb.influxdb;

import java.util.List;

public interface InfluxDbClient {
	public boolean ping();
	public String getVersion();
	public void setLogLevel();
	public void setConsistenctLevel(String level);
	
	public void enableCompression();
	public void disableCompression();
			
	public void enableBatch();
	public void disbaleBatch();
		
	public void setDatabaseName(String db);
	public void setRetentionPolicy(String rp);
	public void createDatabase(String db);
	public void dropDatabase(String db);
	
	public void writeLine(String measurement,String fields,String tags);
	public void writeLine(String measurement,String fields);
	public void writeLine(String line);
	public void writeLines(List<String> lines);
	/*
	* public void writeLine(String dbName,String rpName,String level,String measurement,String fields,String tags);
	* public void writeLine(String dbName,String rpName,String level,String measurement,String fields);
	* public void writeLine(String dbName,String rpName,String level,String line);
	*/
	
	
	public void writePoint(String dbName,String rpName,String level,KuraPoint point);
	public void writePoint(KuraPoint point);
	public void writePoints(List<KuraPoint> points);
	
	//TODO:query
	//public void query();	
}
