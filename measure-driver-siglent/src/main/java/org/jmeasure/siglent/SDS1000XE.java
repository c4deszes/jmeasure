package org.jmeasure.siglent;

import java.io.IOException;

import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPISocketAdapter;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * SDS1000XE
 */
public class SDS1000XE extends SCPISocketAdapter {

	public SDS1000XE(ISCPISocket socket, DeviceIdentifier deviceIdentifier) throws IOException {
		super(socket);
	}

	
}