package org.jmeasure.core.lxi.vxi11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.UnsupportedSocketException;
import org.jmeasure.core.visa.factory.ISocketFactory;
import org.jmeasure.core.visa.factory.InstrumentNameProvider;

/**
 * This factory class is capable of creating VXI-11 sockets for SCPI
 * communication
 * 
 * <p>
 * It supports the VISA resource string syntax for VXI-11 sockets.
 * <p>
 * Format: <i>TCPIP[board]::[host address][::Instrument Name][::INSTR]</i>
 */
public class VXI11SocketFactory implements ISocketFactory {

	private Pattern pattern = Pattern.compile("TCPIP(?<board>[0-9]*)::(?<host>[^:]{1,})(::?<instrument>[^:]{1,})?(::INSTR)?");

	@Override
	public boolean supports(String resourceString) {
		return pattern.matcher(resourceString).matches();
	}

	@Override
	public VXI11Socket create(String resourceString, InstrumentNameProvider nameProvider) throws IOException, UnsupportedSocketException {
		Matcher matcher = pattern.matcher(resourceString);
		if(matcher.matches()) {
			int board = Integer.parseInt(matcher.group("board"));
			String host = matcher.group("host");
			String instrument = matcher.group("instrument");

			//TODO: add factory default configuration
            return new VXI11Socket(InetAddress.getByName(host), nameProvider.apply(instrument));
		}
		throw new UnsupportedSocketException();
	}

    
}