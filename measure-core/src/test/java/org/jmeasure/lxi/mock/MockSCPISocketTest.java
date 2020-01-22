package org.jmeasure.lxi.mock;

import java.io.IOException;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPI;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPIDevice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * MockSCPISocketTest
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class MockSCPISocketTest {

    @Test
    public void testIdn() throws IOException {
        SCPIDevice device = new SCPIDevice(new TestDevice());
        SCPICommand response = device.query(SCPI.idnQuery).get();
        DeviceIdentifier id = DeviceIdentifier.from(response.getRaw());

        Assert.assertEquals("Test", id.getManufacturer());
        Assert.assertEquals("DEV1", id.getModel());
        Assert.assertEquals("ABC123", id.getSerialNumber());
        Assert.assertEquals("1.0.0", id.getFirmwareVersion());

        device.close();
    }

    @Test
    public void testBasicCommand() throws IOException {
        SCPIDevice device = new SCPIDevice(new TestDevice());
        SCPICommand response = device.query(SCPICommand.builder().query("BASIC").build()).get();

        Assert.assertEquals(true, response.hasParameter("PARAM"));
        Assert.assertEquals("VALUE", response.getParameter("PARAM").get());

        device.close();
    }

    @Test
    public void testBasicParameter() throws IOException {
        SCPIDevice device = new SCPIDevice(new TestDevice());
        SCPICommand response = device.query(SCPICommand.builder().query("BASIC2").with("PARAM", "INPUT").build()).get();

        Assert.assertEquals(true, response.hasParameter("PARAM"));
        Assert.assertEquals("INPUT", response.getParameter("PARAM").get());

        device.close();
    }

    @Test
    public void testCommandPathInt() throws IOException {
        SCPIDevice device = new SCPIDevice(new TestDevice());
        SCPICommand response = device.query(SCPICommand.builder().command("C1:CMD").build()).get();

        Assert.assertEquals(true, response.hasParameter("PARAM"));
        Assert.assertEquals("VALUE", response.getParameter("PARAM").get());

        device.close();
    }

    public static class TestDevice extends MockSCPISocket {

        public TestDevice() {
            super(DeviceIdentifier.from("Test", "DEV1", "ABC123", "1.0.0"));
        }

        @OnCommand("BASIC\\?")
        public SCPICommand basic() {
            return SCPICommand.builder().command("BASIC").with("PARAM", "VALUE").build();
        }

        @OnCommand("BASIC2\\?")
        public SCPICommand basic(SCPICommand in) {
            return SCPICommand.builder().command("BASIC2").with("PARAM", in.getParameter("PARAM").get()).build();
        }

        @OnCommand("C(?<id>[0-9]{1}):CMD")
        public SCPICommand channelCommand(int id) {
            return SCPICommand.builder().command("C" + id + ":CMD").with("PARAM", "VALUE").build();
        }

        @Override
        public void onReset() {

        }

        @Override
        public void onNotMapped(SCPICommand command) {
            throw new IllegalArgumentException();
        }

    }
    
}