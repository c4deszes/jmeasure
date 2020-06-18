package org.jmeasure.core.lxi.raw;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;

import lombok.Getter;

/**
 * RawSocket implements a generic TCP/IP socket for sending and receiving byte streams
 */
public class RawSocket implements ISocket {

    //TODO: remove getters from library code
    @Getter
    private final String host;

    @Getter
    private final int port;

    @Getter
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
     * @param port Port number, 0-65535
     * @param board Board number
     * @throws IOException if the connection failed
     */
    public RawSocket(String host, int port, int board) throws IOException {
        if(port > 65535 || port < 0) {
            throw new IllegalArgumentException("TCP Port number must be 0-65535: " + port);
        }
        if(board < 0) {
            throw new IllegalArgumentException("Board number must be positive: " + board);
        }
        this.host = host;
        this.port = port;
        this.board = board;

        this.connect();
    }

    @Override
    public String getInstrumentName() {
        return null;
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
    public void connect() throws IOException {
        if(socket != null) {
            return;
        }
        socket = new Socket(host, port);
        //TODO: receive buffer size
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
            //TODO: restore previous timeout
            socket.setSoTimeout((int)timeout);
            //TODO: rewrite to use ArrayList
            ByteBuffer output = ByteBuffer.allocate(socket.getReceiveBufferSize());
            byte in = (byte) socket.getInputStream().read();
            while(in != delimiter) {
                output.put(in);
                in = (byte) socket.getInputStream().read();
            }
            return ByteBuffer.wrap(output.array(), 0, output.position());
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