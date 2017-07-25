package org.eclipse.kura.cloud.rest;

import java.io.IOException;
import java.util.Map;

import org.eclipse.kura.KuraException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RESTClientImpl implements RESTClient
{
	private static final Logger s_logger = LoggerFactory.getLogger(RESTClientImpl.class);
	private final OkHttpClient client = new OkHttpClient();
	
	/*public RESTClientImpl(String id)
	{
		clientId=id;
	}*/
	@Override
	public void execute(KuraRequest krequest) {
		
		Request.Builder builder = new Request.Builder();
		builder.url(krequest.getBaseURL());
		for (Map.Entry<?,?> entry : krequest.getHeaders().entrySet())
		{
			builder.addHeader(entry.getKey().toString(), entry.getValue().toString());
		}
		if(krequest.getMethod().equals("POST"))
		{
			final MediaType mediaType = MediaType.parse(krequest.getContentType()); //TODO
			RequestBody body=RequestBody.create(mediaType, krequest.getBody());
			builder.post(body);
		}			
		Request okrequest= builder.build();
		try {
			Response okresponse=client.newCall(okrequest).execute();
			if(!okresponse.isSuccessful())
				throw new IOException("Kura Request Failed to execute");
			s_logger.info("Kura Reponse::"+okresponse.body());			
		} catch (IOException e) {
			s_logger.error("RESTService::failed to execute request");
		}
		//KuraResponse kresponse = new KuraResponse();
	}
	
}
