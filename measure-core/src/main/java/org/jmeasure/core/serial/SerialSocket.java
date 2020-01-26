package org.jmeasure.core.serial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;

/**
 * SerialSocket
 */
public class SerialSocket implements ISocket {

    @Override
    public void connect() throws IOException {
        
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

    @Override
    public void send(ByteBuffer message) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public ByteBuffer receive(int count, long timeout) throws IOException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuffer receive(char delimiter, long timeout) throws IOException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

    
}