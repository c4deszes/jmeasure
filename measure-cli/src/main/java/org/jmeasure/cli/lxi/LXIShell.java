package org.jmeasure.cli.lxi;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.jmeasure.core.lxi.mdns.MDNSDiscovery;
import org.jmeasure.core.lxi.vxi11.VXI11Discovery;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@ShellCommandGroup("LXI")
public class LXIShell {

    @ShellMethod(key = "lxi discover", value = "Finds LXI instruments on the network")
    public String lxiDiscover(
        @ShellOption(value = {"-i", "--interface"}, defaultValue = "all", help = "Network interface to broadcast on") String networkInterface,
        @ShellOption(value = {"-p", "--protocol"}, defaultValue = ShellOption.NULL, help = "Discovery protocol (VXI-11 or mDNS)") DiscoveryProtocol protocol,
        @ShellOption(value = {"-t", "--timeout"}, defaultValue = "1000", help = "Time to wait for responses") int timeout,
        @ShellOption(value = {"-x", "--no-resolve"}, defaultValue = "false", help = "Disables automatic device identifier resolution") boolean noResolve) {
        
        Map<InetAddress, DeviceIdentifier> devices = new HashMap<>();

        VXI11Discovery vxi11 = new VXI11Discovery(timeout, !noResolve, 1);
        MDNSDiscovery mdns = new MDNSDiscovery(MDNSDiscovery.ServiceType.ALL);

        if(networkInterface.equals("all")) {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if(protocol == DiscoveryProtocol.VXI11) {
                    devices.putAll(vxi11.discover(ni));
                } else if(protocol == DiscoveryProtocol.mDNS) {
                    devices.putAll(mdns.discover(ni));
                } else if(protocol == null) {
                    devices.putAll(vxi11.discover(ni));
                    devices.putAll(mdns.discover(ni));
                }
            }
        }
        else {
            //discover(discovery, NetworkInterface.getByName(networkInterface), devices);
        }

        return toTable(devices);
    }

    private Map<InetAddress, DeviceIdentifier> discover(DiscoveryProtocol protocol, NetworkInterface networkInterface) {
        Map<InetAddress, DeviceIdentifier> devices = new HashMap<>();
        if(protocol == DiscoveryProtocol.VXI11) {
            devices.putAll(vxi11.discover(ni));
        } else if(protocol == DiscoveryProtocol.mDNS) {
            devices.putAll(mdns.discover(ni));
        } else if(protocol == null) {
            devices.putAll(vxi11.discover(ni));
            devices.putAll(mdns.discover(ni));
        }
        return devices;
    }

    private static String toTable(Map<InetAddress, DeviceIdentifier> devices) {
        StringBuilder builder = new StringBuilder();
        builder.append("Found " + devices.size() + " device(s):\n");
        devices.forEach((ip, dev) -> {
            builder.append(ip.getHostAddress() + "\t");
            builder.append(dev.getManufacturer() + "\t");
            builder.append(dev.getModel()+"\t");
            builder.append(dev.getSerialNumber() + "\t");
            builder.append(dev.getFirmwareVersion() + "\n");
        });
        if(builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private static enum DiscoveryProtocol {
        mDNS, VXI11
    }
}