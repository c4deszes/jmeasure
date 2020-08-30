package org.jmeasure.core.scpi.mock;

import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.mock.MockSocket;

public class TestSCPISocket extends MockSCPISocket {

	public TestSCPISocket(DeviceIdentifier idn) {
		super(new MockSocket("TestSocket", "inst0"), idn);
	}

	@Override
	protected void onReset() {
		
	}

	@Override
	protected void onNotMapped(SCPICommand command) {
		
	}
}