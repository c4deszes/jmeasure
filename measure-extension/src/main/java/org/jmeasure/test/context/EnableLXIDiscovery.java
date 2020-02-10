package org.jmeasure.test.context;

/**
 * EnableLXIDiscovery
 */
public @interface EnableLXIDiscovery {

    boolean mDNSEnabled() default true;

    boolean vxi11Enabled() default true;
    
}