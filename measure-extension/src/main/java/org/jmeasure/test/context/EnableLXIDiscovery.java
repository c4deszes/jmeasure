package org.jmeasure.test.context;

import org.jmeasure.core.lxi.mdns.MDNSDiscovery;
import org.jmeasure.core.lxi.mdns.MDNSDiscovery.ServiceType;

/**
 * EnableLXIDiscovery
 */
public @interface EnableLXIDiscovery {

    /**
     * Enables mDNS based instrument discovery, by default {@code true}
     */
    boolean mDNSEnabled() default true;

    /**
     * Enables VXI-11 (RPC Portmap broadcast) based instrument discovery, by default {@code true}
     */
    boolean vxi11Enabled() default true;

    /**
     * mDNS service types
     */
    MDNSDiscovery.ServiceType[] mdnsServiceTypes() default {ServiceType.LXI};
    
}