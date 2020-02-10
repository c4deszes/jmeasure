package org.jmeasure.core.scpi;

import java.util.HashMap;
import java.util.Map;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.factory.SCPISocketFactory;
import org.jmeasure.core.visa.UnsupportedSocketException;

import lombok.Getter;
import lombok.Setter;

/**
 * SCPIService
 */
public class SCPIService {

    @Setter
    private SCPISocketAdapter defaultAdapter;

    @Setter
    private SCPISocketFactory scpiSocketFactory;

    private transient Map<String, ISCPISocket> sockets;

    public SCPIService(SCPISocketFactory scpiSocketFactory) {
        this(scpiSocketFactory, null);
    }

    public SCPIService(SCPISocketFactory scpiSocketFactory, SCPISocketAdapter defaultAdapter) {
        this.scpiSocketFactory = scpiSocketFactory;
        this.defaultAdapter = defaultAdapter;
        this.sockets = new HashMap<>();
    }

    public ISCPISocket connect(String name, ISocket socket) throws UnsupportedSocketException {
        return this.connect(name, socket, defaultAdapter);
    }

    public ISCPISocket connect(String name, ISocket socket, SCPISocketAdapter adapter)
            throws UnsupportedSocketException {
        ISCPISocket scpiSocket = scpiSocketFactory.create(socket);
        if(adapter != null) {
            scpiSocket = adapter.clone(scpiSocket);
        }
        sockets.put(name, scpiSocket);
        return scpiSocket;
    }

}