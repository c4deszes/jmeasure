package org.jmeasure.lxi;

import java.io.IOException;
import java.util.Optional;

import org.jmeasure.core.device.Connectable;

/**
 * SCPISocket is the interface for sending and receiving SCPI commands
 * 
 * @see RawSCPISocket
 */
public interface SCPISocket extends Connectable {

	/**
	 * Sends the given SCPI commands to the device
	 * 
	 * <p>
	 * <b>
	 * Note: this operation should be atomic, meaning either all the operations complete or none
	 * The exact implementation therefore can vary based on the device's capabilities
	 * </b>
	 * 
	 * @param commands SCPI commands to send
	 * @throws IOException if it couldn't send the commands
	 */
	void send(SCPICommand... commands) throws IOException;

	/**
	 * Receives a SCPI response
	 * 
	 * @param timeout The amount of time to wait for a response in milliseconds, if timeout == 0 then wait indefinitely
	 * @return Returns SCPI response, empty if the device didn't respond
	 * @throws IOException if it couldn't receive a command due to device error
	 */
	Optional<SCPICommand> receive(long timeout) throws IOException;

	/**
	 * Returns the 
	 * 
	 * @param commands
	 * @return
	 */
	public static String concat(SCPICommand... commands) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<commands.length;i++) {
			builder.append(commands[i].toString());
			if(i != commands.length - 1) {
				builder.append(";");
			}
		}
		builder.append("\n");
		return builder.toString();
	}
}