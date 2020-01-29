package org.jmeasure.core.scpi.socket;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.lxi.raw.RawSocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.factory.ISCPISocketFactory;
import org.jmeasure.core.serial.SerialSocket;

/**
 * RawSCPISocketFactory
 */
public class RawSCPISocketFactory implements ISCPISocketFactory {

    @Override
    public boolean supports(ISocket socket) {
        return (socket instanceof RawSocket) || (socket instanceof SerialSocket);
    }

    @Override
    public ISCPISocket create(ISocket socket) {
        //TODO: add termination, etc. configuration
        return new RawSCPISocket(socket);
    }
    
}