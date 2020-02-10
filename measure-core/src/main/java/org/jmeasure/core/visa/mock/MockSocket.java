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
    }

    @Override
    public void connect() {
        this.connected = true;
    }

    @Override
    public void disconnect() {
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public String getResourceString() {
        return "MOCK::" + className + "::" + instrumentName + "::INSTR";
    }

    @Override
    public Optional<String> getInstrumentName() {
        return Optional.ofNullable(this.instrumentName);
    }

    @Override
    public void send(ByteBuffer message) throws IOException {
        
    }

    @Override
    public ByteBuffer receive(int count, long timeout) throws IOException, TimeoutException {
        return ByteBuffer.allocate(0);
    }

    @Override
    public ByteBuffer receive(char delimiter, long timeout) throws IOException, TimeoutException {
        return ByteBuffer.allocate(0);
    }
    
}