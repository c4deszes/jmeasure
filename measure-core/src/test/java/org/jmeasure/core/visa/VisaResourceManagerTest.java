package org.jmeasure.core.visa;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jmeasure.core.scpi.SCPI;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.SCPIService;
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

    private SCPIService scpiService = new SCPIService(new SCPISocketFactory(scpiSocketFactory));

    private VISAResourceManager visa = new VISAResourceManager(socketFactory, scpiService);

    @Before
    public void before() {
        scpiSocketFactory.register("TestDevice", TestSocket.class);
    }

    @Test
    public void testConnectViaResourceString() throws IOException {
        try(TestSocket device = visa.connect("MOCK::TestDevice::inst0::INSTR", TestSocket.class)) {
            DeviceIdentifier response = device.getDeviceIdentifier();
            assertEquals("Test", response.getManufacturer());
        }
    }

    @Test
    public void testConnectSocket() {
        MockSocket socket = new MockSocket("TestDevice");
        try(TestSocket device = visa.connect(socket, TestSocket.class)) {
            DeviceIdentifier response = device.getDeviceIdentifier();
            assertEquals("Test", response.getManufacturer());
        }
    }

    @Test
    public void testDisconnect() {

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