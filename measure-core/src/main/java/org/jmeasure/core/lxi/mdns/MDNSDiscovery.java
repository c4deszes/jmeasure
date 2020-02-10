package org.jmeasure.core.lxi.mdns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import org.jmeasure.core.lxi.LXIDiscovery;
import org.jmeasure.core.visa.DeviceIdentifier;

import lombok.Getter;

/**
 * MDNSDiscovery
 */
public class MDNSDiscovery implements LXIDiscovery {

    public static enum ServiceType {
        LXI("_lxi._tcp.local"), 
        HTTP("_http._tcp.local"), 
        HISLIP("_hislip._tcp.local"), 
        SCPI_RAW("_scpi-raw._tcp.local"), 
        SCPI_TELNET("_scpi-telnet._tcp.local"), 
        VXI11("_vxi11._tcp.local");

        @Getter
        private String type;

        public final static ServiceType[] ALL = ServiceType.values();
        
        private ServiceType(String type) {
            this.type = type;
        }

    }

    private int timeout = 5000;

    private List<ServiceType> serviceTypes;

    public MDNSDiscovery(List<ServiceType> types) {
        this.serviceTypes = new ArrayList<>(types);
    }

    public MDNSDiscovery(ServiceType... types) {
        this(Arrays.asList(types));
    }

    @Override
    public Map<InetAddress, DeviceIdentifier> discover(NetworkInterface networkInterface) throws IOException {
        try(JmDNS jmdns = JmDNS.create(networkInterface.getInterfaceAddresses().get(0).getAddress(), "TestControl")) {
            Map<InetAddress, DeviceIdentifier> devices = new HashMap<>();
            ServiceListener callback = new ServiceListener(){
            
                @Override
                public void serviceResolved(ServiceEvent event) {
                    //TODO: select inetaddress
                    //devices.put(event.getInfo().getInetAddresses()[0], DeviceIdentifier.UNKNOWN);
                }
            
                @Override
                public void serviceRemoved(ServiceEvent event) {
                    //
                }
            
                @Override
                public void serviceAdded(ServiceEvent event) {
                    //
                }
            };
            serviceTypes.forEach(type -> jmdns.addServiceListener(type.getType(), callback));

            Thread.sleep(timeout);

            return devices;
        } catch (InterruptedException e) {
            return null;
        }
    }

    
}