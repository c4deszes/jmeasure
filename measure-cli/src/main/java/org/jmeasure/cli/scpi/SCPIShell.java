package org.jmeasure.cli.scpi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.List;

import org.jmeasure.core.lxi.raw.RawSocket;
import org.jmeasure.core.lxi.vxi11.VXI11Socket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.SCPIService;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.VISAResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ValueProvider;

/**
 * 
 * Available commands: scpi connect -p <url> [-a <alias>]
 */
@ShellComponent
@ShellCommandGroup("SCPI")
public class SCPIShell implements ValueProvider, Converter<String, ISCPISocket> {

	@Autowired
	private SCPIService scpiService;

	@Autowired
	private VISAResourceManager visa;

	@ShellMethod(key="scpi-send", value="Sends ")
	public String scpiSend(
		@ShellOption(value = {"-n", "--name"}) ISCPISocket device,
		@ShellOption(value = {"-c", "--command"}) String command,
		@ShellOption(value = {"--no-response"}) boolean noResponse,
		@ShellOption(value = {"-d", "--daemon"}, defaultValue = "false") boolean daemon,
		@ShellOption(value = {"-t", "--timeout"}, defaultValue = "1000") long timeout) {

		try {
			device.send(new SCPICommand(command));
			if(!daemon && !noResponse) {
				return device.receive(timeout).orElse("");
			}
			else if(!noResponse) {
				Thread task = new Thread(() -> {
					try {
						synchronized(device) {
							device.receive(timeout);
						}
					} catch (IOException e) {}
				});
				task.start();
			}
			return "";
		} catch(IOException e) {
			return "Failed to send/receive command, reason: " + e.getMessage();
		}
	}

	@ShellMethod(key={"scpi-info"}, value="Prints out device information")
	public String scpiInfo(
		@ShellOption(value = {"-n", "--name"}) ISCPISocket device) {

		try {
			StringBuilder out = new StringBuilder();
			DeviceIdentifier idn = device.getDeviceIdentifier();
			out.append("Manufacturer: ");
			out.append(idn.getManufacturer());
			out.append('\n');

			out.append("Model: ");
			out.append(idn.getModel());
			out.append('\n');

			out.append("Serial number: ");
			out.append(idn.getSerialNumber());
			out.append('\n');

			out.append("Firmware version: ");
			out.append(idn.getFirmwareVersion());
			out.append('\n');

			out.append("Resource string: ");
			out.append(device.getResourceString());
			out.append('\n');

			out.append("Capabilities: ");
			for(Class<?> klass : device.getClass().getInterfaces()) {
				out.append(klass.getSimpleName());
				out.append(" ");
			}

			return out.toString();
		} catch(IOException e) {
			return "Failed to get device information, reason: " + e.getMessage();
		}
	}

	@Override
	public ISCPISocket convert(String source) {
		return visa.get(source, ISCPISocket.class);
	}

	@Override
	public boolean supports(MethodParameter parameter, CompletionContext completionContext) {
		if(ISCPISocket.class.isAssignableFrom(parameter.getParameterType())) {
			return true;
		}
		return false;
	}

	@Override
	public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
		visa.getAll(ISCPISocket.class).stream().filter()
	}
	
}