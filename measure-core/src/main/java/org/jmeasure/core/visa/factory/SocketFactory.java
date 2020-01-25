package org.jmeasure.core.visa.factory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmeasure.core.device.ISocket;

/**
 * SocketFactory
 */
public class SocketFactory implements ISocketFactory {

    private List<ISocketFactory> factories;

    public SocketFactory(List<ISocketFactory> factories) {
        this.factories = new LinkedList<>(factories);
    }

    public SocketFactory(ISocketFactory...factories) {
        this(Arrays.asList(factories));
    }

    @Override
    public boolean supports(String resourceURI) {
        return factories.stream().anyMatch(factory -> factory.supports(resourceURI));
    }

    @Override
    public ISocket create(String resourceURI) {
        for(ISocketFactory factory: factories) {
            if(factory.supports(resourceURI)) {
                return factory.create(resourceURI);
            }
        }
        throw new IllegalArgumentException();
    }

    
}