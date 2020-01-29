package org.jmeasure.core.lxi.vxi11.rpc;

import org.acplt.oncrpc.*;
import java.io.IOException;

public class DeviceReadResponse implements XdrAble {
    public DeviceErrorCode error;
    public int reason;
    public byte [] data;

    public DeviceReadResponse() {
    }

    public DeviceReadResponse(XdrDecodingStream xdr) throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        error.xdrEncode(xdr);
        xdr.xdrEncodeInt(reason);
        xdr.xdrEncodeDynamicOpaque(data);
    }

    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        error = new DeviceErrorCode(xdr);
        reason = xdr.xdrDecodeInt();
        data = xdr.xdrDecodeDynamicOpaque();
    }

}