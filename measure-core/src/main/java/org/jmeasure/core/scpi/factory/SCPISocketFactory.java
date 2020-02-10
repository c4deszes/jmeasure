package org.jmeasure.core.scpi.factory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.visa.UnsupportedSocketException;

/**
 * SCPISocketFactory
 */
public class SCPISocketFactory implements ISCPISocketFactory {

    private List<ISCPISocketFactory> factories;

    public SCPISocketFactory(ISCPISocketFactory... factories) {
        this(Arrays.asList(factories));
    }

    public SCPISocketFactory(List<ISCPISocketFactory> factories) {
        this.factories = new LinkedList<>(factories);
    }

    @Override
    public boolean supports(ISocket socket) {
        return factories.stream().anyMatch(factory -> factory.supports(socket));
    }

    @Override
    public ISCPISocket create(ISocket socket) throws UnsupportedSocketException {
        for(ISCPISocketFactory factory : factories) {
            if(factory.supports(socket)) {
                return factory.create(socket);
            }
        }
        throw new IllegalArgumentException();
    }
    
}