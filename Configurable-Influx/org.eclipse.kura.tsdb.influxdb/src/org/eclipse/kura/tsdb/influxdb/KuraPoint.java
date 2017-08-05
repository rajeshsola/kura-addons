package org.eclipse.kura.tsdb.influxdb;

import java.util.HashMap;
import java.util.Map;

public class KuraPoint {
	public String measurement;
	Map<String,Object> fieldset = new HashMap<String,Object>(); //TODO:other types
	Map<String,String> tagset = new HashMap<String,String>();
	public KuraPoint()
	{
		
	}
	public void setMeasurement(String measurement) {
		this.measurement=measurement;
	}
	public void addFieldEntry(String name,Object value)
	{
		fieldset.put(name, value);
	}
	public void addTagEntry(String name,String value)
	{
		tagset.put(name, value);
	}
	public void setTimeStamp()
	{
		//TODO
	}
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append(measurement);		
		for (Map.Entry<?,?> entry : tagset.entrySet()) 
		{
			sb.append(",");
			sb.append(String.format("%s=%s",entry.getKey().toString(),entry.getValue().toString()));
		}
		sb.append(" ");
		int k=0;
		for (Map.Entry<?,?> entry : fieldset.entrySet()) 
		{
			if(k++ >0 )
			{
				sb.append(",");
			}
			sb.append(String.format("%s=%s",entry.getKey().toString(),entry.getValue().toString()));
		}
		return sb.toString();	
	}
}
