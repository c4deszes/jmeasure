package org.jmeasure.core.scpi;

import java.io.IOException;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.factory.SCPIDeviceFactory;
import org.jmeasure.core.scpi.factory.SCPISocketFactory;
import org.jmeasure.core.visa.UnsupportedSocketException;
import org.jmeasure.core.visa.VisaDeviceFactory;

import lombok.Setter;

/**
 * SCPIService
 */
public class SCPIService implements VisaDeviceFactory<ISCPISocket> {

    @Setter
    private SCPISocketAdapter defaultAdapter;

    @Setter
    private SCPISocketFactory scpiSocketFactory;

    @Setter
    private SCPIDeviceFactory scpiDeviceFactory;

    public SCPIService() {
        this(null, null);
    }

    public SCPIService(SCPISocketFactory scpiSocketFactory, SCPIDeviceFactory scpiDeviceFactory) {
        this(scpiSocketFactory, scpiDeviceFactory, null);
    }

    public SCPIService(SCPISocketFactory scpiSocketFactory, SCPIDeviceFactory scpiDeviceFactory, SCPISocketAdapter defaultAdapter) {
        this.scpiSocketFactory = scpiSocketFactory;
        this.scpiDeviceFactory = scpiDeviceFactory;
        this.defaultAdapter = defaultAdapter;
    }

    @Override
    public boolean supports(ISocket socket) {
        return scpiSocketFactory.supports(socket);
    }

    @Override
    public ISCPISocket create(ISocket socket) throws UnsupportedSocketException {
        return this.create(socket, defaultAdapter);
    }

    public ISCPISocket create(ISocket socket, SCPISocketAdapter adapter) throws UnsupportedSocketException {
        try {
            ISCPISocket scpiSocket = scpiSocketFactory.create(socket);
            if (adapter != null) {
                scpiSocket = adapter.clone(scpiSocket);
            }
            scpiSocket = scpiDeviceFactory.create(scpiSocket);
            return scpiSocket;
        } catch (IOException e) {
            throw new UnsupportedSocketException(e);
        }
    }

}