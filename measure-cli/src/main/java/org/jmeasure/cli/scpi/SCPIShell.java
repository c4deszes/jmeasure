package org.jmeasure.cli.scpi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;
import org.jmeasure.lxi.socket.RawSCPISocket;
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
			@ShellOption(value = { "-p", "--path" }) String url,
			@ShellOption(value = { "-a", "--alias" }, defaultValue = ShellOption.NULL) String alias,
			@ShellOption(value = { "--force-driver"}, defaultValue = ShellOption.NULL) String driverClass) {

		try {
			SCPIDevice dev = scpi.connect(url);
			if (alias != null) {
				dev = scpi.createAlias(dev, alias);
			}
			return "Connected " + dev + " via " + url;
		} catch (Exception e) {
			return "Failed to connect to " + url + ", reason: " + e.getMessage();
		}
	}

	@ShellMethod(key = "scpi connect raw", value = "Connects a SCPI device using raw drivers")
	public String scpiConnect(@ShellOption(value = { "-h", "--host" }) String host,
			@ShellOption(value = { "-p", "--port" }) int port,
			@ShellOption(value = { "-a", "--alias" }, defaultValue = ShellOption.NULL) String alias) {

		try {
			SCPIDevice dev = scpi.connect(new RawSCPISocket(host, port));
			if (alias != null) {
				dev = scpi.createAlias(dev, alias);
			}
			return "Connected " + dev.getDeviceIdentifier().getModel() + " via " + host + ":" + port;
		} catch (Exception e) {
			return "Failed to connect to " + host + ":" + port + ", reason: " + e.getMessage();
		}
	}

	@ShellMethod(key = "scpi connect mock", value = "Mocks a SCPI device using the given drivers")
	public String scpiConnectMock(@ShellOption(value = { "-c", "--class" }) String className) {
		try {
			Class<? extends SCPISocket> klazz = (Class<? extends SCPISocket>) Class.forName(className);

			SCPISocket mock = klazz.getConstructor().newInstance();
			SCPIDevice dev = scpi.connect(mock);

			return "Connected " + dev + " via " + mock.getClass().getSimpleName();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException c) {
			return "Mock class couldn't be instantiated " + className + ", reason: " + c;
		} catch(Exception e) {
			return "Mock instance couldn't be initialized " + className + ", reason: " + e;
		}
	}

	@ShellMethod(key="scpi alias", value="Create an alias for a SCPI device")
	public String scpiAlias(
		@ShellOption(value = {"-i", "--id"}) SCPIDevice device,
		@ShellOption(value = {"-a", "--alias"}) String alias) {

		scpi.createAlias(device, alias);

		return "Created alias " + alias + " for " + device;
	}

	@ShellMethod(key="scpi send", value="Sends ")
	public String scpiSend(
		@ShellOption(value = {"-i", "--id"}) SCPIDevice device,
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
		@ShellOption(value = {"-i", "--id"}) SCPIDevice device) {

		if(scpi.disconnect(device)) {
			return "Disconnected " + device;
		}
		return "Device not found";
	}

	@ShellMethod(key={"scpi info"}, value="Prints out device information")
	public String scpiInfo(
		@ShellOption(value = {"-i", "--id"}) SCPIDevice device) {

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

		return out.toString();
	}

	@ShellMethod(key={"scpi list", "scpi ls"}, value="Lists all SCPI devices")
	public String scpiList() {
		StringBuilder out = new StringBuilder();
		List<SCPIDevice> devices = scpi.listDevices();
		for(SCPIDevice dev : devices) {
			out.append(dev);
			out.append("\n");
		}
		return out.toString();
	}
	
}