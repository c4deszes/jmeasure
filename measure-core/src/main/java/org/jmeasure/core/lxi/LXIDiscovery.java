package org.jmeasure.core.lxi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Objects;
import java.util.Set;

import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * LXIDiscovery is the interface for discovering LXI devices on the network
 */
public interface LXIDiscovery {

    /**
     * Returns LXI devices found through the given network interface
     * 
     * @param networkInterface Network Interface
     * @return Set of Instrument endpoints that can be used to connect to the devices
     * @throws IOException If there was an error during device discovery
     */
    Set<? extends InstrumentEndpoint> discover(NetworkInterface networkInterface) throws IOException;
	
	/**
	 * 
	 */
	public abstract static class InstrumentEndpoint {

		private InetAddress host;

		private int port;

		private DeviceIdentifier deviceIdentifier;

		public InstrumentEndpoint(InetAddress host, int port, DeviceIdentifier deviceIdentifier) {
			this.host = host;
			this.port = port;
			this.deviceIdentifier = deviceIdentifier;
		}

		public InetAddress getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public DeviceIdentifier getDeviceIdentifier() {
			return deviceIdentifier;
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof InstrumentEndpoint)) {
				return false;
			}
			InstrumentEndpoint endpoint = (InstrumentEndpoint) obj;
			return host.equals(endpoint.host) && port == endpoint.port;
		}

		@Override
		public int hashCode() {
			return Objects.hash(host, port);
		}
		
	}
}