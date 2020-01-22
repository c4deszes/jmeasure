package org.jmeasure.siglent;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;
import org.jmeasure.lxi.factory.ISCPIDeviceFactory;
import org.jmeasure.lxi.factory.UnsupportedDeviceException;
import org.jmeasure.siglent.SDG1000X;

/**
 * SiglentDeviceFactory
 */
public class SiglentDeviceFactory implements ISCPIDeviceFactory {

	@Override
	public boolean supports(DeviceIdentifier info) {
		return info.getManufacturer().equals("Siglent Technologies") && this.lookupModel(info.getModel()) != null;
	}

	@Override
	public SCPIDevice create(SCPISocket socket, DeviceIdentifier info) throws UnsupportedDeviceException {
		try {
			Class<? extends SCPIDevice> klass = lookupModel(info.getModel());
			return klass.getConstructor(SCPISocket.class, DeviceIdentifier.class).newInstance(socket, info);
		} catch(Exception e) {
			throw new UnsupportedDeviceException(e);
		}
	}
	
	public Class<? extends SCPIDevice> lookupModel(String model) {
		if(model.matches("SDG10[36]{1}2X")) {
			return SDG1000X.class;
		}
		return null;
	}
}