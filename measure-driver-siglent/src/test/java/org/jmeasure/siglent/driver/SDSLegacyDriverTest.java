package org.jmeasure.siglent.driver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jmeasure.core.scpi.mock.TestSCPISocket;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.siglent.driver.SDSLegacyDriver;
import org.jmeasure.siglent.factory.SiglentDeviceFactory;
import org.jmeasure.siglent.info.SiglentOscilloscopeInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class SDSLegacyDriverTest {

	@Test
	public void testInstanceSDS1202XE() throws IOException {
		DeviceIdentifier identifier = DeviceIdentifier.from(SiglentDeviceFactory.SIGLENT_MANUFACTURER_STRING, "SDS1202X-E", "ABC123", "1.0.0");
		SiglentOscilloscopeInfo info = SiglentOscilloscopeInfo.create(identifier.getModel());
		SDSLegacyDriver scope = new SDSLegacyDriver(new TestSCPISocket(identifier), identifier, info);

		assertEquals(scope.getAnalogInputs().size(), 2);
	}

	@Test
	public void testInstanceSDS1104XE() throws IOException {
		DeviceIdentifier identifier = DeviceIdentifier.from(SiglentDeviceFactory.SIGLENT_MANUFACTURER_STRING, "SDS1104X-E", "ABC123", "1.0.0");
		SiglentOscilloscopeInfo info = SiglentOscilloscopeInfo.create(identifier.getModel());
		SDSLegacyDriver scope = new SDSLegacyDriver(new TestSCPISocket(identifier), identifier, info);

		assertEquals(scope.getAnalogInputs().size(), 4);
	}

	@Test
	public void testInstanceSDS1204XE() throws IOException {
		DeviceIdentifier identifier = DeviceIdentifier.from(SiglentDeviceFactory.SIGLENT_MANUFACTURER_STRING, "SDS1204X-E", "ABC123", "1.0.0");
		SiglentOscilloscopeInfo info = SiglentOscilloscopeInfo.create(identifier.getModel());
		SDSLegacyDriver scope = new SDSLegacyDriver(new TestSCPISocket(identifier), identifier, info);
		
		assertEquals(scope.getAnalogInputs().size(), 4);
	}
}