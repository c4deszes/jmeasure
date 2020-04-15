package org.jmeasure.core.scpi;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.jmeasure.core.device.Connectable;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * ISCPISocket is able to send and receive SCPI commands
 */
public interface ISCPISocket extends Connectable {

    public final static long DEFAULT_TIMEOUT = 1000;

    /**
     * Sends SCPI commands to the device, how they're being sent is up to the socket implementation
     * <p>
     * The operation should be atomic, so either all commands should succeed or the device's state shall not change
     * 
     * @param commands SCPI commands to send
     * @throws IOException if 
     */
    public void send(SCPICommand... commands) throws IOException;

    /**
     * Receives a SCPI response from the device
     * <p>
     * The operation has to return {@code Optional.empty()} if the method timed out with no response
     * 
     * @param timeout Time to wait for a response, if 0 then wait indefinitely
     * @return Optional of String, empty if the device didn't respond
     * @throws IOException If the 
     */
    public Optional<String> receive(long timeout) throws IOException;

    /**
     * Receives part of the response from the device, up until the given character count has been reached
     * @param count
     * @param timeout
     * @return
     * @throws IOException
     */
    Optional<String> receive(int count, long timeout) throws IOException;

    default Optional<String> query(SCPICommand command, long timeout) throws IOException {
        this.send(command);
        return this.receive(timeout);
    }

    default DeviceIdentifier getDeviceIdentifier() throws IOException {
        try {
            this.send(SCPI.idnQuery);
            DeviceIdentifier dev = DeviceIdentifier.from(this.receive(DEFAULT_TIMEOUT).get());

            return dev;
        } catch(IllegalArgumentException e) {
            throw new IOException("Cannot resolve device identifier", e);
        } catch(NoSuchElementException e) {
            throw new IOException("No device identifier received", e);
        }
    }

    default void reset() throws IOException {
        this.send(SCPI.resetDevice);
    }

    default void clearStatus() throws IOException {
        this.send(SCPI.clearStatus);
    }

    default void waitForOperation(long timeout) throws IOException {
        this.send(SCPI.opcQuery);
        this.receive(timeout).orElseThrow(() -> new IOException());
    }

    static String concat(char termination, char separator, SCPICommand... commands) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<commands.length;i++) {
			builder.append(commands[i].toString());
			if(i != commands.length - 1) {
				builder.append(separator);
			}
		}
		builder.append(termination);
		return builder.toString();
	}
}