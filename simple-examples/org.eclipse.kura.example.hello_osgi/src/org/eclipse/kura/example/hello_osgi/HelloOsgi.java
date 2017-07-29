package org.eclipse.kura.example.hello_osgi;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloOsgi implements ConfigurableComponent {
	
	private static final Logger s_logger = LoggerFactory.getLogger(HelloOsgi.class);
	private static final String APP_ID = "org.eclipse.kura.example.hello_osgi.HelloOsgi";
    private Map<String, Object> m_properties;
    
    private static final String HELLO_PROP_TOPIC = "hello.topic";
    private static final String HELLO_PROP_PERIOD = "hello.period";
    private static final String HELLO_PROP_MODE = "hello.mode";
    
    private String m_topic;
    private String m_mode;
    private int m_period;
    
    private final ScheduledExecutorService m_worker;
    private ScheduledFuture<?> m_handle;
    private final Random m_random;
    
    public HelloOsgi() {
    	this.m_random = new Random();
        this.m_worker = Executors.newSingleThreadScheduledExecutor();
    }
	protected void activate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has started!");
		s_logger.debug(APP_ID + ": This is a debug message.");
	}
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
        s_logger.info("Bundle " + APP_ID + " has started with config!");
        updated(properties);
    }
	public void updated(Map<String, Object> properties) {
		this.m_properties = properties;
        if(m_properties != null && !m_properties.isEmpty()) {
            Iterator<Entry<String, Object>> it = m_properties.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Object> entry = it.next();
                s_logger.info("New property - " + entry.getKey() + " = " +
                entry.getValue() + " of type " + entry.getValue().getClass().toString());
            }
        }
        doUpdate();
	}
	protected void deactivate(ComponentContext componentContext) {

		s_logger.info("Bundle " + APP_ID + " has stopped!");
		this.m_worker.shutdown();

	}
	public void doUpdate()
	{
		if (this.m_handle != null) {
            this.m_handle.cancel(true);
        }
		
		m_topic = (String) this.m_properties.get(HELLO_PROP_TOPIC);
		m_period = (int) this.m_properties.get(HELLO_PROP_PERIOD);
		m_mode = (String) this.m_properties.get(HELLO_PROP_MODE);
		s_logger.info("HelloOsgi::topic:"+m_topic);
		s_logger.info("HelloOsgi::period:"+m_period);
		s_logger.info("HelloOsgi::mode:"+m_mode);
		
		this.m_handle = this.m_worker.scheduleAtFixedRate(new Runnable() {
			int k=0;
			@Override
			public void run() {
                Thread.currentThread().setName(getClass().getSimpleName());
                s_logger.info("HelloOsgi counter:"+k++ +",value::"+m_random.nextInt()%100);
			}
        }, 0, m_period, TimeUnit.SECONDS);

	}
	

}
