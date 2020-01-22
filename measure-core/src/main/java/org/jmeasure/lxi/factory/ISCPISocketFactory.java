package org.jmeasure.lxi.factory;

import java.io.IOException;

import org.jmeasure.lxi.SCPISocket;

/**
 * A SCPISocketFactory is able to create SCPISockets using VISA resource strings
 */
public interface ISCPISocketFactory {

    /**
     * Returns whether it can create a socket using the resource string
     * 
     * @param resource Resource string
     * @return {@code true} if the factory is capable of creating a socket using the resource string
     */
    boolean supports(String resource);

    /**
     * Creates a socket using the resource string
     * 
     * @param resource Resource string
     * @return SCPISocket
     * @throws IOException if creating the socket failed
     */
    SCPISocket create(String resource) throws IOException;
}