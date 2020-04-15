package org.jmeasure.core.lxi.raw;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.assertj.core.util.Arrays;
import org.jmeasure.core.visa.UnsupportedSocketException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * RawSocketTest
 */
@RunWith(Parameterized.class)
public class RawSocketFactoryTest extends RawSocketBase {

	@Parameters
	public static Collection<?> data() {
		return Arrays.asList(new Object[][] { { "TCPIP::localhost::50000::SOCKET", "localhost", 50000, 0, true },
				{ "TCPIP0::localhost::50000::SOCKET", "localhost", 50000, 0, true },
				{ "TCPIP12::localhost::50000::SOCKET", "localhost", 50000, 12, true },

				{ "TCPIPA::localhost::50000::SOCKET", "localhost", 50000, 0, false },
				{ "TCPIP0::localhost::5000000::SOCKET", "localhost", 50000, 0, false },
				{ "TCPIP0::localhost::50000::ABC", "localhost", 50000, 0, false },
				{ "TCPIP0::localhost::50000::", "localhost", 50000, 0, false },
				{ "TCPIP0::localhost::50000", "localhost", 50000, 0, false } });
	}

	private String input;

	private String host;

	private int port;

	private int board;

	private boolean support;

	public RawSocketFactoryTest(String input, String host, int port, int board, boolean support) {
		super(port);
		this.input = input;
		this.host = host;
		this.port = port;
		this.board = board;
		this.support = support;
	}

	@Test
	public void test() throws IOException, UnsupportedSocketException {
		RawSocketFactory factory = new RawSocketFactory();
		boolean factorySupports = factory.supports(input);
		assertEquals(support, factory.supports(input));
		if(!factorySupports) {
			return;
		}

		RawSocket socket = factory.create(input, name -> name);
		assertEquals(host, socket.getHost());
		assertEquals(port, socket.getPort());
		assertEquals(board, socket.getBoard());
	}
	
}