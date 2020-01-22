package org.jmeasure.lxi;

import java.io.IOException;
import java.util.Optional;

/**
 * SCPISocketAdapter
 */
public abstract class SCPISocketAdapter implements SCPISocket {

    protected final SCPISocket adapter;

    public SCPISocketAdapter(final SCPISocket adapter) {
        this.adapter = adapter;
    }

    @Override
    public Optional<SCPICommand> receive(long timeout) throws IOException {
        return adapter.receive(timeout);
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        adapter.send(commands);
    }

    @Override
    public void connect() throws IOException {
        adapter.connect();
    }

    @Override
    public void disconnect() {
        adapter.disconnect();
    }

    @Override
    public boolean isConnected() {
        return adapter.isConnected();
    }

    
    
}