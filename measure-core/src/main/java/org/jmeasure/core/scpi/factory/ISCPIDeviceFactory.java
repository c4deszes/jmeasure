package org.jmeasure.core.scpi.factory;

import java.io.IOException;

import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.UnsupportedDeviceException;

/**
 * InstrumentFactory
 */
public interface ISCPIDeviceFactory {

    boolean supports(DeviceIdentifier info);
    
    ISCPISocket create(ISCPISocket socket, DeviceIdentifier info) throws IOException, UnsupportedDeviceException;
    
}