package org.jmeasure.core.scpi;

import java.io.IOException;
import java.util.Optional;

import org.jmeasure.core.device.Connectable;

/**
 * ISCPISocket
 */
public interface ISCPISocket extends Connectable {

    public void send(SCPICommand... commands) throws IOException;

    public Optional<SCPICommand> receive(long timeout) throws IOException;

    Optional<String> receive(int count, long timeout) throws IOException;
}