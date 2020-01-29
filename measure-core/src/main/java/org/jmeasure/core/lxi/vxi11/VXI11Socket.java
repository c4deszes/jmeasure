package org.jmeasure.core.lxi.vxi11;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.acplt.oncrpc.OncRpcClient;
import org.acplt.oncrpc.OncRpcClientStub;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcProtocols;
import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.lxi.vxi11.rpc.CreateLinkParams;
import org.jmeasure.core.lxi.vxi11.rpc.CreateLinkResponse;
import org.jmeasure.core.lxi.vxi11.rpc.DeviceError;
import org.jmeasure.core.lxi.vxi11.rpc.DeviceFlags;
import org.jmeasure.core.lxi.vxi11.rpc.DeviceLink;
import org.jmeasure.core.lxi.vxi11.rpc.DeviceReadParams;
import org.jmeasure.core.lxi.vxi11.rpc.DeviceReadResponse;
import org.jmeasure.core.lxi.vxi11.rpc.DeviceWriteParams;
import org.jmeasure.core.lxi.vxi11.rpc.DeviceWriteResponse;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;

/**
 * VXI11Socket
 */
public class VXI11Socket implements ISocket {

    public final static int CLIENT_ID = 1234567;

    private final String host;

    private final String name;

    private boolean lock;

    private int lockTimeout;

    private transient OncRpcClient client;

    private transient CreateLinkResponse link;

    public VXI11Socket(final String host) {
        this(host, "inst0", false, 0);
    }

    public VXI11Socket(final String host, final String name, boolean lock, int lockTimeout) {
        this.host = host;
        this.name = name;
        this.lock = lock;
        this.lockTimeout = lockTimeout;
    }

    @Override
    public void connect() throws IOException {
        try {
            client = OncRpcClient.newOncRpcClient(InetAddress.getByName(host), 
                                                    VXI11.DeviceCore.PROGRAM,
                                                    VXI11.DeviceCore.VERSION, 
                                                    0, 
                                                    OncRpcProtocols.ONCRPC_TCP);
            link = createLink(CLIENT_ID, lock, lockTimeout, name);
        } catch (OncRpcException e) {
            client = null;
            throw new IOException(e);
        }
    }

    public final CreateLinkResponse createLink(int clientId, boolean lockDevice, int lockTimeout, String device) throws OncRpcException {
        CreateLinkResponse response = new CreateLinkResponse();
        client.call(VXI11.DeviceCore.CREATE_LINK, VXI11.DeviceCore.VERSION, new CreateLinkParams(clientId, lockDevice, lockTimeout, device), response);
        return response;
    }

    public final DeviceWriteResponse write(int linkId, int ioTimeout, int lockTimeout, int flags, byte... data) throws OncRpcException {
        DeviceWriteParams params = new DeviceWriteParams(new DeviceLink(linkId), ioTimeout, lockTimeout, new DeviceFlags(flags), data);
        DeviceWriteResponse response = new DeviceWriteResponse();
        client.call(VXI11.DeviceCore.DEVICE_WRITE, VXI11.DeviceCore.VERSION, params, response);
        return response;
    }

    public final DeviceReadResponse read(int linkId, int size, int ioTimeout, int lockTimeout, int flags, byte termination) throws OncRpcException {
        DeviceReadParams params = new DeviceReadParams(new DeviceLink(linkId), size, ioTimeout, lockTimeout, new DeviceFlags(flags), termination);
        DeviceReadResponse response = new DeviceReadResponse();
        client.call(VXI11.DeviceCore.DEVICE_READ, VXI11.DeviceCore.VERSION, params, response);
        return response;
    }

    public final DeviceError destroyLink(int linkId) throws OncRpcException {
        DeviceError error = new DeviceError();
        client.call(VXI11.DeviceCore.DESTROY_LINK, VXI11.DeviceCore.VERSION, new DeviceLink(linkId), error);
        return error;
    }

    @Override
    public void disconnect() {
        try {
            destroyLink(link.lid.value);
            client.close();
        } catch(OncRpcException e) {
            //log warning
        } finally {
            client = null;
        }
    }

    @Override
    public boolean isConnected() {
        return client != null;
    }

    @Override
    public String getResourceString() {
        return "TCPIP::" + this.host + "::" + this.name;
    }

    @Override
    public void send(ByteBuffer message) throws IOException {
        if(!message.hasArray()) {
            throw new IOException("Message empty.");
        }
    }

    @Override
    public ByteBuffer receive(int count, long timeout) throws IOException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuffer receive(char delimiter, long timeout) throws IOException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }
    
}