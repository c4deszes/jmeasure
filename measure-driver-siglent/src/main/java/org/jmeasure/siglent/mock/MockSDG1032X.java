package org.jmeasure.siglent.mock;

import org.jmeasure.lxi.DeviceIdentifier;

public class MockSDG1032X extends MockSDG1000X {
	public MockSDG1032X() {
		super(DeviceIdentifier.from("Siglent Technologies","SDG1032X","SDG1XCAD2RABC123","1.01.01.33R1"));
	}
}