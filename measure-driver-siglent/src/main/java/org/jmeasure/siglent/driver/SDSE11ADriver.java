package org.jmeasure.siglent.driver;

import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPISocketAdapter;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.siglent.info.SiglentOscilloscopeInfo;

public class SDSE11ADriver extends SCPISocketAdapter {

	public SDSE11ADriver(ISCPISocket adapter, DeviceIdentifier deviceIdentifier, SiglentOscilloscopeInfo scopeInfo) {
		super(adapter, deviceIdentifier);
		// TODO Auto-generated constructor stub
	}
	
}