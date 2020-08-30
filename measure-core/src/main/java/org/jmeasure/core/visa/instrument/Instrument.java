package org.jmeasure.core.visa.instrument;

import java.io.IOException;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.VISAResourceManager;
import org.jmeasure.core.visa.VisaException;

public abstract class Instrument {
	private String id;

	private String displayName;

	public Instrument(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public abstract ISocket connect(VISAResourceManager visaResourceManager) throws IOException, VisaException;

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}
}