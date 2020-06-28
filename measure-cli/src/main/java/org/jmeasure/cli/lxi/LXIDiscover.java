package org.jmeasure.cli.lxi;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jmeasure.core.lxi.mdns.MDNSServiceType;
import org.jmeasure.core.lxi.vxi11.VXI11Discovery;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * LXICommand
 */
@Command(name = "lxi-discover")
public class LXIDiscover implements Callable<Integer> {

	@Option(names = {"--interface", "-i"}, description = "Network interface to discover on, if not specified then it will broadcast on all interfaces", required = false)
	NetworkInterface networkInterface;

	@ArgGroup(exclusive =  true)
	DiscoverySettings discoverySettings;

	class DiscoverySettings {
		@ArgGroup(exclusive = false)
		VXI11Settings vxi11Settings;

		@ArgGroup(exclusive = false)
		MDNSSettings mdnsSettings;
	}

	class VXI11Settings {
		@Option(names = {"--vxi11"}, description = "Enables VXI-11 based instrument discovery", defaultValue = "true", required = false)
		boolean vxi11DiscoveryEnabled;

		//Map<InetAddress, DeviceIdentifier> discover() {

		//}
	}

	class MDNSSettings {
		@Option(names = {"--mdns"}, description = "Enables mDNS based instrument discovery", defaultValue = "false", required = false)
		boolean mdnsDiscoveryEnabled;

		@Option(names = {"--mdns-services"}, description = "Specify mDNS service types to search", required = false)
		List<MDNSServiceType> mdnsServiceTypes = Collections.singletonList(MDNSServiceType.LXI);
	}

	@Option(names = {"--timeout", "-t"}, description = "Specify the timeout of a broadcast response in milliseconds", defaultValue = "1000", required = false)
	int timeout;

	@Override
	public Integer call() throws Exception {
		VXI11Discovery vxi11Discovery = new VXI11Discovery(timeout);

		Stream<NetworkInterface> interfaces;
		if(networkInterface == null) {
			//Not using NetworkInterface.networkInterfaces() as it's only introduced in Java 9
			interfaces = StreamSupport.stream(
							Spliterators.spliteratorUnknownSize(NetworkInterface.getNetworkInterfaces().asIterator(), 
																Spliterator.ORDERED), 
							false);
		}
		else {
			interfaces = Collections.singletonList(networkInterface).stream();
		}

		

		return 0;
	}
}