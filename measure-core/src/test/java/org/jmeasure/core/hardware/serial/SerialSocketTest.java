package org.jmeasure.core.hardware.serial;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.serial.SerialSocket;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openmuc.jrxtx.DataBits;
import org.openmuc.jrxtx.FlowControl;
import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.SerialPortBuilder;
import org.openmuc.jrxtx.StopBits;


/**
 * Hardware tests of Serial sockets
 * 
 * 
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SerialSocketTest {

	@BeforeClass
	public static void init() {
		//System.setProperty("java.library.path", ".\\native\\rxtx\\x64_windows");
	}

	@Test
	public void testSerialSocketDiscovery() {
		for(String port : SerialPortBuilder.getSerialPortNames()) {
			System.out.println(port);
		}
	}

	@Test
	public void testSerialSocketConstructor() throws IOException, TimeoutException {
		System.err.println(System.getProperty("java.library.path"));
		try(SerialSocket socket = new SerialSocket("COM3", 9600, DataBits.DATABITS_8, Parity.NONE, StopBits.STOPBITS_1, FlowControl.NONE)) {
			socket.send(ByteBuffer.wrap(new byte[]{1, 2, 3, 4}));
			//ByteBuffer response = socket.receive(0, 1000);
			//assertEquals(response.position(), 4);
			//assertEquals(new byte[]{1, 2, 3, 4}, response.array());
		}
	}
	
	@Test
	public void testSerialSocketCreateViaStringParams() throws IOException {
		try(SerialSocket socket = SerialSocket.create("COM3", "9600/8/N/1")) {
			socket.send(ByteBuffer.wrap(new byte[]{1, 2, 3, 4}));
			//ByteBuffer response = socket.receive(0, 1000);
			//assertEquals(response.position(), 4);
			//assertEquals(new byte[]{1, 2, 3, 4}, response.array());
		}
	}
}