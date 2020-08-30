package org.jmeasure.siglent.hardware;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jmeasure.core.instrument.InstrumentException;
import org.jmeasure.core.lxi.vxi11.VXI11Socket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.socket.RawSCPISocket;
import org.jmeasure.core.util.Units;
import org.jmeasure.siglent.driver.SDSLegacyDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * These tests are used to test the Siglent SDS1000XE driver
 * 
 * <p>The test suite is disabled in the buildscript because it 
 * needs the actual oscilloscope hardware
 * 
 * <p>For these tests the following hardware connections are needed
 * 
 * <p>Channel 1 connected to the calibration output in 1X mode
 * 
 * <p>On 2 channel variants Channel 2 connected to the calibration output in 10X mode
 * 
 * <p>On 4 channel variants Channel 3 connected to the calibration output in 10X mode
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SDSLegacyDriverHardwareTest {

	private final static String IP = "192.168.1.2";

	@Test
	public void testVdiv() throws UnknownHostException, IOException {
		try (ISCPISocket socket = new RawSCPISocket(new VXI11Socket(InetAddress.getByName(IP)))) {
			//socket.send(SCPICommand.builder().command("C1", "CPL").with("A1M").build());
		}
	}

	@Test
	public void testScopeDriver() throws IOException, InstrumentException {
		try(ISCPISocket socket = new RawSCPISocket(new VXI11Socket(InetAddress.getByName(IP)))) {
			//SDS1000XE scope = new SDS1000XE(socket, socket.getDeviceIdentifier());
			//scope.setAnalogInputEnabled(1, true);
			//scope.setAnalogInputEnabled(2, false);
			//scope.setAnalogInputEnabled(3, false);
			//scope.setAnalogInputEnabled(4, false);

			//scope.setTimebase(Units.milli(14), 0);
			//scope.setAnalogInputScale(1, 3.1);
			//scope.setAnalogInputOffset(1, -1.5);
			//scope.setAnalogInputAttenuation(1, 10);
		}
	}
}