package org.jmeasure.core.visa.instrument;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.VISAResourceManager;

public class DiscoverableInstrument extends Instrument {

	private DeviceIdentifier deviceIdentifier;

	public DiscoverableInstrument(String id, String displayName, DeviceIdentifier deviceIdentifier) {
		super(id, displayName);
		this.deviceIdentifier = deviceIdentifier;
	}

	@Override
	public ISocket connect(VISAResourceManager visaResourceManager) {
		// TODO Auto-generated method stub
		return null;
	}
 }