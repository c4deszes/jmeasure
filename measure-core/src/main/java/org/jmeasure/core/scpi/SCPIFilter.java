package org.jmeasure.core.scpi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * SCPIFilter
 */
public class SCPIFilter extends SCPISocketAdapter {

    private Predicate<SCPICommand> allow;

    public SCPIFilter(ISCPISocket adapter, Predicate<SCPICommand> allow) {
        super(adapter);
        this.allow = allow;
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        ArrayList<SCPICommand> filtered = new ArrayList<>();
        Stream.of(commands).filter(allow).forEach(filtered::add);
        SCPICommand[] out = new SCPICommand[filtered.size()];
        out = filtered.toArray(out);
        super.send(out);
    }
    
}