package org.jmeasure.core.scpi;

import java.io.IOException;
import java.util.Optional;

import org.jmeasure.core.visa.DeviceIdentifier;

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
public class SCPIDevice implements ISCPISocket {
	
	private final ISCPISocket socket;

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
	public SCPIDevice(final ISCPISocket socket, DeviceIdentifier deviceIdentifier) throws IOException {
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
	public SCPIDevice(final ISCPISocket socket) throws IOException {
		this(socket, null);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[model=" + deviceIdentifier.getModel() + ", sn=" + deviceIdentifier.getSerialNumber() + ", interface=" + socket.getClass().getSimpleName() + "]";
	}

	@Override
	public final String getConnectionInfo() {
		return socket.getConnectionInfo();
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

	@Override
	public void send(SCPICommand... commands) throws IOException {
		socket.send(commands);
	}

	@Override
	public Optional<SCPICommand> receive(long timeout) throws IOException {
		return socket.receive(timeout);
	}

	@Override
	public Optional<String> receive(int count, long timeout) throws IOException {
		return socket.receive(count, timeout);
	}
}