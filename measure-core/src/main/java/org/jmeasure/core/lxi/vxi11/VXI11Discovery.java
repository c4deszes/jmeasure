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
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * LXIDiscovery
 */
public class VXI11Discovery implements LXIDiscovery {

    private final static int DEFAULT_TIMEOUT = 1000;

    private final int timeout;

    private final boolean resolveDeviceInfo;

    public VXI11Discovery() {
        this(DEFAULT_TIMEOUT, true);
    }

    public VXI11Discovery(int timeout, boolean resolveDeviceInfo) {
        this.timeout = timeout;
        this.resolveDeviceInfo = resolveDeviceInfo;
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
                    devices.put(evt.getReplyAddress(), DeviceIdentifier.from("-,-,-,-"));
                }
            };

            PortmapResponse response = new PortmapResponse();
            rpcClient.broadcastCall(Portmap.PROC_GETPORT, new PortmapParams(VXI11.DeviceCore.PROGRAM, VXI11.DeviceCore.VERSION, OncRpcProtocols.ONCRPC_TCP, 4), response, callback);
            
            

            return devices;
        } catch(OncRpcException e) {
            throw new IOException(e);
        } catch(IllegalArgumentException e) {
            return Collections.emptyMap();
        }
    }

    private static InetAddress getBroadcastAddress(NetworkInterface networkInterface) throws SocketException {
        if (!networkInterface.isUp() || networkInterface.isVirtual()) {
            throw new IllegalArgumentException("No broadcast address found.");
        }
        Optional<InterfaceAddress> address = networkInterface.getInterfaceAddresses().stream()
                .filter(addr -> addr.getBroadcast() != null).findFirst();
        return address.orElseThrow(() -> new IllegalArgumentException("No broadcast address found.")).getBroadcast();
    }

}