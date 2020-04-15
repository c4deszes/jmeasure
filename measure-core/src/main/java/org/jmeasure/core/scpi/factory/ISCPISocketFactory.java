package org.jmeasure.core.scpi.factory;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.visa.UnsupportedSocketException;
import org.jmeasure.core.visa.VisaDeviceFactory;

/**
 * ISCPISocketFactory
 */
public interface ISCPISocketFactory extends VisaDeviceFactory<ISCPISocket> {

    boolean supports(ISocket socket);

    ISCPISocket create(ISocket socket) throws UnsupportedSocketException;

}