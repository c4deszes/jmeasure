package org.jmeasure.core.scpi.mock;

import java.util.HashMap;
import java.util.Map;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.factory.ISCPISocketFactory;
import org.jmeasure.core.visa.UnsupportedSocketException;
import org.jmeasure.core.visa.mock.MockSocket;

/**
 * MockSCPIDeviceFactory
 */
public class MockSCPISocketFactory implements ISCPISocketFactory {

    private Map<String, Class<? extends MockSCPISocket>> classes;

    public MockSCPISocketFactory() {
        classes = new HashMap<>();
    }

    public void register(String name, Class<? extends MockSCPISocket> klass) {
        classes.put(name, klass);
    }

    @Override
    public boolean supports(ISocket socket) {
        return (socket instanceof MockSocket);
    }

    @Override
    public ISCPISocket create(ISocket socket) throws UnsupportedSocketException {
        if (socket instanceof MockSocket) {
            try {
                MockSocket mock = (MockSocket) socket;
                Class<? extends MockSCPISocket> mockClass = classes.get(mock.getClassName());
                if(mockClass == null) {
                    mockClass = (Class<? extends MockSCPISocket>) Class.forName(mock.getClassName());
                }
                return mockClass.getConstructor(MockSocket.class).newInstance(socket);
            } catch (Exception e) {
                throw new UnsupportedSocketException(e);
            }
        }
        throw new UnsupportedSocketException();
    }
    
}