package org.jmeasure.core.visa.instrument;

import java.io.IOException;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.VISAResourceManager;
import org.jmeasure.core.visa.VisaException;

public class ConnectableInstrument extends Instrument {

	private String socket;

	public ConnectableInstrument(String id, String displayName, String socket) {
		super(id, displayName);
		this.socket = socket;
	}

	@Override
	public ISocket connect(VISAResourceManager visaResourceManager) throws IOException, VisaException {
		return visaResourceManager.connect(socket);
	}
}