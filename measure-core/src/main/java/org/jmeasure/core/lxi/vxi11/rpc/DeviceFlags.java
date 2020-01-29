package org.jmeasure.core.lxi.vxi11.rpc;

import org.acplt.oncrpc.*;
import java.io.IOException;

public class DeviceFlags implements XdrAble {

    public int value;

    public DeviceFlags() {
    }

    public DeviceFlags(int value) {
        this.value = value;
    }

    public DeviceFlags(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        xdr.xdrEncodeInt(value);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        value = xdr.xdrDecodeInt();
    }

}