package org.jmeasure.lxi.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPI;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * DeviceFactory
 */
@Slf4j
public class CompositeSCPIDeviceFactory {

	@Getter
	@Setter
	private long timeout = 1000;

	private List<ISCPIDeviceFactory> factories;

	public CompositeSCPIDeviceFactory(long timeout, ISCPIDeviceFactory... factories) {
		this(timeout, Arrays.asList(factories));
	}

	public CompositeSCPIDeviceFactory(long timeout, List<ISCPIDeviceFactory> factories) {
		this.timeout = timeout;
		this.factories = new LinkedList<>(factories);
	}

	@SuppressWarnings("resource")
	public SCPIDevice connect(SCPISocket socket) throws UnsupportedDeviceException {
		try {
			SCPIDevice device = new SCPIDevice(socket);
			SCPICommand response = device.query(SCPI.idnQuery, timeout).get();
			DeviceIdentifier deviceIdentifier = DeviceIdentifier.from(response.getRaw());

			for (ISCPIDeviceFactory factory : factories) {
				try {
					if (factory.supports(deviceIdentifier)) {
						return factory.create(socket, deviceIdentifier);
					}
				} catch (UnsupportedDeviceException e) {
					log.warn("Failed to instantiate ", e);
				}
			}
			return new SCPIDevice(socket, deviceIdentifier);
		} catch (IOException | NoSuchElementException e) {
			throw new UnsupportedDeviceException("Device didn't respond..");
		}
	}
}