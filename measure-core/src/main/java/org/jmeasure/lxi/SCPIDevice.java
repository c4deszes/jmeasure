package org.jmeasure.lxi;

import java.io.IOException;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * A SCPIDevice is a device understanding SCPI commands
 * 
 * <p>
 * This class is an adapter for SCPISocket and extends it with standard functionality, 
 * such as resetting the device and retrieving the device identifier.
 * 
 * @author Balazs Eszes
 */
public class SCPIDevice implements SCPISocket {

	private final SCPISocket socket;
	
	/**
	 * The identifier of this device
	 * <p>
	 * The setter is protected, so if necessary the driver can change it
	 */
	@Getter
	@Setter(AccessLevel.PROTECTED)
	private DeviceIdentifier deviceIdentifier;

	/**
	 * Constructs a new SCPI device
	 * 
	 * @param deviceIdentifier Device's response to standard *IDN? command
	 * @param socket SCPISocket used to connect to the device (like {@link org.jmeasure.lxi.socket.RawSCPISocket RawSCPISocket}
	 * @throws IOException if the connection to the device failed
	 */
	public SCPIDevice(SCPISocket socket, DeviceIdentifier deviceIdentifier) throws IOException {
		this.socket = socket;
		this.deviceIdentifier = deviceIdentifier;
		this.socket.connect();
	}

	/**
	 * Constructs a new SCPI device
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public SCPIDevice(SCPISocket socket) throws IOException {
		this(socket, null);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[model=" + deviceIdentifier.getModel() + ", sn=" + deviceIdentifier.getSerialNumber() + ", interface=" + socket.getClass().getSimpleName() + "]";
	}

	public final String getConnectionInfo() {
		return socket.toString();
	}

	@Override
	public final void send(SCPICommand... commands) throws IOException {
		socket.send(commands);
	}

	/**
	 * Queries a resource, then waits for a response
	 * 
	 * @param timeout Maximum time of waiting for a response in milliseconds
	 * @param command SCPI Command to send
	 * @return SCPI response
	 * @throws IOException
	 */
	public final Optional<SCPICommand> query(SCPICommand command, long timeout) throws IOException {
		socket.send(command);
		return this.receive(timeout);
	}

	/**
	 * Queries a resource, then waits for a response indefinitely
	 * 
	 * @param command SCPICommand to send
	 * @return SCPI response, null if not responding and interrupted
	 * @throws IOException
	 */
	public final Optional<SCPICommand> query(SCPICommand command) throws IOException {
		return this.query(command, 0);
	}

	/**
	 * Waits for a response indefinitely
	 * 
	 * @return SCPI response
	 * @throws IOException
	 */
	public final Optional<SCPICommand> receive() throws IOException {
		return this.receive(0);
	}

	@Override
	public final Optional<SCPICommand> receive(long timeout) throws IOException {
		return socket.receive(timeout);
	}

	/**
	 * Waits for an operation complete signal indefinitely
	 * 
	 * @return true once the signal is received
	 * @throws IOException 
	 */
	public final boolean waitForOPC() throws IOException {
		return this.waitForOPC(0);
	}

	/**
	 * Waits for an operation complete signal
	 * 
	 * @param timeout Maximum time of waiting for a response in milliseconds
	 * @return {@code true} if the signal was received
	 * @throws IOException
	 */
	public final boolean waitForOPC(long timeout) throws IOException {
		socket.send(SCPI.opcQuery);
		return this.receive(timeout).isPresent();
	}

	/**
	 * Resets the device by sending the SCPI *RST command
	 * 
	 * @throws IOException
	 */
	public final void reset() throws IOException {
		socket.send(SCPI.resetDevice);
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