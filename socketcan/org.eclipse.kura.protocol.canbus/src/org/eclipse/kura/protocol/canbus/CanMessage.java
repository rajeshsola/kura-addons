package org.eclipse.kura.protocol.canbus;

import de.entropia.can.CanSocket.CanId;
import de.entropia.can.CanSocket.CanInterface;

public class CanMessage {
	private CanInterface m_iface;
	private int m_canId;
	private byte[] m_data;
    
	public byte[] getData() {
        return this.m_data;
    }

    public void setData(byte[] data) {
        this.m_data = data;
    }

    public int getCanId() {
        return this.m_canId;
    }

    public void setCanId(int canId) {
        this.m_canId = canId;
    }


}
