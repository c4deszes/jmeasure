package org.jmeasure.lxi.socket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmeasure.lxi.factory.ISCPISocketFactory;

/**
 * This factory class is capable of creating Raw TCP sockets for SCPI communication
 * 
 * <p>
 * It supports the VISA resource string syntax for raw tcp sockets.
 * <p>	
 * Format: <i>TCPIP[board]::[host address]::[port]::SOCKET</i>
 */
public class RawSCPISocketFactory implements ISCPISocketFactory {

	private Pattern pattern = Pattern.compile("TCPIP(?<board>[0-9]*)::(?<host>.*)::(?<port>[0-9]{1,5})::SOCKET");

	@Override
	public boolean supports(String connectionInfo) {
		return pattern.matcher(connectionInfo).matches();
	}

	@Override
	public RawSCPISocket create(String connectionInfo) {
		Matcher matcher = pattern.matcher(connectionInfo);
		if(matcher.find()) {
			String host = matcher.group("host");
			int port = Integer.parseInt(matcher.group("port"));
			return new RawSCPISocket(host, port);
		}
		throw new IllegalArgumentException();
	}

	
}