package org.jmeasure.siglent;

import java.io.IOException;

import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.factory.ISCPIDeviceFactory;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.UnsupportedDeviceException;
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
	public ISCPISocket create(ISCPISocket socket) throws UnsupportedDeviceException, IOException {
		try {
			String model = socket.getDeviceIdentifier().getModel();
			Class<? extends ISCPISocket> klass = lookupModel(model);
			return klass.getConstructor(ISCPISocket.class, DeviceIdentifier.class).newInstance(socket, socket.getDeviceIdentifier());
		} catch(Exception e) {
			//TODO: clean
			if(e.getCause() instanceof IOException) {
				throw (IOException) e.getCause();
			}
			throw new UnsupportedDeviceException(e);
		}
	}
	
	public Class<? extends ISCPISocket> lookupModel(String model) {
		if(model.matches("SDG10[36]{1}2X")) {
			return SDG1000X.class;
		}
		else if(model.matches("SDS1[12]{1}4X-E")) {
			return SDS1000XE.class;
		}
		return null;
	}
}