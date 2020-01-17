package org.jmeasure.cli.scpi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPIDeviceFactory;
import org.jmeasure.lxi.SCPISocket;
import org.jmeasure.lxi.SCPISocketFactory;
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
public class SCPIService implements ValueProvider, Converter<String, SCPIDevice> {

	@Autowired
	private SCPIDeviceFactory deviceFactory;

	@Autowired
	private SCPISocketFactory socketFactory;

	private Map<String, SCPIDevice> devices = new HashMap<>();

	public SCPIDevice connect(String path) throws Exception {
		SCPISocket socket = socketFactory.create(path);
		
		return this.connect(socket);
	}

	public SCPIDevice connect(SCPISocket socket) throws Exception {
		SCPIDevice device = deviceFactory.connect(socket);
		devices.put(device.getDeviceIdentifier().getSerialNumber(), device);
	
		return device;
	}

	public SCPIDevice createAlias(SCPIDevice device, String alias) {
		if(device == null) {
			return null;
		}
		devices.put(alias, device);
		return device;
	}

	public List<SCPIDevice> listDevices() {
		return devices.entrySet()
				.stream()
				.sorted((a, b) -> Integer.compare(a.getValue().hashCode(), b.getValue().hashCode()))
				.map(a -> a.getValue())
				.collect(Collectors.toList());
	}

	public boolean disconnect(SCPIDevice device) {
		if(device != null) {
			device.disconnect();
			devices.entrySet().removeIf(entry -> entry.getValue() == device);
			return true;
		}
		return false;
	}

	@Override
	public boolean supports(MethodParameter parameter, CompletionContext completionContext) {
		if(parameter.getParameterType().isAssignableFrom(SCPIDevice.class)) {
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
	public SCPIDevice convert(String source) {
		return devices.get(source);
	}

}