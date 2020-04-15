package org.jmeasure.core.serial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jmeasure.core.visa.factory.ISocketFactory;
import org.jmeasure.core.visa.factory.InstrumentNameProvider;

/**
 * SerialSocketFactory
 */
public class SerialSocketFactory implements ISocketFactory {

	private Pattern pattern = Pattern.compile("ASRL(?<port>[0-9]*)::INSTR");

	@Override
	public boolean supports(String connectionInfo) {
		return pattern.matcher(connectionInfo).matches();
	}

	@Override
	public SerialSocket create(String connectionInfo, InstrumentNameProvider nameProvider) {
		Matcher matcher = pattern.matcher(connectionInfo);
		if(matcher.matches()) {
			int port = Integer.parseInt(matcher.group("port"));
			//TODO: serial configuration
			return new SerialSocket(getPortPath(port), "");
		}
		throw new IllegalArgumentException(connectionInfo + " doesn't match " + pattern.pattern());
	}

	public String getPortPath(int port) {
		String os = System.getProperty("os.name");
		if(os.startsWith("Windows")) {
			return "COM" + port;
		} else if(os.contains("nix") || os.contains("nux")) {
			return "/dev/ttyS" + port;
		}
		return null;
	}

    
}