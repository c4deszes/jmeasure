package org.jmeasure.core.scpi.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPI;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.SCPISocketAdapter;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.UnsupportedDeviceException;
import org.jmeasure.core.visa.factory.SocketFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * DeviceFactory
 */
@Slf4j
public class SCPIDeviceFactory {

	@Getter
	@Setter
	private long timeout = 1000;

	private final SCPISocketFactory scpiFactory;

	private final List<ISCPIDeviceFactory> factories;

	public SCPIDeviceFactory(final SCPISocketFactory scpiFactory, ISCPIDeviceFactory... factories) {
		this(scpiFactory, Arrays.asList(factories));
	}

	public SCPIDeviceFactory(final SCPISocketFactory scpiFactory, List<ISCPIDeviceFactory> factories) {
		this.scpiFactory = scpiFactory;
		this.factories = new LinkedList<>(factories);
	}

	public ISCPISocket create(ISocket socket) throws IOException {
		return this.create(scpiFactory.create(socket));
	}

	//@SuppressWarnings("resource")
	public ISCPISocket create(ISCPISocket socket) throws IOException {
		socket.connect();
		socket.send(SCPI.idnQuery);
		Optional<SCPICommand> response = socket.receive(timeout);
		response.orElseThrow(() -> new IOException("Device didn't respond"));
		DeviceIdentifier deviceIdentifier = DeviceIdentifier.from(response.get().getRaw());

		for (ISCPIDeviceFactory factory : factories) {
			try {
				if (factory.supports(deviceIdentifier)) {
					return factory.create(socket, deviceIdentifier);
				}
			} catch (UnsupportedDeviceException e) {
				log.warn("Failed to instantiate " + deviceIdentifier + " using " + factory, e);
			}
		}
		return new SCPISocketAdapter(socket, deviceIdentifier);
	}
}