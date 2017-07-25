package org.eclipse.kura.cloud.rest;


import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTServiceFactory implements RESTService {
	
	private static final Logger s_logger = LoggerFactory.getLogger(RESTServiceFactory.class);
	private static final String APP_ID = "org.eclipse.kura.cloud.rest.RESTService";

	@SuppressWarnings("unused")
	private ComponentContext m_ctx;

	
	public void activate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has started!");
		s_logger.debug(APP_ID + ": This is a debug message.");		
	}
	public void deactivate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has stopped!");
	}
	@Override
	public RESTClient newRESTClient(String id) {
		//TODO: using RESTClientImpl, using okhttp APIs directly
		return new RESTClientImpl();
	}
}
