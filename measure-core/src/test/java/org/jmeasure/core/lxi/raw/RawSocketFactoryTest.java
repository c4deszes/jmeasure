package org.jmeasure.core.lxi.raw;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * RawSocketTest
 */
@RunWith(Parameterized.class)
public class RawSocketFactoryTest {

	@Parameters
    public static Collection<?> data() {
		return Arrays.asList(new Object[][] {
		   { "TCPIP::hostname.net::1234::SOCKET", "hostname.net", 1234, 0 },
		   { "TCPIP0::hostname.net::1234::SOCKET", "hostname.net", 1234, 0 },
		   { "TCPIP12::hostname.net::1234::SOCKET", "hostname.net", 1234, 12 }
		});
	 }

	private String input;

	private String host;

	private int port;

	private int board;

	public RawSocketFactoryTest(String input, String host, int port, int board) {
		this.input = input;
		this.host = host;
		this.port = port;
		this.board = board;
	}

	@Test
	public void test() {
		RawSocketFactory factory = new RawSocketFactory();
		assertEquals(true, factory.supports(input));

		RawSocket socket = factory.create(input);
		assertEquals(host, socket.getHost());
		assertEquals(port, socket.getPort());
		assertEquals(board, socket.getBoard());
	}
	
}