package org.jmeasure.cli.scpi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.jmeasure.core.lxi.raw.RawSocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * 
 * Available commands:
 * 	scpi connect -p <url> [-a <alias>]
 */
@ShellComponent
@ShellCommandGroup("SCPI")
public class SCPIShell {

	@Autowired
	private SCPIService scpi;

	@ShellMethod(key = "scpi connect", value = "Connects a SCPI device")
	public String scpiConnect(
			@ShellOption(value = { "-p", "--path" }) String url) {

		try {
			ISCPISocket dev = scpi.connect(url);
			return "Connected " + dev.getDeviceIdentifier().getSerialNumber() + " via " + url;
		} catch (Exception e) {
			return "Failed to connect to " + url + ", reason: " + e.getMessage();
		}
	}

	@ShellMethod(key = "scpi connect raw", value = "Connects a SCPI device using raw drivers")
	public String scpiConnect(@ShellOption(value = { "-h", "--host" }) String host,
			@ShellOption(value = { "-p", "--port" }) int port) {

		try {
			RawSocket socket = new RawSocket(host, port);
			ISCPISocket dev = scpi.connect(socket);

			return "Connected " + dev.getDeviceIdentifier().getModel() + " via " + socket.toString();
		} catch (Exception e) {
			return "Failed to connect to " + host + ":" + port + ", reason: " + e;
		}
	}

	@ShellMethod(key = "scpi connect mock", value = "Mocks a SCPI device using the given socket")
	public String scpiConnectMock(@ShellOption(value = { "-c", "--class" }) String className) {
		try {
			Class<? extends ISCPISocket> klazz = (Class<? extends ISCPISocket>) Class.forName(className);

			ISCPISocket mock = klazz.getConstructor().newInstance();
			ISCPISocket dev = scpi.connect(mock);

			return "Connected " + dev + " via " + mock.getClass().getSimpleName();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException c) {
			return "Mock class couldn't be instantiated " + className + ", reason: " + c;
		} catch(Exception e) {
			return "Mock instance couldn't be initialized " + className + ", reason: " + e;
		}
	}

	@ShellMethod(key="scpi send", value="Sends ")
	public String scpiSend(
		@ShellOption(value = {"-i", "--id"}) ISCPISocket device,
		@ShellOption(value = {"-c", "--command"}) String command,
		@ShellOption(value = {"-s", "--silent"}, defaultValue = "false") boolean silent,
		@ShellOption(value = {"-t", "--timeout"}, defaultValue = "1000") long timeout) {

		try {
			device.send(new SCPICommand(command));
			if(!silent) {
				return device.receive(timeout).orElse(new SCPICommand("")).getRaw();
			}
			else {
				Thread task = new Thread(() -> {
					try {
						device.receive(timeout);
					} catch (IOException e) {}
				});
				task.start();
				
				return null;
			}
		} catch(IOException e) {
			return "Failed to send/receive command, reason: " + e.getMessage();
		}
	}

	@ShellMethod(key="scpi disconnect", value="Disconnects the SCPI device")
	public String scpiDisconnect(
		@ShellOption(value = {"-i", "--id"}) ISCPISocket device) {

		if(scpi.disconnect(device)) {
			return "Disconnected " + device;
		}
		return "Device not found";
	}

	@ShellMethod(key={"scpi info"}, value="Prints out device information")
	public String scpiInfo(
		@ShellOption(value = {"-i", "--id"}) ISCPISocket device) {

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

	@ShellMethod(key={"scpi list", "scpi ls"}, value="Lists all SCPI devices")
	public String scpiList() {
		StringBuilder out = new StringBuilder();
		List<ISCPISocket> devices = scpi.listDevices();
		for(ISCPISocket dev : devices) {
			out.append(dev);
			out.append("\n");
		}
		return out.toString();
	}
	
}