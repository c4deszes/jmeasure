package org.jmeasure.core.lxi.vxi11;

/**
 * VXI11
 */
public class VXI11 {

    public static class DeviceCore {
        public final static int PROGRAM = 0x0607AF;
        public final static int VERSION = 1;

        //public final static int GETPORT = 3;

        public final static int CREATE_LINK = 10;
        public final static int DEVICE_WRITE = 11;
        public final static int DEVICE_READ = 12;
        public final static int DEVICE_READSTB = 13;
        public final static int DEVICE_TRIGGER = 14;
        public final static int DEVICE_CLEAR = 15;
        public final static int DEVICE_REMOTE = 16;
        public final static int DEVICE_LOCAL = 17;
        public final static int DEVICE_LOCK = 18;
        public final static int DEVICE_UNLOCK = 19;
        public final static int DEVICE_ENABLE_SRQ = 20;
        public final static int DEVICE_DOCMD = 22;
        public final static int DESTROY_LINK = 23;
        public final static int CREATE_INTR_CHANNEL = 25;
        public final static int DESTROY_INTR_CHANNEL = 26;
    }

    public static class DeviceInterrupt {
        public final static int PROGRAM = 0x0607B1;
        public final static int VERSION = 1;

        public final static int DEVICE_INTR_SRQ = 30;
    }

    public static class DeviceAsync {
        public final static int PROGRAM = 0x0607B0;
        public final static int VERSION = 1;

        public final static int DEVICE_ABORT = 1;
    }

    /**
     * Found in VXI-11 Rev 1.0 specification Table B.2
     */
    public static class ErrorCode {
        public final static int NO_ERROR = 0;
        public final static int SYNTAX_ERROR = 2;
        public final static int DEVICE_NOT_ACCESSIBLE = 3;
        public final static int INVALID_LINK_IDENTIFIER = 4;
        public final static int PARAMETER_ERROR = 5;
        public final static int CHANNEL_NOT_ESTABLISHED = 6;
        public final static int OPERATION_NOT_SUPPORTED = 8;
        public final static int OUT_OF_RESOURCES = 9;
        public final static int DEVICE_LOCKED_BY_ANOTHER_LINK = 11;
        public final static int NO_LOCK_HELD_BY_THIS_LINK = 12;
        public final static int IO_TIMEOUT = 15;
        public final static int IO_ERROR = 17;
        public final static int INVALID_ADDRESS = 21;
        public final static int ABORT = 23;
        public final static int CHANNEL_ALREADY_ESTABLISHED = 29;
    }
}