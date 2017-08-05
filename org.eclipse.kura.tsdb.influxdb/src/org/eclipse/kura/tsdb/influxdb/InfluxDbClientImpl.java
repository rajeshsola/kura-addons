package org.eclipse.kura.tsdb.influxdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.dto.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxDbClientImpl implements InfluxDbClient {

	private static final Logger s_logger = LoggerFactory.getLogger(InfluxServiceFactory.class);
	private static final String APP_ID = "org.eclipse.kura.tsdb.influxdb.InfluxDbClientImpl";

	private InfluxDB influxDB;
	private String dbName;
	private String rpName;
	InfluxDB.LogLevel logLevel;
	InfluxDB.ConsistencyLevel consistencyLevel;
	
	Map<String,Object> fieldset = new HashMap<String,Object>(); //TODO:other types
	Map<String,String> tagset = new HashMap<String,String>();
	String measurement;
	
	public InfluxDbClientImpl()
	{
		//TODO: initialize with custom ip address,port based on kura configuration
		this.influxDB = InfluxDBFactory.connect("http://localhost:8086");
	}
	public InfluxDbClientImpl(String host,int port)
	{
		this.influxDB = InfluxDBFactory.connect("http://"+host+":"+port);
	}
	@Override
	public boolean ping() {
		Pong result = this.influxDB.ping();
		if(result==null || result.getVersion().equals("unknown"))
			return false;
		return true;	
	}
	@Override
	public String getVersion() {
		String version = this.influxDB.version();
		return version;
	}
	@Override
	public void enableBatch() {
		//TODO: no.of points, flush duration via configuration
		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);		
	}
	@Override
	public void disbaleBatch() {
		influxDB.disableBatch();
	}
	private boolean isBatchEnabled() {		
		return influxDB.isBatchEnabled();
	}
	@Override
	public void enableCompression() {
		influxDB.enableGzip();
		
	}
	@Override
	public void disableCompression() {
		influxDB.disableGzip();
		
	}
	
	private boolean isCompressionEnabled() {
		return  influxDB.isGzipEnabled();
	}
	@Override
	public void setLogLevel() 
	{
		//TODO:with params,ignored due kura logging services
		logLevel=InfluxDB.LogLevel.NONE;
		this.influxDB.setLogLevel(logLevel);
		
	}
	@Override
	public void setConsistenctLevel(String level) {
		//TODO:with params
		this.consistencyLevel=ConsistencyLevel.valueOf(level);
		this.influxDB.setConsistency(this.consistencyLevel);		
	}
	@Override
	public void setDatabaseName(String db) {
		//influxDB.setDatabase(db);
		if(!influxDB.databaseExists(db))
			influxDB.createDatabase(db);
		dbName=db;
	}
	@Override
	public void setRetentionPolicy(String rp) {
		//influxDB.setRetentionPolicy(rp);
		rpName=rp;
	}
	@Override
	public void createDatabase(String db) {
		if(!influxDB.databaseExists(db))
			influxDB.createDatabase(db);
		else
			s_logger.error("Database:"+db+" already exists!!");
		
	}
	@Override
	public void dropDatabase(String db) {
		if(influxDB.databaseExists(db))
			influxDB.deleteDatabase(db);
		else
			s_logger.error("Database:"+db+" doesn't exists!!");
		
	}
	@Override
	public void writeLine(String measurement,String fields) {
		//long now=System.currentTimeMillis();
		//TimeUnit unit=TimeUnit.MILLISECONDS;
		String linestr = measurement + " " + fields;
		influxDB.write(dbName, rpName,InfluxDB.ConsistencyLevel.ONE,linestr);
		//influxDB.write(linestr);
	}
	@Override
	public void writeLine(String measurement, String fields, String tags) {
		String linestr = measurement + "," + tags + " " + fields;
		influxDB.write(dbName, rpName,InfluxDB.ConsistencyLevel.ONE,linestr);
		//influxDB.write(linestr);
	}
	@Override
	public void writeLine(String linestr)
	{
		//influxDB.write(dbName, rpName,InfluxDB.ConsistencyLevel.ONE,linestr);
		influxDB.write(linestr);
	}
	@Override
	public void writeLines(List<String> lines)
	{
		//TODO
	}
	@Override
	public void writePoint(KuraPoint point) {
		influxDB.write(this.dbName, this.rpName,InfluxDB.ConsistencyLevel.ONE,point.toString());
		//influxDB.write(point.toString());
	}
	@Override
	public void writePoint(String dbName,String rpName,String level,KuraPoint point) {
		//TODO:utilize consistency level param
		influxDB.write(dbName, rpName,InfluxDB.ConsistencyLevel.valueOf(level),point.toString());
	}
	@Override
	public void writePoints(List<KuraPoint> points)
	{
		//TODO
	}
}