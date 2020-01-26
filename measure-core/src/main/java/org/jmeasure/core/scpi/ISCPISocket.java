package org.jmeasure.core.scpi;

import java.io.IOException;
import java.util.Optional;

import org.jmeasure.core.device.Connectable;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * ISCPISocket
 */
public interface ISCPISocket extends Connectable {

    public final static long DEFAULT_TIMEOUT = 1000;

    public void send(SCPICommand... commands) throws IOException;

    public Optional<SCPICommand> receive(long timeout) throws IOException;

    Optional<String> receive(int count, long timeout) throws IOException;

    default Optional<SCPICommand> query(SCPICommand command, long timeout) throws IOException {
        this.send(command);
        return this.receive(timeout);
    }

    default DeviceIdentifier getDeviceIdentifier() throws IOException {
        this.send(SCPI.idnQuery);
        return DeviceIdentifier.from(this.receive(DEFAULT_TIMEOUT).get().getRaw());
    }

    default void reset() throws IOException {
        this.send(SCPI.resetDevice);
    }

    default void waitForOperation(long timeout) throws IOException {
        this.send(SCPI.opcQuery);
        this.receive(timeout).orElseThrow(() -> new IOException());
    }

    static String concat(String termination, SCPICommand... commands) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<commands.length;i++) {
			builder.append(commands[i].toString());
			if(i != commands.length - 1) {
				builder.append(";");
			}
		}
		builder.append(termination);
		return builder.toString();
	}
}