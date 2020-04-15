package org.jmeasure.core.lxi.raw;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmeasure.core.visa.UnsupportedSocketException;
import org.jmeasure.core.visa.factory.ISocketFactory;
import org.jmeasure.core.visa.factory.InstrumentNameProvider;

/**
 * This factory class is capable of creating Raw TCP sockets for SCPI
 * communication
 * 
 * <p>
 * It supports the VISA resource string syntax for raw tcp sockets.
 * <p>
 * Format: <i>TCPIP[board]::[host address]::[port]::SOCKET</i>
 */
public class RawSocketFactory implements ISocketFactory {

	private Pattern pattern = Pattern.compile("TCPIP(?<board>[0-9]*)::(?<host>.{1,})::(?<port>[0-9]{1,5})::SOCKET");

	@Override
	public boolean supports(String connectionInfo) {
		return pattern.matcher(connectionInfo).matches();
	}

	@Override
	public RawSocket create(String resourceString, InstrumentNameProvider nameProvider) throws IOException, UnsupportedSocketException {
		Matcher matcher = pattern.matcher(resourceString);
		if(matcher.matches()) {
			String host = matcher.group("host");
			int port = Integer.parseInt(matcher.group("port"));
			int board = 0;
			if(!matcher.group("board").trim().isEmpty()) {
				board = Integer.parseInt(matcher.group("board"));
			}
			return new RawSocket(host, port, board);
		}
		throw new UnsupportedSocketException();
	}
	
}