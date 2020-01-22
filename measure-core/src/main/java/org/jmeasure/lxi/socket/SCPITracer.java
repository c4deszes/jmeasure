package org.jmeasure.lxi.socket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPISocket;
import org.jmeasure.lxi.SCPISocketAdapter;

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
        adapter.connect();
        this.log(this.adapter + " ~ Connected.");
    }

    @Override
    public void disconnect() {
        adapter.disconnect();
        this.log(this.adapter + " ~ Disconnected.");
    }

    @Override
    public boolean isConnected() {
        return adapter.isConnected();
    }

    private void outbound(SCPICommand... commands) {
        try {
            String out = SCPISocket.concat(commands);
            this.log(this.adapter + " <- " + truncate(out, 30));
            outbound.write(out.getBytes());
        } catch(IOException e) {
            log.warn("Error writing outbound message.", e);
        }
    }

    private void inbound(SCPICommand command) {
        try {
            this.log(this.adapter + " <- " + truncate(command.getRaw(), 30));
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
        this.outbound(commands);
        adapter.send(commands);
    }

    @Override
    public Optional<SCPICommand> receive(long timeout) throws IOException {
        Optional<SCPICommand> response = adapter.receive(timeout);
        response.ifPresent(this::inbound);
        return response;
    }
    
}