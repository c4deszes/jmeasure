package org.jmeasure.lxi;

import static org.junit.Assert.assertEquals;

import org.jmeasure.lxi.socket.RawSCPISocket;
import org.jmeasure.lxi.socket.RawSCPISocketFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * RawSocketTest
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class RawSocketTest {

	@Test
	public void testConnect() throws Exception {
		//RawSCPISocket socket = new RawSCPISocket("localhost", 5025);
		//socket.send(SCPICommand.builder().query("*IDN").build());
		//Thread.sleep(1000000);
		//socket.close();
	}

	@Test
	public void testFactoryCreate() {
		RawSCPISocketFactory factory = new RawSCPISocketFactory();
		RawSCPISocket socket = factory.create("TCPIP0::192.168.1.1::5025::SOCKET");
		assertEquals("192.168.1.1", socket.getHost());
		assertEquals(5025, socket.getPort());
	}
	
}