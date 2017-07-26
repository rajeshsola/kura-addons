package org.eclipse.kura.protocol.canbus;

import java.io.IOException;
import java.util.Map;

import org.eclipse.kura.KuraErrorCode;
import org.eclipse.kura.KuraException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.kura.protocol.canbus.SocketCanAgent;


import de.entropia.can.CanSocket;
import de.entropia.can.CanSocket.CanFrame;
import de.entropia.can.CanSocket.CanId;
import de.entropia.can.CanSocket.CanInterface;
import de.entropia.can.CanSocket.Mode;

public class SocketCanServiceImpl implements SocketCanService 
{
	private static final Logger s_logger = LoggerFactory.getLogger(SocketCanServiceImpl.class);
	private static final String APP_ID = "org.eclipse.kura.protocol.canbus";
	private ComponentContext m_ctx;
	
	protected void activate(ComponentContext componentContext) {
     		s_logger.info("activating CanConnectionService");
		m_ctx=componentContext;		
    	}

    	protected void deactivate(ComponentContext componentContext) {
    	
    	}
   	/*protected void updated(Map<String, Object> properties)
    	{
    	
    	}*/
    	public SocketCanAgent getSocketCanAgent(String ifName) throws KuraException, IOException
    	{
    		return new SocketCanAgent(ifName,true);
    	}  
    
}
