package org.jmeasure.core.lxi.raw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmeasure.core.visa.factory.ISocketFactory;

/**
 * This factory class is capable of creating Raw TCP sockets for SCPI communication
 * 
 * <p>
 * It supports the VISA resource string syntax for raw tcp sockets.
 * <p>	
 * Format: <i>TCPIP[board]::[host address]::[port]::SOCKET</i>
 */
public class RawSocketFactory implements ISocketFactory {

	private Pattern pattern = Pattern.compile("TCPIP(?<board>[0-9]*)::(?<host>.*)::(?<port>[0-9]{1,5})::SOCKET");

	@Override
	public boolean supports(String connectionInfo) {
		return pattern.matcher(connectionInfo).matches();
	}

	@Override
	public RawSocket create(String connectionInfo) {
		Matcher matcher = pattern.matcher(connectionInfo);
		if(matcher.matches()) {
			String host = matcher.group("host");
            int port = Integer.parseInt(matcher.group("port"));
            int board = Integer.parseInt(matcher.group("board"));
			return new RawSocket(host, port, board);
		}
		throw new IllegalArgumentException();
	}

	
}