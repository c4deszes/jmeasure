package org.jmeasure.core.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * ISocket
 */
public interface ISocket extends Connectable {

    void send(final ByteBuffer message) throws IOException;

    ByteBuffer receive(final int count, final long timeout) throws IOException, TimeoutException;

    ByteBuffer receive(final char delimiter, final long timeout) throws IOException, TimeoutException;

    default Optional<String> getInstrumentName() {
        return Optional.empty();
    }
}