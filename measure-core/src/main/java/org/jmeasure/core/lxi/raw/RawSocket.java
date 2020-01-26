package org.jmeasure.core.lxi.raw;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;

import lombok.Getter;

/**
 * RawSocket
 */
public class RawSocket implements ISocket {

    @Getter
    private final String host;

    @Getter
    private final int port;

    @Getter
    private final int board;

    private Socket socket;

    public RawSocket(String host, int port) {
        this(host, port, 0);
    }

    public RawSocket(String host, int port, int board) {
        this.host = host;
        this.port = port;
        this.board = board;
    }

    @Override
    public String getConnectionInfo() {
        return String.format("TCP%d::%s::%d::SOCKET", board, host, port);
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
            socket.setSoTimeout((int)timeout);
            ByteBuffer output = ByteBuffer.allocate(count);
            while(count > 0) {
                output.put((byte) socket.getInputStream().read());
            }
            return output;
        } catch(SocketException e) {
            throw new IOException(e);
        } catch(SocketTimeoutException e) {
            throw new TimeoutException(e.getMessage());
        }
    }

    @Override
    public ByteBuffer receive(final char delimiter, final long timeout) throws IOException, TimeoutException {
        try {
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
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException | NullPointerException e) {}
    }

    @Override
    public boolean isConnected() {
        return !socket.isClosed();
    }
    
}