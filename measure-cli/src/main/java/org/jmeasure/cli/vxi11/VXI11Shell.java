package org.jmeasure.cli.vxi11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.jmeasure.core.lxi.vxi11.VXI11Discovery;
import org.jmeasure.core.visa.DeviceIdentifier;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import lombok.extern.slf4j.Slf4j;

/**
 * VXI11Shell
 */
@Slf4j
@ShellComponent
@ShellCommandGroup("VXI11")
public class VXI11Shell {

    @ShellMethod(key = "vxi11 discover", value="Discovers LXI devices")
    public String vxi11discover(
        @ShellOption(value = {"-t", "--timeout"}, defaultValue = "1000") int timeout,
        @ShellOption(value = {"-r", "--retry"}, defaultValue = "0") int retransmissions,
        @ShellOption(value = "--no-resolve", defaultValue = "false") boolean noResolve,
        @ShellOption(value = {"-i", "--interface"}, defaultValue = "all") String networkInterface) {

        try {
            VXI11Discovery discovery = new VXI11Discovery(timeout, !noResolve, retransmissions);
            Map<InetAddress, DeviceIdentifier> devices = new HashMap<>();
            if(networkInterface.equals("all")) {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while(interfaces.hasMoreElements()) {
                    discover(discovery, interfaces.nextElement(), devices);
                }
            }
            else {
                discover(discovery, NetworkInterface.getByName(networkInterface), devices);
            }
            
            StringBuilder builder = new StringBuilder();
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
        } catch(IOException e) {
            return "error: " + e.getMessage();
        }
    }

    private void discover(VXI11Discovery discovery, NetworkInterface networkInterface, Map<InetAddress, DeviceIdentifier> devices) {
        try {
            devices.putAll(discovery.discover(networkInterface));
        } catch(IOException e) {
            log.warn("error during discovery: " + e.getMessage());
        }
    }

}