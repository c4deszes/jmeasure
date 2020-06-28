package org.jmeasure.core.lxi.mdns;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * 
 */
public class MDNSTransponder implements Runnable {

	private String instrumentName;

	private Map<MDNSServiceType, MDNSServiceConfiguration> services;

	public MDNSTransponder(String instrumentName) {

	}

	public MDNSTransponder(String instrumentName, DeviceIdentifier deviceIdentifier) {

	}

	@Override
	public void run() {
		try(JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost())) {
			for(MDNSServiceType type : services.keySet()) {
				MDNSServiceConfiguration config = services.get(type);
				ServiceInfo serviceInfo = ServiceInfo.create(type.fqdn(), instrumentName, config.port, config.text);
				jmdns.registerService(serviceInfo);
			}
		}
		catch(IOException e) {
			
		}
	}

	public static class MDNSServiceConfiguration {
		private final int port;

		private final String text;

		public MDNSServiceConfiguration(int port, String text) {
			this.port = port;
			this.text = text;
		}
	}

	public static class LXIServiceConfiguration extends MDNSServiceConfiguration {

		/**
		 * The LXI mDNS service is expected to run on port 80, same as the HTTP service
		 * <p>
		 * Specified in rule 10.4.3 of the LXI standard
		 */
		private final static int LXI_SERVICE_PORT = 80;

		public LXIServiceConfiguration(DeviceIdentifier identifier) {
			super(LXI_SERVICE_PORT, convertToRecordKeys(identifier));
		}

		private static String convertToRecordKeys(DeviceIdentifier identifier) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Manufacturer=" + identifier.getManufacturer() + "\n");
			buffer.append("Model=" + identifier.getModel() + "\n");
			buffer.append("SerialNumber=" + identifier.getSerialNumber() + "\n");
			buffer.append("FirmwareVersion=" + identifier.getFirmwareVersion());
			return buffer.toString();
		}
	}
	
}