package org.jmeasure.core.lxi.raw;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;

/**
 * RawSocket implements a generic TCP/IP socket for sending and receiving byte streams
 */
public class RawSocket implements ISocket {

	private final String host;

	private final int port;

	private final int board;

	private transient Socket socket;

	/**
	 * Constructs a RawSocket, board number defaults to 0
	 * 
	 * @param host Hostname or IP address
	 * @param port Port number, 0-65535
	 * @throws IOException if the connection failed
	 */
	public RawSocket(String host, int port) throws IOException {
		this(host, port, 0);
	}

	/**
	 * Constructs a RawSocket
	 * 
	 * @param host Hostname or IP address
	 * @param port Port number
	 * @param board Board number
	 * @throws IOException if the connection failed
	 */
	public RawSocket(String host, int port, int board) throws IOException {
		if(board < 0) {
			throw new IllegalArgumentException("Board number must be positive: " + board);
		}
		this.host = host;
		this.port = port;
		this.board = board;

		socket = new Socket(host, port);
	}
	
	public int getBoard() {
		return board;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String getResourceString() {
		return String.format("TCP%d::%s::%d::SOCKET", board, host, port);
	}

	@Override
	public String toString() {
		return String.format("RawSocket[host=%s, port=%d, board=%d]", host, port, board);
	}

	@Override
	public void send(ByteBuffer message) throws IOException {
		if(!message.hasArray()) {
			throw new IOException("Message is empty.");
		}
		socket.getOutputStream().write(message.array());
	}

	@Override
	public ByteBuffer receive(final int count, final long timeout) throws IOException, TimeoutException {
		try {
			//TODO: restore previous timeout
			socket.setSoTimeout((int)timeout);
			ByteBuffer output = ByteBuffer.allocate(count);
			while(count > 0) {
				output.put((byte) socket.getInputStream().read());
			}
			return output;
		} catch(SocketTimeoutException e) {
			throw new TimeoutException(e.getMessage());
		} catch(IOException e) {
			this.socket = null;
			throw e;
		}
	}

	@Override
	public ByteBuffer receive(final char delimiter, final long timeout) throws IOException, TimeoutException {
		try {
			int previousTimeout = socket.getSoTimeout();
			socket.setSoTimeout((int)timeout);
			
			ArrayList<Byte> input = new ArrayList<>();
			byte in = (byte) socket.getInputStream().read();
			while(in != delimiter) {
				input.add(in);
				in = (byte) socket.getInputStream().read();
			}
			socket.setSoTimeout((int)previousTimeout);
			ByteBuffer bytes = ByteBuffer.allocate(input.size());
			for(byte b : input) {
				bytes.put(b);
			}
			return bytes;
		} catch(SocketException e) {
			throw new IOException(e);
		} catch(SocketTimeoutException e) {
			throw new TimeoutException(e.getMessage());
		}
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException | NullPointerException e) {
			//TODO: warn
		} finally {
			socket = null;
		}
	}

	@Override
	public boolean isConnected() {
		return socket != null && !socket.isClosed();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RawSocket) {
			RawSocket other = (RawSocket) obj;
			return Objects.equals(this.host, other.host) 
					&& Objects.equals(this.port, other.port)
					&& Objects.equals(this.board, other.board);
		}
		return false;
	}
	
}