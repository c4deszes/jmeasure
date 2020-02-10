package org.jmeasure.siglent.mock;

import org.apache.commons.lang3.RandomStringUtils;
import org.jmeasure.core.scpi.mock.MockSCPIClass;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.mock.MockSocket;

@MockSCPIClass("Siglent-SDG1032X")
public class MockSDG1032X extends MockSDG1000X {
	public MockSDG1032X(MockSocket socket) {
		super(socket, DeviceIdentifier.from("Siglent Technologies","SDG1032X","SDG1X" + RandomStringUtils.randomAlphanumeric(11).toUpperCase(), "1.01.01.33R1"));
	}
}