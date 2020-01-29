package org.jmeasure.core.scpi.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;

public class RawSCPISocket implements ISCPISocket {

    public final static String DEFAULT_TERMINATION = "\n";

    private final String termination;

    private final ISocket socket;

    public RawSCPISocket(final ISocket socket) {
        this(socket, DEFAULT_TERMINATION);
    }

    public RawSCPISocket(final ISocket socket, final String termination) {
        this.socket = socket;
        this.termination = termination;
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        if(commands.length == 0) {
            return;
        }
        ByteBuffer output = ByteBuffer.wrap(ISCPISocket.concat(this.termination, commands).getBytes(StandardCharsets.ISO_8859_1));
        socket.send(output);
    }

    @Override
    public Optional<SCPICommand> receive(long timeout) throws IOException {
        try {
            ByteBuffer response = socket.receive('\n', timeout);
            return Optional.of(new SCPICommand(new String(response.array(), StandardCharsets.ISO_8859_1)));
        } catch(TimeoutException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> receive(int count, long timeout) throws IOException {
        try {
            ByteBuffer response = socket.receive(count, timeout);
            return Optional.of(new String(response.array(), StandardCharsets.ISO_8859_1));
        } catch(TimeoutException e) {
            return Optional.empty();
        }
    }

    @Override
    public void connect() throws IOException {
        socket.connect();
    }

    @Override
    public void disconnect() {
        socket.disconnect();
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public String getResourceString() {
        return socket.getResourceString();
    }

}