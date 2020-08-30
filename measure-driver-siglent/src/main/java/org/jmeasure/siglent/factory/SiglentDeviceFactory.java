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
import org.jmeasure.core.visa.VisaException;
import org.jmeasure.siglent.info.SiglentOscilloscopeInfo;

/**
 * SiglentDeviceFactory
 */
public class SiglentDeviceFactory implements VisaDeviceFactory<ISCPISocket> {

	public final static String SIGLENT_MANUFACTURER_STRING = "Siglent Technologies";

	@Override
	public boolean supports(ISocket socket) {
		return this.supports(new RawSCPISocket(socket));
	}

	public boolean supports(ISCPISocket scpiSocket) {
		try {
			return scpiSocket.getDeviceIdentifier()
							.getManufacturer()
							.equals(SIGLENT_MANUFACTURER_STRING);
		} catch(IOException e) {
			return false;
		}
	}

	@Override
	public ISCPISocket create(ISocket socket) throws VisaException {
		return this.create(new RawSCPISocket(socket));
	}

	public ISCPISocket create(ISCPISocket scpiSocket) throws VisaException {
		try {
			
		} catch(IOException e) {
			throw new VisaException(e);
		}
	}
}