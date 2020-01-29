package org.jmeasure.core.lxi.vxi11.portmap;

import java.io.IOException;

import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.XdrAble;
import org.acplt.oncrpc.XdrDecodingStream;
import org.acplt.oncrpc.XdrEncodingStream;

import lombok.Getter;

/**
 * PortmapResponse
 */
public class PortmapResponse implements XdrAble {

    @Getter
    private int port;

    public void xdrEncode(XdrEncodingStream xdr) throws OncRpcException, IOException {
        xdr.xdrEncodeInt(port);
    }

    public void xdrDecode(XdrDecodingStream xdr) throws OncRpcException, IOException {
        port = xdr.xdrDecodeInt();
    }
}