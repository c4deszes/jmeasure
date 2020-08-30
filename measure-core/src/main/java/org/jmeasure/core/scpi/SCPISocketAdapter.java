package org.jmeasure.core.scpi;

import java.io.IOException;
import java.util.Optional;

import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * SCPISocketAdapter is a proxy class for the SCPISocket interface
 * 
 * <p>The only additional functionality it provides, is that it caches the Device identifier.
 */
public class SCPISocketAdapter implements ISCPISocket {

	private final ISCPISocket adapter;

	private DeviceIdentifier deviceIdentifier;

	public SCPISocketAdapter(final ISCPISocket adapter) {
		this(adapter, null);
	}

	public SCPISocketAdapter(final ISCPISocket adapter, DeviceIdentifier deviceIdentifier) {
		this.adapter = adapter;
		this.deviceIdentifier = deviceIdentifier;
	}

	@Override
	public DeviceIdentifier getDeviceIdentifier() throws IOException {
		return this.deviceIdentifier == null ? (this.deviceIdentifier = ISCPISocket.super.getDeviceIdentifier()) : this.deviceIdentifier;
	}
	
	protected void setDeviceIdentifier(DeviceIdentifier deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}

	@Override
	public Optional<String> receive(long timeout) throws IOException {
		return adapter.receive(timeout);
	}

	@Override
	public Optional<String> receive(int count, long timeout) throws IOException {
		return adapter.receive(count, timeout);
	}

	@Override
	public void send(SCPICommand... commands) throws IOException {
		adapter.send(commands);
	}

	@Override
	public void close() {
		adapter.close();
	}

	@Override
	public boolean isConnected() {
		return adapter.isConnected();
	}

	public SCPISocketAdapter clone(ISCPISocket socket) {
		return new SCPISocketAdapter(socket);
	}
	
}