package org.jmeasure.core.scpi.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;

public class RawSCPISocket implements ISCPISocket {

	public final static char DEFAULT_TERMINATION = '\n';

	public final static char DEFAULT_SEPARATOR = ';';

	private final char termination;

	private final char separator;

	private final ISocket socket;

	private final Charset charset;

	public RawSCPISocket(final ISocket socket) {
		this(socket, DEFAULT_TERMINATION, DEFAULT_SEPARATOR);
	}

	public RawSCPISocket(final ISocket socket, final char termination, final char separator) {
		this(socket, termination, separator, StandardCharsets.ISO_8859_1);
	}

	public RawSCPISocket(final ISocket socket, final char termination, final char separator, final Charset charset) {
		this.socket = socket;
		this.termination = termination;
		this.separator = separator;
		this.charset = charset;
	}

	@Override
	public void send(SCPICommand... commands) throws IOException {
		if(commands.length == 0) {
			return;
		}
		ByteBuffer output = ByteBuffer.wrap(ISCPISocket.concat(this.termination, this.separator, commands).getBytes(charset));
		socket.send(output);
	}

	@Override
	public Optional<String> receive(long timeout) throws IOException {
		try {
			ByteBuffer response = socket.receive(this.termination, timeout);
			return Optional.of(new String(response.array(), charset));
		} catch(TimeoutException e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<String> receive(int count, long timeout) throws IOException {
		try {
			ByteBuffer response = socket.receive(count, timeout);
			return Optional.of(new String(response.array(), charset));
		} catch(TimeoutException e) {
			return Optional.empty();
		}
	}

	@Override
	public void close() {
		socket.close();
	}

	@Override
	public boolean isConnected() {
		return socket.isConnected();
	}

}