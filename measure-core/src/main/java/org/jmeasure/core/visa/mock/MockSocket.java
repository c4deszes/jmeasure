package org.jmeasure.core.visa.mock;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;

import lombok.Getter;

/**
 * VirtualSocket
 */
public class MockSocket implements ISocket {

    private boolean connected;

    @Getter
    private String className;

    private String instrumentName;

    public MockSocket(String className) {
        this(className, null);
    }

    public MockSocket(String className, String instrumentName) {
        this.className = className;
        this.instrumentName = instrumentName;

        this.connect();
    }

    @Override
    public void connect() {
        this.connected = true;
    }

    @Override
    public void close() {
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public String getResourceString() {
        return "MOCK::" + className + (instrumentName != null ? "::" + instrumentName: "") + "::INSTR";
    }

    @Override
    public String getInstrumentName() {
        return this.instrumentName;
    }

    @Override
    public void send(ByteBuffer message) throws IOException {
        if(!this.connected) {
            throw new IOException("MockSocket not connected");
        }
    }

    @Override
    public ByteBuffer receive(int count, long timeout) throws IOException, TimeoutException {
        if(!this.connected) {
            throw new IOException("MockSocket not connected");
        }
        return ByteBuffer.allocate(0);
    }

    @Override
    public ByteBuffer receive(char delimiter, long timeout) throws IOException, TimeoutException {
        if(!this.connected) {
            throw new IOException("MockSocket not connected");
        }
        return ByteBuffer.allocate(0);
    }
    
}