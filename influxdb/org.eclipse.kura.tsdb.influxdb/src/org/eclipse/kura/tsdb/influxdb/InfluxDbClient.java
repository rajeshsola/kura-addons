package org.eclipse.kura.tsdb.influxdb;

public interface InfluxDbClient {
	public boolean ping();
	public String getVersion();
	public void setLogLevel();
	public void setConsistenctLevel(String level);
	//public void enableCompression();
	//public void disableCompression();
	//public boolean isCompressionEnabled();
	
	public void enableBatch();
	public void disbaleBatch();
	public boolean isBatchEnabled();
	
	public void setDatabaseName(String db);
	public void setRetentionPolicy(String rp);
	public void prepareLineProtocol(String measurement); //initialize new point

	public void addFieldEntry(String name,Object value);
	public void addTagEntry(String name,String value);
	
	public void writeLine();
	public void writeLine(String measurement,String fields,String tags);
	public void writeLine(String measurement,String fields);
	
	//TODO: write methods with Point objects, batch mode
	//public void preparePoint(); //initialize new point
	//public void writePoint(String measurement);
		
	public void query();
	public void createDatabase(String db);
	public void dropDatabase();
}
