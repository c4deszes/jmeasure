package org.jmeasure.lxi.socket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jmeasure.lxi.SCPISocketFactory.ISCPISocketFactory;

/**
 * RawSocketFactory
 */
public class RawSCPISocketFactory implements ISCPISocketFactory<RawSCPISocket> {

	private Pattern pattern = Pattern.compile("TCPIP0::(?<host>.*)::(?<port>[0-9]{1,5})::SOCKET");

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