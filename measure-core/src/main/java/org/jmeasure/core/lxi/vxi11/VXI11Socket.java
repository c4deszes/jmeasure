package org.jmeasure.core.lxi.vxi11;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;

/**
 * VXI11Socket
 */
public class VXI11Socket implements ISCPISocket {

    public VXI11Socket(String host, int port, String name, boolean lock, int timeout) {

    }

    @Override
    public Optional<String> receive(int count, long timeout) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<SCPICommand> receive(long timeout) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void connect() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getConnectionInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    
}