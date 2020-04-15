package org.jmeasure.core.visa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.factory.ISocketFactory;
import org.jmeasure.core.visa.factory.InstrumentNameProvider;
import org.jmeasure.core.visa.factory.SocketFactory;

import lombok.Setter;

/**
 * VISAResourceManager
 */
public class VISAResourceManager implements AutoCloseable, InstrumentNameProvider {

    @Setter
    private ISocketFactory socketFactory;

    private List<VisaDeviceFactory<?>> factories;

    private List<VisaDeviceDecorator<?>> decorators;

    private transient Map<String, Object> sockets;

    public VISAResourceManager(final ISocketFactory socketFactory, List<VisaDeviceFactory<?>> factories, List<VisaDeviceDecorator<?>> decorators) {
        this.socketFactory = socketFactory;
        this.factories = new LinkedList<>(factories);
        this.decorators = new LinkedList<>(decorators);
        this.sockets = new HashMap<>();
    }

    public ISocket connect(String resourceString) throws IOException, VisaException {
        return this.connect(resourceString, ISocket.class);
    }

    public ISocket connect(ISocket socket) throws VisaException {
        return this.connect(socket, ISocket.class);
    }

    public <T> T connect(String resourceString, Class<T> klass) throws IOException, VisaException {
        try {
            return this.connect(socketFactory.create(resourceString, this), klass);
        } catch(UnsupportedSocketException e) {
            throw new VisaException(e);
        }
    }

    public <T> T connect(ISocket socket, Class<T> klass) throws VisaException {
        ArrayList<Object> candidates = new ArrayList<>(factories.size());
        candidates.add(socket);
        
        for(VisaDeviceFactory<?> factory : factories) {
            try {
                if(factory.supports(socket)) {
                    Object dev = factory.create(socket);
                    candidates.add(dev);
                }
            } catch(UnsupportedSocketException e) {
                e.printStackTrace();
            }
        }
        
        try {
            T candidate = findCandidate(candidates, klass);
            this.sockets.put(socket.getInstrumentName(), candidate);
            return candidate;
        } catch(NoSuchElementException e) {
            throw new VisaException("No candidate found for type " + klass);
        }
    }

    public ISocket get(String instrumentName) {
        return this.get(instrumentName, ISocket.class);
    }

    public <T> T get(String instrumentName, Class<T> klass) {
        return klass.cast(sockets.get(instrumentName));
    }

    public <T> Set<T> getAll(Class<T> klass) {
        return findCandidates(this.sockets.values(), klass);
    }

    private static <T> T findCandidate(Collection<Object> candidates, Class<T> klass) throws NoSuchElementException {
        for(Object obj : candidates) {
            if(klass.isAssignableFrom(obj.getClass())) {
                //TODO: handle ambiguity, prefer equal type
                return klass.cast(obj);
            }
        }
        throw new NoSuchElementException();
    }

    private static <T> Set<T> findCandidates(Collection<Object> objects, Class<T> klass) throws NoSuchElementException {
        Set<T> candidates = new HashSet<>();
        for(Object obj : objects) {
            if(klass.isAssignableFrom(obj.getClass())) {
                candidates.add(klass.cast(obj));
            }
        }
        return candidates;
    }

    

    @Override
    public void close() {
        //this.sockets.forEach((name, dev) -> dev.close());
    }

    @Override
    public String apply(String name) {
        if(name == null || name.isEmpty()) {
            return this.getDefaultInstrumentName();
        }
        return name;
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