package org.jmeasure.lxi.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPISocket;
import org.jmeasure.lxi.SCPISocketAdapter;

/**
 * SCPIFilter
 */
public class SCPIFilter extends SCPISocketAdapter {

    private Predicate<SCPICommand> allow;

    public SCPIFilter(SCPISocket adapter, Predicate<SCPICommand> allow) {
        super(adapter);
        this.allow = allow;
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        ArrayList<SCPICommand> filtered = new ArrayList<>();
        Stream.of(commands).filter(allow).forEach(filtered::add);
        SCPICommand[] out = new SCPICommand[filtered.size()];
        out = filtered.toArray(out);
        this.send(out);
    }
    
}