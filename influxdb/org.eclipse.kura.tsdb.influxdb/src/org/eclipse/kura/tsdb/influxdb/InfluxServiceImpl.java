package org.eclipse.kura.tsdb.influxdb;

import java.io.UnsupportedEncodingException;

import java.util.HashMap;
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

public class InfluxServiceImpl implements InfluxService
{
	private static final Logger s_logger = LoggerFactory.getLogger(InfluxServiceImpl.class);
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
		return new InfluxDbClientImpl(host,port);
	}
}

class InfluxDbClientImpl implements InfluxDbClient {
	
	private static final Logger s_logger = LoggerFactory.getLogger(InfluxServiceImpl.class);
	private static final String APP_ID = "org.eclipse.kura.tsdb.influxdb.InfluxDbClientImpl";

	private InfluxDB influxDB;
	private String dbName;
	private String rpName;
	
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
	public void setLogLevel() {
		//TODO:with params
		this.influxDB.setLogLevel(LogLevel.NONE);
	}
	@Override
	public void setConsistenctLevel(String level) {
		//TODO:with params
		this.influxDB.setConsistency(ConsistencyLevel.ONE);
		
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
	@Override
	public boolean isBatchEnabled() {		
		return influxDB.isBatchEnabled();
	}
	@Override
	public void setDatabaseName(String db) {
		//influxDB.setDatabase(db);
		dbName=db;
	}
	@Override
	public void setRetentionPolicy(String rp) {
		//influxDB.setRetentionPolicy(rp);
		rpName=rp;
	}
	@Override
	public void writeLine(String measurement,String fields) {
		//long now=System.currentTimeMillis();
		//TimeUnit unit=TimeUnit.MILLISECONDS;
		String linestr = measurement + " " + fields;
		influxDB.write(dbName, rpName,InfluxDB.ConsistencyLevel.ONE,linestr);
	}
	@Override
	public void writeLine(String measurement, String fields, String tags) {
		String linestr = measurement + "," + tags + " " + fields;
		influxDB.write(dbName, rpName,InfluxDB.ConsistencyLevel.ONE,linestr);
		
	}
	@Override
	public void createDatabase(String db) {
		influxDB.createDatabase(db);
		
	}
	@Override
	public void dropDatabase() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void prepareLineProtocol(String measurement) {
		tagset.clear();
		fieldset.clear();
		this.measurement=measurement; 	
	}
	@Override
	public void addFieldEntry(String name, Object value) {
		fieldset.put(name, value);		
	}
	@Override
	public void addTagEntry(String name, String value) {
		tagset.put(name, value);		
	}
	/*
	@Override
	public void preparePoint() {
		tagset.clear();
		fieldset.clear();
	}
	@Override
	public void writePoint(String measurement)
	{
		Point.Builder point = Point.measurement(measurement);
		for (Map.Entry<?,?> entry : tagset.entrySet()) {
			point.tag(entry.getKey(),entry.getValue());
		}
		for (Map.Entry<?,?> entry : fieldset.entrySet()) {
			point.add
			//point.add
			//point.addField(entry.getKey().toString(),entry.getValue());
		}
	}*/
	@Override
	public void writeLine() {
		StringBuilder tags = new StringBuilder();
		for (Map.Entry<?,?> entry : tagset.entrySet()) {
			if(tags.length() >0 )
			{
				tags.append(",");
			}
			tags.append(String.format("%s=%s",entry.getKey().toString(),entry.getValue().toString()));
		}
		StringBuilder fields = new StringBuilder();
		for (Map.Entry<?,?> entry : fieldset.entrySet()) {
			if(fields.length() >0 )
			{
				fields.append(",");
			}
			fields.append(String.format("%s=%s",entry.getKey().toString(),entry.getValue().toString()));
		}
		
		String linestr;
		if(!tagset.isEmpty())
			 linestr =measurement +","+tags.toString() +" "+fields.toString();
		else
			linestr = measurement + " " + fields.toString();
		influxDB.write(dbName, rpName,InfluxDB.ConsistencyLevel.ONE,linestr);
	}
}
