package com.sample;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmeasure.core.lxi.LXIDiscovery;
import org.jmeasure.core.lxi.vxi11.VXI11Discovery;

/**
 * This sample implements the 'lxi discover' command found in {@link lxi-tools https://github.com/lxi-tools/lxi-tools}
 * although only using VXI-11
 */
public class DeviceDiscovery {

	public static void main(String[] args) throws IOException {

		//Initialize tool with 1 second timeout, 1 retransmission and automatic identifier resolution
		LXIDiscovery lxitool = new VXI11Discovery(true, 1, VXI11Discovery.DEFAULT_TIMEOUT);

		AtomicInteger deviceCount = new AtomicInteger(0);

		System.out.println("Searching for LXI devices - please wait...");
		System.out.println();

		//Run discovery on each interface
		for(Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces(); ni.hasMoreElements();) {
			NetworkInterface networkInterface = ni.nextElement();
			if(networkInterface.isVirtual() || !networkInterface.isUp()) {
				continue;
			}
			System.out.println("Broadcasting on interface " + networkInterface.getName());
			lxitool.discover(networkInterface)
					.stream()
					.peek(endpoint -> deviceCount.incrementAndGet())
					.forEach(endpoint -> {
						System.out.println("\tFound \"" + endpoint.getDeviceIdentifier().value() + "\" on address " + endpoint.getHost());
					});
		}

		System.out.println();
		System.out.println("Found " + deviceCount.get() + " devices");
	}
}