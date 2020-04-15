package org.jmeasure.core.visa;

import org.jmeasure.core.device.ISocket;

/**
 * VisaDeviceFactory
 */
public interface VisaDeviceFactory<T> {

    boolean supports(ISocket socket);

    T create(ISocket socket) throws UnsupportedSocketException;
}