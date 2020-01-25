package org.jmeasure.core.scpi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;

class SCPISocket implements ISCPISocket {

    private final ISocket socket;

    public SCPISocket(final ISocket socket) {
        this.socket = socket;
    }
    
    public final static String concat(SCPICommand... commands) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<commands.length;i++) {
			builder.append(commands[i].toString());
			if(i != commands.length - 1) {
				builder.append(";");
			}
		}
		builder.append('\n');
		return builder.toString();
	}

    @Override
    public void send(SCPICommand... commands) throws IOException {
        if(commands.length == 0) {
            return;
        }
        ByteBuffer output = ByteBuffer.wrap(concat(commands).getBytes(StandardCharsets.ISO_8859_1));
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
    public String getConnectionInfo() {
        return socket.getConnectionInfo();
    }

}