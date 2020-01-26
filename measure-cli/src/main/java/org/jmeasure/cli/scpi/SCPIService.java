package org.jmeasure.cli.scpi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.factory.SCPIDeviceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

/**
 * SCPIService
 */
@Component
public class SCPIService implements ValueProvider, Converter<String, ISCPISocket> {

	@Autowired
	private SCPIDeviceFactory deviceFactory;

	private Map<String, ISCPISocket> devices = new HashMap<>();

	public ISCPISocket connect(String path) throws IOException {
		return register(deviceFactory.create(path));
	}

	public ISCPISocket connect(ISocket socket) throws IOException {
		return register(deviceFactory.create(socket));
	}

	public ISCPISocket connect(ISCPISocket socket) throws IOException {
		return register(deviceFactory.create(socket));
	}

	private ISCPISocket register(ISCPISocket dev) throws IOException {
		devices.put(dev.getDeviceIdentifier().getSerialNumber(), dev);
		return dev;
	}

	public List<ISCPISocket> listDevices() {
		return devices.entrySet()
				.stream()
				.sorted((a, b) -> Integer.compare(a.getValue().hashCode(), b.getValue().hashCode()))
				.map(a -> a.getValue())
				.collect(Collectors.toList());
	}

	public boolean disconnect(ISCPISocket device) {
		if(device != null) {
			device.disconnect();
			devices.entrySet().removeIf(entry -> entry.getValue() == device);
			return true;
		}
		return false;
	}

	@Override
	public boolean supports(MethodParameter parameter, CompletionContext completionContext) {
		if(parameter.getParameterType().isAssignableFrom(ISCPISocket.class)) {
			return true;
		}
		return false;
	}

	@Override
	public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext,
			String[] hints) {
		return devices.keySet()
					.stream()
					.filter(id -> id.startsWith(completionContext.currentWordUpToCursor()))
					.map(CompletionProposal::new)
					.collect(Collectors.toList());
	}

	@Override
	public ISCPISocket convert(String source) {
		return devices.get(source);
	}

}