package org.jmeasure.siglent.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.mock.MockSCPISocket;
import org.jmeasure.core.scpi.mock.TestSCPISocket;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.UnsupportedDeviceException;
import org.jmeasure.core.visa.VisaException;
import org.jmeasure.core.visa.mock.MockSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class SiglentFactoryTest {

	private SiglentDeviceFactory factory = new SiglentDeviceFactory();

	@Test
	public void testFactorySupportsDevice() {
		TestSCPISocket scpiSocket = new TestSCPISocket(
				DeviceIdentifier.from(
					SiglentDeviceFactory.SIGLENT_MANUFACTURER_STRING,
					"ABC123",
					"ABC123",
					"1.0.0"));

		assertTrue(factory.supports(scpiSocket));
	}

	@Test
	public void testFactoryUnsupportedDevice() {
		TestSCPISocket scpiSocket = new TestSCPISocket(
				DeviceIdentifier.from(
					"Other Manufacturer", 
					"ABC123",
					"ABC123",
					"1.0.0"));

		assertFalse(factory.supports(scpiSocket));
	}

	@Test(expected = UnsupportedDeviceException.class)
	public void testFactoryCreateUnsupportedDevice() throws VisaException {
		TestSCPISocket scpiSocket = new TestSCPISocket(
				DeviceIdentifier.from(
					SiglentDeviceFactory.SIGLENT_MANUFACTURER_STRING,
					"ABC123",
					"ABC123",
					"1.0.0"));
	
		factory.create(scpiSocket);
	}

	@Test(expected = UnsupportedDeviceException.class)
	public void testFactoryCreateSupportedDevice() throws VisaException {
		TestSCPISocket scpiSocket = new TestSCPISocket(
				DeviceIdentifier.from(
					SiglentDeviceFactory.SIGLENT_MANUFACTURER_STRING,
					"SDS1104XE",
					"ABC123",
					"1.0.0"));
	
		factory.create(scpiSocket);
	}
}