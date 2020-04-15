package org.jmeasure.core.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * ISocket is a connectable socket that's capable of transmitting and receiving byte streams
 */
public interface ISocket extends Connectable {

    /**
     * Transmits the given message to the device
     * 
     * @param message Message as a byte buffer
     * @throws IOException
     */
    void send(final ByteBuffer message) throws IOException;

    /**
     * Receives a message from the device until the given byte count has been reached
     * 
     * @param count Message's length
     * @param timeout Maximum time to wait for the given number of bytes to be received 
     * @return Message as a byte buffer
     * @throws IOException If there was an error during reception
     * @throws TimeoutException If the timeout has been reached
     */
    ByteBuffer receive(final int count, final long timeout) throws IOException, TimeoutException;

    /**
     * Receives a message from the device until the given character has been reached
     * 
     * @param termination Termination character, like '\n'
     * @param timeout Maximum time to wait for the given character to be reached
     * @return Message as a byte buffer
     * @throws IOException If there was an error during reception
     * @throws TimeoutException If the timeout has been reached
     */
    ByteBuffer receive(final char termination, final long timeout) throws IOException, TimeoutException;

    /**
     * Returns the instrument name associated with this socket
     */
     String getInstrumentName();
}