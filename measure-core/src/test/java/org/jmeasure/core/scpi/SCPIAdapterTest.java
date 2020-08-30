package org.jmeasure.core.scpi;

import java.io.IOException;

import org.jmeasure.core.scpi.adapter.SCPIFilter;
import org.jmeasure.core.scpi.mock.MockSCPISocket;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.mock.MockSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * SCPIAdapterTest
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SCPIAdapterTest {

    @Test
    public void testFilterAllowEverything() throws IOException {
        ISCPISocket filter = new SCPIFilter(new TestSCPISocket(), command -> {
            return true;
        });
        filter.send(new SCPICommand("ABCD"));
        filter.close();
    }

    @Test(expected = IOException.class)
    public void testFilterAllowIllegalCall() throws IOException {
        ISCPISocket filter = new SCPIFilter(new TestSCPISocket(), command -> {
            return true;
        });
        filter.send(new SCPICommand("BLCK"));
        filter.close();
    }

    @Test
    public void testFilterBlockIllegalCall() throws IOException {
        ISCPISocket filter = new SCPIFilter(new TestSCPISocket(), command -> {
            if(command.getCommand().equals("BLCK")) {
                return false;
            }
            return true;
        });
        filter.send(new SCPICommand("BLCK"));
        filter.close();
    }

    static class TestSCPISocket extends MockSCPISocket {

        public TestSCPISocket() {
            super(new MockSocket("TestSocket"), DeviceIdentifier.from("Test", "Test", "ABC123", "1.0"));
        }

        @Override
        protected void onReceive(SCPICommand in) throws Exception {
            if(in.getCommand().equals("BLCK")) {
                throw new UnsupportedOperationException("Illegal method call");
            }
        }

        @Override
        public void onReset() {
            
        }

        @Override
        public void onNotMapped(SCPICommand command) {
            
        }

    }
    
}