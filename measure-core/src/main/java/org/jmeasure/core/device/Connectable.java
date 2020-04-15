package org.jmeasure.core.device;

import java.io.IOException;

/**
 * Connectable is any device that has a binary state of connection, either connected or disconnected
 */
public interface Connectable extends AutoCloseable {

    /**
	 * Attempts to connect to the device.
	 * 
	 * <p><b>Note: this operation should be idempotent, meaning that calling it 
	 * multiple times shouldn't affect the state of the connection beyond the first call</b>
	 * 
	 * @throws IOException if the connection failed for some reason
	 */
	public void connect() throws IOException;

	/**
	 * Returns whether or not the device is connected
	 * 
	 * @return {@code true} if the device is connected
	 */
	public boolean isConnected();

	/**
	 * Returns connection information
	 * 
	 * <p><b>Note: using the returned the String the factory must be able to recreate the connection 
	 * (or there should be at least 1 public constructor with a single String parameter)</b>
	 * 
	 * @return Connection information
	 */
	public String getResourceString();

	/**
	 * Disconnects the device
	 * 
	 * <p><b>Note: this operation should be idempotent, meaning that calling it 
	 * multiple times shouldn't affect the state of the connection beyond the first call</b>
	 */
	@Override
	public void close();
}