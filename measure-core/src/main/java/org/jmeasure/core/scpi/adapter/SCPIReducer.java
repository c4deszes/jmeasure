package org.jmeasure.core.scpi.adapter;

import java.io.IOException;

import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.SCPISocketAdapter;

/**
 * SCPIReducer
 */
public class SCPIReducer extends SCPISocketAdapter {

    public SCPIReducer(ISCPISocket adapter) {
        super(adapter);
    }

    @Override
    public void send(SCPICommand... commands) throws IOException {
        //TODO: reduce
    }
    
}