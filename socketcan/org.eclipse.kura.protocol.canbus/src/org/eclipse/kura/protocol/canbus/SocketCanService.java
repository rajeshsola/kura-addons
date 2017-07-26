package org.eclipse.kura.protocol.canbus;

import java.io.IOException;

import org.eclipse.kura.KuraException;

public interface SocketCanService {
	public SocketCanAgent getSocketCanAgent(String ifName) throws KuraException, IOException;
}
