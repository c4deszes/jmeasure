package org.jmeasure.core.scpi;

import java.io.IOException;

import org.jmeasure.core.visa.DeviceIdentifier;
import org.jmeasure.core.visa.UnsupportedDeviceException;

/**
 * InstrumentFactory
 */
public interface ISCPIDeviceFactory {

    /**
     * Returns true if the factory can create an instance of the given device
     * 
     * This method shouldn't try to create the given instance
     * 
     * @param info SCPI Device information
     * @return {@code true} if the factory is able to create a device
     */
    boolean supports(DeviceIdentifier info);

    /**
     * Returns an instance of the given device and attaches the SCPI socket to that instance
     * 
     * **Note: it's not a requirement to have this method return without an exception if support returned true**
     * Also this method shouldn't check if the device is supported
     * 
     * @param <T> Returned instance should be cast to this type
     * @param socket SCPI Socket
     * @param info SCPI Device information
     * @return SCPIDevice 
     */
    SCPIDevice create(ISCPISocket socket, DeviceIdentifier info) throws IOException, UnsupportedDeviceException;
    
}