package org.jmeasure.core.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

/**
 * ISocket
 */
public interface ISocket extends Connectable {

    public void send(final ByteBuffer message) throws IOException;

    public ByteBuffer receive(final int count, final long timeout) throws IOException, TimeoutException;

    public ByteBuffer receive(final char delimiter, final long timeout) throws IOException, TimeoutException;
}