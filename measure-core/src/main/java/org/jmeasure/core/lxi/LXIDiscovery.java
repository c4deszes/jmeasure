package org.jmeasure.core.lxi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;

import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * LXIDiscovery
 */
public interface LXIDiscovery {

    Map<InetAddress, DeviceIdentifier> discover(NetworkInterface networkInterface) throws IOException;
    
}