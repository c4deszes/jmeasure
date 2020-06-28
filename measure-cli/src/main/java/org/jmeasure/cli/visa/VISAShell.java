package org.jmeasure.cli.visa;

import java.io.IOException;
import java.net.InetAddress;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.lxi.raw.RawSocket;
import org.jmeasure.core.lxi.vxi11.VXI11Socket;
import org.jmeasure.core.visa.VISAResourceManager;
import org.jmeasure.core.visa.VisaException;

/**
 * VISAShell
 */

public class VISAShell {

	/*
    @Autowired
    private VISAResourceManager visa;

    @ShellMethod(key = "visa connect", value = "Connects an instrument")
    public String visaConnect(
            @ShellOption(value = { "-r", "--resource" }, help = "VISA Resource string") String resourceString) {

        try {
            ISocket socket = visa.connect(resourceString);
            
            return "Connected " + socket;
        } catch (IOException e) {
            return "I/O Error: " + e.getMessage();
        } catch (VisaException e) {
            return "VISA Error: " + e.getMessage();
        }
    }

    @ShellMethod(key = "visa connect raw", value = "Connects an instrument using raw TCP/IP socket")
    public String visaConnectRaw(
        @ShellOption(value = {"-h", "--host"}, help = "Hostname or address") String host,
        @ShellOption(value = {"-p", "--port"}, defaultValue = "5025", help = "Port number (0-65535)") int port) {

        try {
            RawSocket socket = new RawSocket(host, port);
            visa.connect(socket);
            return "Connected " + socket;
        } catch(IOException e) {
            return "I/O Error: " + e.getMessage();
        } catch(VisaException e) {
            return "VISA Error: " + e.getMessage();
        }
    }

    @ShellMethod(key = "visa connect vxi11", value = "Connects an instrument using VXI-11")
    public String visaConnectVXI11(
        @ShellOption(value = {"-h", "--host"}, help = "Hostname or address") String host,
        @ShellOption(value = {"-n", "--name"}, defaultValue = ShellOption.NULL, help = "Instrument name") String name,
        @ShellOption(value = {"-l", "--lock"}, defaultValue = "0", help = "Lock timeout") int lockTimeout,
        @ShellOption(value = {"-io", "--io-timeout"}, defaultValue = "2000", help = "I/O timeout") int ioTimeout,
        @ShellOption(value = {"-wb", "--write-block-size"}, defaultValue = "8128", help = "Message chunk size") int writeBlockSize) {
        
        try {
            VXI11Socket socket = new VXI11Socket(InetAddress.getByName(host), name, lockTimeout > 0, lockTimeout, ioTimeout, writeBlockSize);
            visa.connect(socket);
            return "Connected " + socket;
        } catch(IOException e) {
            return "I/O Error: " + e.getMessage();
        } catch(VisaException e) {
            return "VISA Error: " + e.getMessage();
        }
    }

    @ShellMethod(key = "visa info", value = "Prints out ")
    public String visaInfo(
        @ShellOption(value = {"-n", "--name"}, defaultValue = "inst0", help = "Instrument name") String name) {

        return null;
	}
	*/

}