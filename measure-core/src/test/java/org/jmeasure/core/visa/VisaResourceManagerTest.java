package org.jmeasure.core.visa;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPI;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.SCPIService;
import org.jmeasure.core.scpi.factory.SCPIDeviceFactory;
import org.jmeasure.core.scpi.factory.SCPISocketFactory;
import org.jmeasure.core.scpi.mock.MockSCPISocket;
import org.jmeasure.core.scpi.mock.MockSCPISocketFactory;
import org.jmeasure.core.visa.factory.SocketFactory;
import org.jmeasure.core.visa.mock.MockSocket;
import org.jmeasure.core.visa.mock.MockSocketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * VisaResourceManagerTest
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class VisaResourceManagerTest {

    private MockSCPISocketFactory scpiSocketFactory = new MockSCPISocketFactory();

    private SocketFactory socketFactory = new SocketFactory(new MockSocketFactory());

    private SCPIService scpiService = new SCPIService(new SCPISocketFactory(scpiSocketFactory), new SCPIDeviceFactory());

    private VISAResourceManager visa = new VISAResourceManager(socketFactory, Collections.singletonList(scpiService), Collections.emptyList());

    @Before
    public void before() {
        scpiSocketFactory.register("TestSocket", TestSocket.class);
    }

    @Test
    public void testConnectSocket() throws IOException, VisaException {
        MockSocket socket = new MockSocket("TestSocket");
        try(ISocket device = visa.connect(socket)) {
            
        }
    }

    @Test
    public void testConnectMockSocket() throws VisaException {
        MockSocket socket = new MockSocket("TestSocket");
        try(MockSocket device = visa.connect(socket, MockSocket.class)) {
            
        }
    }

    @Test
    public void testConnectSCPISocket() throws VisaException, IOException {
        MockSocket socket = new MockSocket("TestSocket");
        try(ISCPISocket device = visa.connect(socket, ISCPISocket.class)) {
            DeviceIdentifier response = device.getDeviceIdentifier();
            assertEquals("Test", response.getManufacturer());
        }
    }

    @Test
    public void testDisconnect() throws VisaException {
        MockSocket socket = new MockSocket("TestSocket");
        try(MockSocket device = visa.connect(socket, MockSocket.class)) {
            
        }
    }

    public static class TestSocket extends MockSCPISocket {

        public TestSocket(MockSocket socket) {
            super(socket, DeviceIdentifier.from("Test", "Test", "ABC123", "1.0.0"));
        }

        @Override
        protected void onReset() {
            
        }

        @Override
        protected void onNotMapped(SCPICommand command) {
            
        }

    }
    
}