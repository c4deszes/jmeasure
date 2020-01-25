package org.jmeasure.core.scpi;

import java.io.IOException;
import java.util.Optional;

/**
 * SCPISocketAdapter
 */
public abstract class SCPISocketAdapter implements ISCPISocket {

    private final ISCPISocket adapter;

    public SCPISocketAdapter(final ISCPISocket adapter) {
        this.adapter = adapter;
    }

    public String getConnectionInfo() {
        return this.adapter.getConnectionInfo();
    }

    @Override
    public Optional<SCPICommand> receive(long timeout) throws IOException {
        return adapter.receive(timeout);
    }

    @Override
    public Optional<String> receive(int count, long timeout) throws IOException {
        return adapter.receive(count, timeout);
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