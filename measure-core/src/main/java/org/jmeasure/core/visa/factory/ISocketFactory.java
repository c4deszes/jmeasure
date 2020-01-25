package org.jmeasure.core.visa.factory;

import org.jmeasure.core.device.ISocket;

/**
 * SocketFactory
 */
public interface ISocketFactory {

    boolean supports(String resourceURI);

    ISocket create(String resourceURI);
}