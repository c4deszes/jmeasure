package org.jmeasure.core.lxi.vxi11;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.factory.ISocketFactory;

/**
 * This factory class is capable of creating VXI-11 sockets for SCPI communication
 * 
 * <p>
 * It supports the VISA resource string syntax for vxi-11 sockets.
 * <p>	
 * Format: <i>TCPIP[board]::[host address]::[port]::SOCKET</i>
 */
public class VXI11SocketFactory implements ISocketFactory {

    private Pattern pattern = Pattern.compile("TCPIP(?<board>[0-9]*)::(?<host>.*)::(?<instrument>[A-Za-z]{1})::INSTR");

	@Override
	public boolean supports(String connectionInfo) {
		return pattern.matcher(connectionInfo).matches();
	}

	@Override
	public VXI11Socket create(String connectionInfo) {
		Matcher matcher = pattern.matcher(connectionInfo);
		if(matcher.matches()) {
			String host = matcher.group("host");
            int port = Integer.parseInt(matcher.group("port"));
            int board = Integer.parseInt(matcher.group("board"));
            
            //return new VXI11Socket(host, name)
		}
		throw new IllegalArgumentException(connectionInfo + " doesn't match " + pattern.pattern());
	}

    
}