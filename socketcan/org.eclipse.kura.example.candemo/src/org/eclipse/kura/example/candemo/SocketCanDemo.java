package org.eclipse.kura.example.candemo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.protocol.canbus.SocketCanService;
import org.eclipse.kura.protocol.canbus.SocketCanAgent;
import org.eclipse.kura.protocol.canbus.CanMessage;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketCanDemo implements ConfigurableComponent {
	private static final Logger s_logger = LoggerFactory.getLogger(SocketCanDemo.class);

	private static final String APP_ID = "org.eclipse.kura.example.candemo";
	private Map<String, Object> m_properties;
	private SocketCanService m_socketCanService;
	
    private String m_ifName;
    private int m_canId;
    
    private ScheduledExecutorService m_snd_worker;
    private ScheduledExecutorService m_rcv_worker;
    private ScheduledFuture<?> m_sender;
    private ScheduledFuture<?> m_receiver;
    private final Random m_random;
    private  SocketCanAgent m_socketCanAgent;
    
    public SocketCanDemo()
    {
    	this.m_random = new Random();
        this.m_snd_worker = Executors.newSingleThreadScheduledExecutor();
        this.m_rcv_worker = Executors.newSingleThreadScheduledExecutor();
    }
    public void setSocketCanService(SocketCanService socketCanservice)
	{
		m_socketCanService=socketCanservice;
	}
	public void unsetSocketCanService(SocketCanService socketCanservice)
	{
		m_socketCanService=socketCanservice;
	}
	/*protected void activate(ComponentContext componentContext) {

		s_logger.info("Bundle " + APP_ID + " has started!");
		s_logger.debug(APP_ID + ": This is a debug message.");
		this.m_ifName = "vcan0";
		this.m_canId=0;

	}*/
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
	        s_logger.info("Bundle " + APP_ID + " has started with config!");
	        updated(properties);
	    }

	protected void deactivate(ComponentContext componentContext) {
		s_logger.info("Bundle " + APP_ID + " has stopped!");
		this.m_snd_worker.shutdown();
		this.m_rcv_worker.shutdown();
	}
	public void updated(Map<String, Object> properties) {
	        this.m_properties = properties;
	        if(properties != null && !properties.isEmpty()) {
	            Iterator<Entry<String, Object>> it = properties.entrySet().iterator();
	            while (it.hasNext()) {
	                Entry<String, Object> entry = it.next();
	                s_logger.info("New property - " + entry.getKey() + " = " +
	                entry.getValue() + " of type " + entry.getValue().getClass().toString());
	            }
	        }
	        this.m_ifName = "vcan0";
			this.m_canId=0;
            if (this.m_properties.get("can.channel") != null) {
	                this.m_ifName = (String) this.m_properties.get("can.channel");
	            }
			try {
				m_socketCanAgent=m_socketCanService.getSocketCanAgent(m_ifName);
			} catch (KuraException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			if (this.m_sender != null) {
	            this.m_sender.cancel(true);
	        }
			if (this.m_receiver != null) {
	            this.m_receiver.cancel(true);
	        }
			this.m_sender = this.m_snd_worker.scheduleAtFixedRate(new Runnable() {
	            @Override
	            public void run() {
	                Thread.currentThread().setName(getClass().getSimpleName());
	                doCanPublish();
	            }
	       	}, 0, 500, TimeUnit.MILLISECONDS);
			this.m_receiver = this.m_rcv_worker.schedule(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						while(true) {
							CanMessage cm=m_socketCanAgent.recvCanMessage();
							s_logger.info("got a message,id="+cm.getCanId()+",payload="+cm.getData());
						}
					} catch (KuraException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}				
			}, 0, TimeUnit.MILLISECONDS);
   	}	
	void doCanPublish()
	{
		int canid=m_random.nextInt(4096);
		int dlc=m_random.nextInt(8);
		byte payload[]=new byte[dlc];
		m_random.nextBytes(payload);
		//s_logger.info("sending can frame");
		try {
			m_socketCanAgent.sendCanMessage(canid,payload);
		} catch (KuraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
