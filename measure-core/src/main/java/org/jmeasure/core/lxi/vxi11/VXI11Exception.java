package org.jmeasure.core.lxi.vxi11;

import java.io.IOException;

import org.jmeasure.core.lxi.vxi11.VXI11.ErrorCode;

/**
 * VXI11Exception
 */
public class VXI11Exception extends IOException {

    public VXI11Exception() {
    }

    public VXI11Exception(int code) {
        super("VXI11 Error: " + ErrorCode.getErrorString(code));
    }

    public VXI11Exception(String message) {
        super(message);
    }

    public VXI11Exception(Throwable cause) {
        super(cause);
    }

    public VXI11Exception(String message, Throwable cause) {
        super(message, cause);
    }

}