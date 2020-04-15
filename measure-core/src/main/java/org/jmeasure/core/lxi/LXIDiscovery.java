package org.jmeasure.core.lxi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;

import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * LXIDiscovery is the generic interface for discovering LXI devices on the network
 */
public interface LXIDiscovery {

    /**
     * Returns LXI devices found through the given network interface
     * 
     * @param networkInterface Network Interface
     * @return Mapping of IP addresses to Device identifiers, if the identifier cannot or shouldn't be resolved 
     * then it should default to {@code DeviceIdentifier.UNKNOWN}
     * @throws IOException If there was an error during device discovery
     */
    Map<InetAddress, DeviceIdentifier> discover(NetworkInterface networkInterface) throws IOException;
    
}