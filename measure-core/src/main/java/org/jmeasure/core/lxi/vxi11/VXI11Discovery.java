package org.jmeasure.core.lxi.vxi11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.acplt.oncrpc.OncRpcBroadcastEvent;
import org.acplt.oncrpc.OncRpcBroadcastListener;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcProtocols;
import org.acplt.oncrpc.OncRpcUdpClient;

import org.jmeasure.core.lxi.LXIDiscovery;
import org.jmeasure.core.lxi.vxi11.portmap.Portmap;
import org.jmeasure.core.lxi.vxi11.portmap.PortmapParams;
import org.jmeasure.core.lxi.vxi11.portmap.PortmapResponse;
import org.jmeasure.core.scpi.socket.RawSCPISocket;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * VXI11Discovery implements RPC based instrument discovery
 * 
 * <p>
 * This implementation broadcasts RPC Portmap queries over UDP.
 * The portmap parameters specifically look for the existence of the VXI-11 Device Core program over TCP.
 */
public class VXI11Discovery implements LXIDiscovery {

    private int timeout;

    private boolean resolveDeviceInfo;

    private int retransmissions;

    /**
     * Constructs a new VXI-11 discovery tool using the default parameters, which are:
     * <ul>
     * <li>
     * 1 second timeout (as stated in the LXI specification)
     * <li>
     * Automatically resolve device identifiers
     * <li>
     * Retransmit 1 time
     */
    public VXI11Discovery() {
        this(1000, true, 1);
	}
	
	public VXI11Discovery(int timeout) {
		this(timeout, true, 1);
	}

    /**
     * Constructs a new VXI-11 discovery using the given parameters
     * @param timeout Time to wait for responses
     * @param resolveDeviceInfo if {@code true} then for every response it will try to do an *IDN? query
     * @param retransmissions The number of retranmissions
     */
    public VXI11Discovery(int timeout, boolean resolveDeviceInfo, int retransmissions) {
        this.timeout = timeout;
        this.resolveDeviceInfo = resolveDeviceInfo;
        this.retransmissions = retransmissions;
    }

    @Override
    public Map<InetAddress, DeviceIdentifier> discover(NetworkInterface networkInterface) throws IOException {
        try {
            InetAddress bcast = getBroadcastAddress(networkInterface);
            OncRpcUdpClient rpcClient = (OncRpcUdpClient) OncRpcUdpClient.newOncRpcClient(bcast, Portmap.PROGRAM, Portmap.VERSION, Portmap.PORT, OncRpcProtocols.ONCRPC_UDP);
            rpcClient.setTimeout(timeout);

            Map<InetAddress, DeviceIdentifier> devices = new HashMap<>();
            OncRpcBroadcastListener callback = new OncRpcBroadcastListener(){
                @Override
                public void replyReceived(OncRpcBroadcastEvent evt) {
                    devices.put(evt.getReplyAddress(), DeviceIdentifier.UNKNOWN);
                }
            };

            PortmapResponse response = new PortmapResponse();

            for(int i = 0; i < retransmissions + 1; i++) {
                rpcClient.broadcastCall(Portmap.PROC_GETPORT, new PortmapParams(VXI11.DeviceCore.PROGRAM, VXI11.DeviceCore.VERSION, OncRpcProtocols.ONCRPC_TCP, 4), response, callback);
            }

            if(resolveDeviceInfo) {
                devices.replaceAll((ip, dev) -> {
                    try(RawSCPISocket socket = new RawSCPISocket(new VXI11Socket(ip, "inst0"))) {
                        return socket.getDeviceIdentifier();
                    } catch(IOException e) {
						//TODO: log warn
                        //log.warn("Failed to resolve device identifier of " + ip);
                        return dev;
                    }
                });
            }

            return devices;
        } catch(OncRpcException e) {
            throw new IOException(e);
        } catch(IllegalArgumentException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * Returns a broadcast address for the given network interface
     * @param networkInterface Network interface
     * @return Broadcast IP Address
     * @throws SocketException If the network interface is unavailable
     */
    private static InetAddress getBroadcastAddress(NetworkInterface networkInterface) throws SocketException {
        if (!networkInterface.isUp() || networkInterface.isVirtual()) {
            throw new IllegalArgumentException("Network interface unavailable or is virtual.");
        }
        Optional<InterfaceAddress> address = networkInterface.getInterfaceAddresses().stream()
                                                            .filter(addr -> addr.getBroadcast() != null).findFirst();
        return address.orElseThrow(() -> new IllegalArgumentException("No broadcast address found.")).getBroadcast();
    }

}