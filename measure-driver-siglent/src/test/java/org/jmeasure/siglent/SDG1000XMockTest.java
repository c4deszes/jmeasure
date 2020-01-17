package org.jmeasure.siglent;

import static org.junit.Assert.assertEquals;

import org.jmeasure.core.util.Util;
import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPI;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.siglent.mock.MockSDG1000X;
import org.jmeasure.siglent.SDG1000X;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * MockTest
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SDG1000XMockTest {

	static DeviceIdentifier deviceIdentifier = DeviceIdentifier.from("Siglent Technologies,SDG1032X,SDG1XCAD2RABC123,1.01.01.33R1");

	@Test
	public void testIdnQuery() throws Exception {
		try(SDG1000X wavegen = new SDG1000X(deviceIdentifier, new MockSDG1000X(deviceIdentifier))) {
			SCPICommand response = wavegen.query(SCPI.idnQuery, 1000);
			DeviceIdentifier deviceIdentifier = DeviceIdentifier.from(response.getRaw());
			assertEquals("SDG1032X", deviceIdentifier.getModel());
		}
	}

	@Test
	public void testSetBasicWave() throws Exception {
		try(SDG1000X wavegen = new SDG1000X(deviceIdentifier, new MockSDG1000X(deviceIdentifier))) {
			wavegen.send(SCPICommand.builder().command("C1:BSWV").with("FREQ", (float)Util.mega(10)).build());
			SCPICommand bswv = wavegen.query(SCPICommand.builder().query("C1:BSWV").build(), 1000);
			assertEquals((float)Util.mega(10), bswv.getFloat("FREQ"), 0.01);
		}
	}
	
}