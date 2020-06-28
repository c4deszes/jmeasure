package org.jmeasure.test.context;

import org.jmeasure.core.lxi.mdns.MDNSDiscovery;
import org.jmeasure.core.lxi.mdns.MDNSServiceType;

/**
 * EnableLXIDiscovery
 */
public @interface EnableLXIDiscovery {

    /**
     * Enables mDNS based instrument discovery
     */
    boolean mDNSEnabled() default true;

    /**
     * Enables VXI-11 (RPC Portmap broadcast) based instrument discovery
     */
    boolean vxi11Enabled() default true;

    /**
     * mDNS service types
     */
    MDNSServiceType[] mdnsServiceTypes() default {MDNSServiceType.LXI};
    
}