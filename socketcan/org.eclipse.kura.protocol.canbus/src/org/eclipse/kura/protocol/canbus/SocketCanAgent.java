package org.eclipse.kura.protocol.canbus;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.eclipse.kura.KuraException;
import org.slf4j.Logger;

import de.entropia.can.CanSocket;
import de.entropia.can.CanSocket.CanFrame;
import de.entropia.can.CanSocket.CanId;
import de.entropia.can.CanSocket.CanInterface;
import de.entropia.can.CanSocket.Mode;

public class SocketCanAgent {
	private static final Logger s_logger = LoggerFactory.getLogger(SocketCanAgent.class);
	CanSocket socket = null;
	CanInterface canif=null;
	public SocketCanAgent(String ifName,boolean loopback)throws KuraException, IOException 
	{
		try {
            		socket = new CanSocket(Mode.RAW);
            		socket.setLoopbackMode(true);
            		canif = new CanInterface(socket, ifName);
		            socket.bind(canif);
            		// s_logger.debug("message sent on " + ifName);
        	} catch (IOException e) {
            		s_logger.error("Error on CanSocket binding: {}", e.getMessage());
            		throw e;
        	} 
	}
	public void sendCanMessage(int canId,byte[] message) throws KuraException, IOException
	{
		try {
			socket.send(new CanFrame(canif, new CanId(canId), message));
		}catch(IOException e) {
			s_logger.error("Error on CanSocket in sendCanMessage: {}", e.getMessage());
            		throw e;
		}
		
	}
	public CanMessage recvCanMessage()throws KuraException, IOException
	{
		try {
			CanFrame cf = socket.recv();
            		CanId ci = cf.getCanId();
            		// s_logger.debug(cf.toString());
            		CanMessage cm = new CanMessage();
            		cm.setCanId(ci.getCanId_SFF());
            		cm.setData(cf.getData());     
            		return cm;
		}catch(IOException e) {
			s_logger.error("Error on CanSocket in receiveCanMessage: {}", e.getMessage());
            		throw e;
		}
		
	}
	
}
