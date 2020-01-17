package org.jmeasure.lxi;

import java.io.IOException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * A SCPIDevice is a device understanding SCPI commands
 * 
 * This class is an adapter for SCPISocket and extends it with standard functionality, such as resetting the device.
 */
public class SCPIDevice implements SCPISocket {

	private final SCPISocket socket;
	
	@Getter
	@Setter(AccessLevel.PROTECTED)
	private DeviceIdentifier deviceIdentifier;

	/**
	 * Constructs a new SCPI device
	 * @param deviceIdentifier Device's response to standard *IDN? command
	 * @param socket SCPISocket used to connect to the device (like {@link org.jmeasure.lxi.socket.RawSCPISocket RawSCPISocket}
	 * @throws IOException if the connection to the device failed
	 */
	public SCPIDevice(DeviceIdentifier deviceIdentifier, SCPISocket socket) throws IOException {
		this.socket = socket;
		this.deviceIdentifier = deviceIdentifier;
		this.socket.connect();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[dev=" + deviceIdentifier.getModel() + ", sn=" + deviceIdentifier.getSerialNumber() + ", driver=" + socket.getClass().getSimpleName() + "]";
	}

	@Override
	public final void send(SCPICommand... commands) throws IOException {
		socket.send(commands);
	}

	/**
	 * Queries a resource, 
	 * @param timeout
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public final SCPICommand query(SCPICommand command, long timeout) throws IOException {
		socket.send(command);
		return this.receive(timeout);
	}

	/**
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public final SCPICommand query(SCPICommand command) throws IOException {
		return this.query(command, 0);
	}

	/**
	 * Convenience method for waiting for response indefinitely
	 * @return SCPI response
	 * @throws IOException if the device has disconnected
	 */
	public final SCPICommand receive() throws IOException {
		return this.receive(0);
	}

	@Override
	public final SCPICommand receive(long timeout) throws IOException {
		return socket.receive(timeout);
	}

	public final boolean waitForOPC() throws IOException {
		return this.waitForOPC(0);
	}

	public final boolean waitForOPC(long timeout) throws IOException {
		socket.send(SCPI.opcQuery);
		return this.receive(timeout) != null ? true : false;
	}

	public final void reset() {

	}

	@Override
	public void connect() throws IOException {
		socket.connect();
	}

	@Override
	public void disconnect() {
		socket.disconnect();
	}

	@Override
	public boolean isConnected() {
		return socket.isConnected();
	}
}