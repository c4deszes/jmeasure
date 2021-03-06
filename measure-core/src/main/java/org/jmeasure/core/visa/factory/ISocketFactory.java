package org.jmeasure.core.visa.factory;

import java.io.IOException;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.UnsupportedSocketException;

/**
 * SocketFactory is a factory capable of creating Sockets using a VISA resource string
 * 
 * <p>
 * The factory may have internal default configurations, such as baud rates for serial implementations.
 * It's recommended to have a no-args constructor with these defaults, setters for certain parameters and an all-args constructor.
 */
public interface ISocketFactory {

    /**
     * Returns whether this factory is capable of creating a socket given the resource string
     * @param resourceString VISA Resource string
     * @return {@code true} if the factory can create a socket from the resource string 
     */
    boolean supports(String resourceString);

    /**
     * Creates a socket using the given resource string
     * 
     * <p>
     * The return type should be overriden by the socket implementation this factory can provide
     * 
     * @param resourceString VISA Resource string
     * @return Socket
     * @throws IOException if the creation failed because of a communication error
     * @throws UnsupportedSocketException if the socket creation failed because of a bad resource string
     */
    ISocket create(String resourceString) throws IOException, UnsupportedSocketException;
}