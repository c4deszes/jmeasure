package org.jmeasure.siglent.factory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Position;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.socket.RawSCPISocket;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.UnsupportedDeviceException;
import org.jmeasure.core.visa.UnsupportedSocketException;
import org.jmeasure.core.visa.VisaDeviceFactory;
//import org.jmeasure.siglent.SDG1000X;
import org.jmeasure.core.visa.VisaException;
import org.jmeasure.siglent.driver.SDSE11ADriver;
import org.jmeasure.siglent.info.SiglentOscilloscopeInfo;


public class SiglentScopeFactory {

	public boolean supports(ISCPISocket scpiSocket) {
		try {
			return scpiSocket.getDeviceIdentifier()
							.getManufacturer()
							.equals(SIGLENT_MANUFACTURER_STRING);
		} catch(IOException e) {
			return false;
		}
	}

	public ISCPISocket create(ISCPISocket scpiSocket) throws VisaException {
		try {
			DeviceIdentifier deviceIdentifier = scpiSocket.getDeviceIdentifier();
			String model = deviceIdentifier.getModel();
			SiglentOscilloscopeInfo scopeInfo = SiglentOscilloscopeInfo.create(model);
			//TODO: verify IDN response of the SDS2000X Plus series, the postfix may be P or Plus
			if(	scopeInfo.getSeries() == 5 || 
				scopeInfo.getSeries() == 6 || 
				(scopeInfo.getSeries() == 2 && scopeInfo.getPostfix().equals("X+"))) {
				
				return new SDSE11ADriver(scpiSocket, deviceIdentifier, scopeInfo);
			}
			return new SDSLegacyDriver(scpiSocket, deviceIdentifier, scopeInfo);
		} catch(IOException e) {
			throw new VisaException(e);
		}
	}
}