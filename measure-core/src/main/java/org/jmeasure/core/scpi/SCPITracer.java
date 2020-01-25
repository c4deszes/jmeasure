package org.jmeasure.core.scpi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

/**
 * SCPITracer is an adapter class for any SCPISocket
 * 
 * <p>
 * It captures all SCPICommands and can log outbound messages so they can be replayed later
 * 
 * <p>
 * <b>You need to enable Trace level logging for this class for the consoleEnabled setting to work</b>
 */
@Slf4j
public class SCPITracer extends SCPISocketAdapter {

    private FileOutputStream outbound;

    private FileOutputStream inbound;

    private boolean consoleEnabled;

    public SCPITracer(SCPISocket adapter, boolean consoleEnabled, FileOutputStream inbound, FileOutputStream outbound) {
        super(adapter);
        this.consoleEnabled = consoleEnabled;
        this.outbound = outbound;
        this.inbound = inbound;
    }

    @Override
    public void connect() throws IOException {
        super.connect();
        this.log(super.getConnectionInfo() + " ~ Connected.");
    }

    @Override
    public void disconnect() {
        super.disconnect();
        this.log(super.getConnectionInfo() + " ~ Disconnected.");
    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    private void outbound(SCPICommand... commands) {
        try {
            String out = SCPISocket.concat(commands);
            this.log(super.getConnectionInfo() + " <- " + truncate(out, 30));
            outbound.write(out.getBytes());
        } catch(IOException e) {
            log.warn("Error writing outbound message.", e);
        }
    }

    private void inbound(SCPICommand command) {
        try {
            this.log(super.getConnectionInfo() + " <- " + truncate(command.getRaw(), 30));
            inbound.write(command.getRaw().getBytes());
        } catch (IOException e) {
            log.warn("Error writing inbound message.", e);
        }
    }

    private void log(String msg) {
        if(consoleEnabled) {
            log.trace(msg);
        }
    }

    private String truncate(String in, int length) {
        return in.length() > length ? in.substring(0, length) + "..." : in;
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        super.send(commands);
        this.outbound(commands);
    }

    @Override
    public Optional<SCPICommand> receive(long timeout) throws IOException {
        Optional<SCPICommand> response = super.receive(timeout);
        response.ifPresent(this::inbound);
        return response;
    }
    
}