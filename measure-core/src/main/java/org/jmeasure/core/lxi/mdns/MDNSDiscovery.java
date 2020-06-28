package org.jmeasure.core.lxi.mdns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import org.jmeasure.core.lxi.LXIDiscovery;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * MDNSDiscovery
 */
public class MDNSDiscovery implements LXIDiscovery {

    private int timeout = 5000;

	private List<MDNSServiceType> serviceTypes;
	
	public MDNSDiscovery() {
		this(MDNSServiceType.LXI);
	}

	public MDNSDiscovery(MDNSServiceType... types) {
        this(Arrays.asList(types));
    }

    public MDNSDiscovery(List<MDNSServiceType> types) {
        this.serviceTypes = new ArrayList<>(types);
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
            serviceTypes.forEach(type -> jmdns.addServiceListener(type.fqdn(), callback));

            Thread.sleep(timeout);

            return devices;
        } catch (InterruptedException e) {
            return null;
        }
    }

    
}