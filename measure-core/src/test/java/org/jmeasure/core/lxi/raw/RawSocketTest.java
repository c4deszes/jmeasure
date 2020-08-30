package org.jmeasure.core.lxi.raw;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * RawSocketTest
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class RawSocketTest extends RawSocketBase {

    public RawSocketTest() {
        super(50000);
    }

    @Test
    public void testConstructorValid() throws IOException {
        RawSocket socket = new RawSocket("localhost", 50000, 0);
        assertEquals("localhost", socket.getHost());
        assertEquals(50000, socket.getPort());
        assertEquals(0, socket.getBoard());
        socket.close();
    }

    @Test
    public void testResourceString() throws IOException {
        RawSocket socket = new RawSocket("localhost", 50000, 0);
        assertEquals("TCP0::localhost::50000::SOCKET", socket.getResourceString());
        socket.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidPortNegative() throws IOException {
        RawSocket socket = new RawSocket("localhost", -1, 0);
        socket.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidPortTooHigh() throws IOException {
        RawSocket socket = new RawSocket("localhost", 65536, 0);
        socket.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidBoard() throws IOException {
        RawSocket socket = new RawSocket("localhost", 50000, -1);
        socket.close();
    }

    @Test
    public void testConnectionLifeCycle() throws IOException {
        RawSocket socket = new RawSocket("localhost", 50000, 0);
        assertEquals(true, socket.isConnected());

        socket.close();
        assertEquals(false, socket.isConnected());
    }

    @Test
    public void testConnectionIdempotent() throws IOException {
        RawSocket socket = new RawSocket("localhost", 50000, 0);
        assertEquals(true, socket.isConnected());

        socket.close();
        socket.close();
        assertEquals(false, socket.isConnected());
    }

}