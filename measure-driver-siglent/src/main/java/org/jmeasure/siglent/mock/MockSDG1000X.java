package org.jmeasure.siglent.mock;

import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.mock.MockSCPISocket;
import org.jmeasure.core.scpi.mock.OnCommand;
import org.jmeasure.core.util.Units;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * MockWaveformGenerator
 */
public class MockSDG1000X extends MockSCPISocket {

	private float freq;

	public MockSDG1000X(DeviceIdentifier deviceIdentifier) {
		super(deviceIdentifier);

		this.onReset();
	}

	@OnCommand("C1:BSWV\\?")
	public SCPICommand basicWaveQuery() {
		return SCPICommand.builder()
							.command("C1:BSWV")
							.with("FREQ", freq)
							.build();
	}

	@OnCommand("C1:BSWV")
	public void setBasicWave(SCPICommand command) {
		this.freq = command.getFloat("FREQ").get();
	}

	@Override
	public void onReset() {
		freq = Units.kilo(1);
	}

	@Override
	public void onNotMapped(SCPICommand command) {
		
	}
	
}