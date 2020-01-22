package org.jmeasure.siglent.mock;

import org.jmeasure.core.Units;
import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.mock.MockSCPISocket;
import org.jmeasure.lxi.mock.OnCommand;

/**
 * MockWaveformGenerator
 */
public class MockSDG1000X extends MockSCPISocket {

	private float freq = (float) Units.kilo(1);

	public MockSDG1000X(DeviceIdentifier deviceIdentifier) {
		super(deviceIdentifier);
	}

	@OnCommand("C1:BSWV\\?")
	public SCPICommand basicWaveQuery() {
		return SCPICommand.builder()
							.command("C1:BSVW")
							.with("FREQ", freq)
							.build();
	}

	@OnCommand("C1:BSWV")
	public void setBasicWave(SCPICommand command) {
		this.freq = command.getFloat("FREQ").get();
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotMapped(SCPICommand command) {
		// TODO Auto-generated method stub

	}
	
}