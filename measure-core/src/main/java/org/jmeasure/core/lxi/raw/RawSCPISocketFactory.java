package org.jmeasure.core.lxi.raw;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.factory.ISCPISocketFactory;

/**
 * RawSCPISocketFactory
 */
public class RawSCPISocketFactory implements ISCPISocketFactory {

    @Override
    public boolean supports(ISocket socket) {
        return (socket instanceof RawSocket);
    }

    @Override
    public ISCPISocket create(ISocket socket) {
        //TODO: add termination, etc. configuration
        return new RawSCPISocket(socket);
    }
    
}