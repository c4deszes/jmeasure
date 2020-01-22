package org.jmeasure.siglent;

import java.io.IOException;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;

/**
 * SDS1000XE
 */
public class SDS1000XE extends SCPIDevice {

	public SDS1000XE(SCPISocket socket, DeviceIdentifier deviceIdentifier) throws IOException {
		super(socket, deviceIdentifier);
	}

	
}