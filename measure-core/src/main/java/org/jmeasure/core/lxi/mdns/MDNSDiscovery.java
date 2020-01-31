package org.jmeasure.core.lxi.mdns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;

import org.jmeasure.core.lxi.LXIDiscovery;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * MDNSDiscovery
 */
public class MDNSDiscovery implements LXIDiscovery {

    @Override
    public Map<InetAddress, DeviceIdentifier> discover(NetworkInterface networkInterface) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    
}