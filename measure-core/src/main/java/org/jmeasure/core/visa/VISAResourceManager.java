package org.jmeasure.core.visa;

import java.util.HashMap;
import java.util.Map;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPIService;
import org.jmeasure.core.visa.factory.SocketFactory;

/**
 * VISAResourceManager
 */
public class VISAResourceManager implements AutoCloseable {

    private SocketFactory socketFactory;

    private SCPIService scpiService;

    private Map<String, ISocket> sockets = new HashMap<>();

    public VISAResourceManager(final SocketFactory socketFactory, final SCPIService scpiService) {
        this.socketFactory = socketFactory;
        this.scpiService = scpiService;
    }
    
    public <T> T connect(String resourceString, Class<T> klass) throws UnsupportedSocketException {
        return this.connect(socketFactory.create(resourceString), klass);
    }

    public <T> T connect(ISocket socket, Class<T> klass) throws UnsupportedSocketException {
        String name = socket.getInstrumentName().orElse(getDefaultInstrumentName());
        
        this.sockets.put(name, socket);
        
        ISCPISocket scpiSocket = scpiService.connect(name, socket);

        return (T) scpiSocket;
    }

    public void disconnect(String instrumentName) {
        this.sockets.get(instrumentName).disconnect();
        this.sockets.remove(instrumentName);
    }

    @Override
    public void close() {
        this.sockets.forEach((name, dev) -> dev.disconnect());
    }

    private String getDefaultInstrumentName() {
        int counter = 0;
        String name = instrumentName(counter);
        while(sockets.containsKey(name)) {
            counter++;
            name = instrumentName(counter);
        }
        return name;
    }

    private static String instrumentName(int c) {
        return "inst" + c;
    }

}