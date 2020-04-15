package org.jmeasure.core.serial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

import org.jmeasure.core.device.ISocket;
import org.openmuc.jrxtx.DataBits;
import org.openmuc.jrxtx.FlowControl;
import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.SerialPort;
import org.openmuc.jrxtx.SerialPortBuilder;
import org.openmuc.jrxtx.StopBits;

/**
 * SerialSocket
 */
public class SerialSocket implements ISocket {

    private final String port;

    private int baudRate;

    private DataBits dataBits;

    private Parity parity;

    private StopBits stopBits;

    private FlowControl flowControl;

    private SerialPort socket;

    public SerialSocket(String port, int baudRate, DataBits dataBits, Parity parity, StopBits stopBits, FlowControl flowControl) {
        this.port = port;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.parity = parity;
        this.stopBits = stopBits;
        this.flowControl = flowControl;
    }

    public SerialSocket(String port, String params) {
        this.port = port;
        //TODO: extract
        //new SerialSocket("COM3", "9600/8/N/1");
    }

    public SerialSocket(String port, int baudRate) {
        this(port, baudRate, DataBits.DATABITS_8, Parity.NONE, StopBits.STOPBITS_1, FlowControl.NONE);
    }

    //public DataBits extract(String val)

    @Override
    public void connect() throws IOException {
        if(this.isConnected()) {
            return;
        }
        this.socket = SerialPortBuilder.newBuilder(port)
                                        .setBaudRate(baudRate)
                                        .setDataBits(dataBits)
                                        .setParity(parity)
                                        .setStopBits(stopBits)
                                        .setFlowControl(flowControl)
                                        .build();
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (NullPointerException | IOException e) {}
    }

    @Override
    public boolean isConnected() {
        return this.socket != null && !socket.isClosed();
    }

    @Override
    public String getInstrumentName() {
        return null;
    }

    @Override
    public String getResourceString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void send(ByteBuffer message) throws IOException {
        if(socket == null) {
            throw new IOException("Socket is closed.");
        }
        if(!message.hasArray()) {
            throw new IOException("Message empty.");
        }
        socket.getOutputStream().write(message.array());
    }

    @Override
    public ByteBuffer receive(int count, long timeout) throws IOException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuffer receive(char delimiter, long timeout) throws IOException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

    
}