package org.jmeasure.core.lxi.raw;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;

/**
 * RawSocketBase
 */
public class RawSocketBase {

    private final int port;

    private ServerSocket server;

    public RawSocketBase(int port) {
        this.port = port;
    }

    @Before
    public void setupServer() throws IOException {
        server = new ServerSocket(port);

        Thread accept = new Thread(() -> {
            try {
                server.accept();
            } catch (IOException e) {}
        });
        accept.start();
    }

    @After
    public void teardownServer() throws IOException {
        server.close();
    }
}