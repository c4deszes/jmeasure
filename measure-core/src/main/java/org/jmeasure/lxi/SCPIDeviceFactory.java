package org.jmeasure.lxi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPI;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;

import lombok.Getter;
import lombok.Setter;

/**
 * DeviceFactory
 */
public class SCPIDeviceFactory {

	@Getter
	@Setter
	private long timeout = 1000;

	private List<ISCPIDeviceFactory> factories;

	public SCPIDeviceFactory(long timeout, ISCPIDeviceFactory... factories) {
		this.timeout = timeout;
		this.factories = new LinkedList<>(Arrays.asList(factories));
	}

	public void add(ISCPIDeviceFactory factory) {
		this.factories.add(factory);
	}

	public void remove(ISCPIDeviceFactory factory) {
		this.factories.remove(factory);
	}

	@SuppressWarnings("resource")
	public SCPIDevice connect(SCPISocket socket) throws UnsupportedDeviceException {
		try {
			SCPIDevice device = new SCPIDevice(null, socket);
			SCPICommand response = device.query(SCPI.idnQuery, timeout);
			DeviceIdentifier deviceIdentifier = DeviceIdentifier.from(response.getRaw());

			for (ISCPIDeviceFactory factory : factories) {
				try {
					if (factory.supports(deviceIdentifier)) {
						return factory.create(deviceIdentifier, socket);
					}
				} catch (Exception e) {
					throw new UnsupportedDeviceException(
							"Failed to instantiate " + deviceIdentifier + " using " + factory.getClass().getName());
				}
			}
			throw new UnsupportedDeviceException("No driver found for " + deviceIdentifier);
		} catch (Exception e) {
			throw new UnsupportedDeviceException("Device didn't respond..");
		}
	}

	/**
	 * InstrumentFactory
	 */
	public static interface ISCPIDeviceFactory {

		/**
		 * Returns true if the factory can create an instance of the given device
		 * 
		 * This method shouldn't try to create the given instance
		 * 
		 * @param info SCPI Device information
		 * @return true if the factory is able to create a device
		 */
		public boolean supports(DeviceIdentifier info);

		/**
		 * Returns an instance of the given device and attaches the SCPI socket to that instance
		 * 
		 * **Note: it's not a requirement to have this method return without an exception if support returned true**
		 * Also this method shouldn't check if the device is supported
		 * 
		 * @param <T> Returned instance should be cast to this type
		 * @param info SCPI Device information
		 * @param socket SCPI Socket
		 * @return 
		 */
		public SCPIDevice create(DeviceIdentifier info, SCPISocket socket) throws UnsupportedDeviceException;
		
	}
}