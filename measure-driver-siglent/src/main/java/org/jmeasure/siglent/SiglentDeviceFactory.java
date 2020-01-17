package org.jmeasure.siglent;

import java.io.IOException;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;
import org.jmeasure.lxi.UnsupportedDeviceException;
import org.jmeasure.lxi.SCPIDeviceFactory.ISCPIDeviceFactory;
import org.jmeasure.siglent.SDG1000X;

/**
 * SiglentDeviceFactory
 */
public class SiglentDeviceFactory implements ISCPIDeviceFactory {

	@Override
	public boolean supports(DeviceIdentifier info) {
		if (info.getModel().equals("SDG1032X")) {
			return true;
		}
		return false;
	}

	@Override
	public SCPIDevice create(DeviceIdentifier info, SCPISocket socket) throws UnsupportedDeviceException {
		try {
			return new SDG1000X(info, socket);
		} catch(IOException e) {
			throw new UnsupportedDeviceException(e);
		}
	}
	
}