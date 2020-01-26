package org.jmeasure.core.device;

import java.io.IOException;

/**
 * Connectable
 */
public interface Connectable extends AutoCloseable {

    /**
	 * Attempts to connect to the device.
	 * 
	 * <p><b>Note: this operation should be idempotent, meaning that calling it 
	 * multiple times shouldn't affect the state of the connection</b>
	 * 
	 * @throws IOException if the connection failed for some reason
	 */
	public void connect() throws IOException;

    /**
	 * Disconnects the device
	 * <p><b>Note: this operation should be idempotent, meaning that calling it 
	 * multiple times shouldn't affect the state of the connection</b>
	 */
	public void disconnect();

	/**
	 * Returns whether or not the device is connected
	 * @return true if the device is connected
	 * 
	 */
	public boolean isConnected();

	/**
	 * Returns information about this connection
	 * 
	 * @return Connection information
	 */
	public String getResourceString();

	/**
	 * Disconnects the device, effectively final and should only be called in a try-resource block
	 */
	@Override
	public default void close() {
		this.disconnect();
	}
}