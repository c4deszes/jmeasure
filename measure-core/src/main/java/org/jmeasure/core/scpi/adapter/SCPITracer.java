package org.jmeasure.core.scpi.adapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.SCPISocketAdapter;

/**
 * SCPITracer is an adapter class for any SCPISocket
 * 
 * <p>
 * It captures all SCPICommands and can log outbound messages so they can be replayed later
 * 
 * <p>
 * <b>You need to enable Trace level logging for this class for the consoleEnabled setting to work</b>
 */
public class SCPITracer extends SCPISocketAdapter {

    private OutputStream outbound;

    private OutputStream inbound;

    public SCPITracer(ISCPISocket adapter, OutputStream inbound, OutputStream outbound) {
        super(adapter);
        this.outbound = outbound;
        this.inbound = inbound;
    }

    @Override
    public void connect() throws IOException {
        super.connect();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    private void outbound(SCPICommand... commands) {
        try {
            String out = ISCPISocket.concat(';', ' ', commands);
            outbound.write(out.getBytes());
        } catch(IOException e) {
			//TODO: log
            //log.warn("Error writing outbound message.", e);
        }
    }

    private void inbound(String command) {
        try {
            inbound.write(command.getBytes());
        } catch (IOException e) {
			//TODO: log
            //log.warn("Error writing inbound message.", e);
        }
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        super.send(commands);
        this.outbound(commands);
    }

    @Override
    public Optional<String> receive(long timeout) throws IOException {
        Optional<String> response = super.receive(timeout);
        response.ifPresent(this::inbound);
        return response;
    }

    @Override
    public SCPITracer clone(ISCPISocket socket) {
        return new SCPITracer(socket, this.inbound, this.outbound);
    }
    
}