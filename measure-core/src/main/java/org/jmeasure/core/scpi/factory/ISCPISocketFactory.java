package org.jmeasure.core.scpi.factory;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.visa.UnsupportedSocketException;

/**
 * ISCPISocketFactory
 */
public interface ISCPISocketFactory {

    boolean supports(ISocket socket);

    ISCPISocket create(ISocket socket) throws UnsupportedSocketException;

}