package org.jmeasure.lxi;

import java.io.IOException;

import org.jmeasure.core.device.Connectable;

/**
 * SCPISocket is the interface for sending and receiving SCPI commands
 */
public interface SCPISocket extends Connectable {

	/**
	 * Sends the given SCPI commands to the device
	 * 
	 * **Note: this operation should be atomic, meaning either all the operations complete or none**
	 * The exact implementation therefore can vary based on the device's capabilities
	 * 
	 * @param commands
	 * @throws IOException
	 */
	void send(SCPICommand... commands) throws IOException;

	/**
	 * Receives a SCPI response
	 * @param timeout The amount of time to wait for a response in milliseconds, if timeout == 0 then wait indefinitely
	 * @return Returns SCPI response, null if the request has been timed out or interrupted
	 * @throws IOException
	 */
	SCPICommand receive(long timeout) throws IOException;
}