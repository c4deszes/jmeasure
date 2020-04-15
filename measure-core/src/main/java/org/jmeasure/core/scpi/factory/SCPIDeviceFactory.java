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
public class SCPIDeviceFactory implements ISCPIDeviceFactory {

	@Getter
	@Setter
	private long timeout = 1000;

	private final List<ISCPIDeviceFactory> factories;

	public SCPIDeviceFactory(ISCPIDeviceFactory... factories) {
		this(Arrays.asList(factories));
	}

	public SCPIDeviceFactory(List<ISCPIDeviceFactory> factories) {
		this.factories = new LinkedList<>(factories);
	}

	@Override
	public boolean supports(DeviceIdentifier info) {
		//We return true, because worst case this factory returns the default ISCPISocket implementation @see SCPISocketAdapter
		return true;
	}

	@Override
	public ISCPISocket create(ISCPISocket socket) throws IOException {
		socket.send(SCPI.idnQuery);
		Optional<String> response = socket.receive(timeout);
		response.orElseThrow(() -> new IOException("Device didn't respond"));
		DeviceIdentifier deviceIdentifier = DeviceIdentifier.from(response.get());

		for (ISCPIDeviceFactory factory : factories) {
			try {
				if (factory.supports(deviceIdentifier)) {
					return factory.create(socket);
				}
			} catch (UnsupportedDeviceException e) {
				log.warn("Failed to instantiate " + deviceIdentifier + " using " + factory, e);
			}
		}
		return new SCPISocketAdapter(socket, deviceIdentifier);
	}

}