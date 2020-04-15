package org.jmeasure.core.visa.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.UnsupportedSocketException;

/**
 * SocketFactory is a composite ISocketFactory
 */
public class SocketFactory implements ISocketFactory {

    private List<ISocketFactory> factories;

    /**
     * Constructs a 
     * @param factories
     */
    public SocketFactory(List<ISocketFactory> factories) {
        this.factories = new LinkedList<>(factories);
    }

    public SocketFactory(ISocketFactory... factories) {
        this(Arrays.asList(factories));
    }

    @Override
    public boolean supports(String resourceURI) {
        return factories.stream().anyMatch(factory -> factory.supports(resourceURI));
    }

    @Override
    public ISocket create(String resourceString, InstrumentNameProvider nameProvider) throws IOException, UnsupportedSocketException {
        for(ISocketFactory factory: factories) {
            if(factory.supports(resourceString)) {
                try {
                    return factory.create(resourceString, nameProvider);
                } catch(UnsupportedSocketException e) {
                    //TODO log warning
                }  
            }
        }
        throw new UnsupportedSocketException();
    }

    
}