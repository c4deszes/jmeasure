package org.jmeasure.cli.visa;

import java.net.InetAddress;

import org.jmeasure.core.lxi.vxi11.VXI11Socket;
import org.jmeasure.spring.service.VISAResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * VISAShell
 */
@ShellComponent
@ShellCommandGroup("VISA")
public class VISAShell {

    @Autowired
    private VISAResourceManager visa;

    @ShellMethod(key = "visa connect", value = "Connects an instrument")
    public String visaConnect(
        @ShellOption(value = {"-r", "--resource"}, help = "VISA Resource string") String resourceString) {
        
        visa.connect(resourceString);
        
        return null;
    }

    @ShellMethod(key = "visa connect raw", value = "Connects an instrument using raw TCP/IP socket")
    public String visaConnectRaw(
        @ShellOption(value = {"-h", "--host"}, help = "Hostname or address") String host,
        @ShellOption(value = {"-p", "--port"}, defaultValue = "5025", help = "Port number (0-65535)") int port,
        @ShellOption(value = {"-n", "--name"}, defaultValue = "inst0", help = "Instrument name") String name) {


        return null;
    }

    @ShellMethod(key = "visa connect vxi11", value = "Connects an instrument using VXI-11")
    public String visaConnectVXI11(
        @ShellOption(value = {"-h", "--host"}, help = "Hostname or address") String host,
        @ShellOption(value = {"-n", "--name"}, defaultValue = "inst0", help = "Instrument name") String name,
        @ShellOption(value = {"-l", "--lock"}, defaultValue = "0", help = "Lock timeout") int lockTimeout,
        @ShellOption(value = {"-io", "--io-timeout"}, defaultValue = "2000", help = "I/O timeout") int ioTimeout,
        @ShellOption(value = {"-wb", "--write-block-size"}, defaultValue = "8128", help = "Message chunk size") int writeBlockSize) {
        
        //VXI11Socket socket = new VXI11Socket(InetAddress.getByName(host), name, lockTimeout > 0, lockTimeout, ioTimeout, writeBlockSize);
        
        return null;
    }

    @ShellMethod(key = "visa info", value = "Prints out ")
    public String visaInfo(
        @ShellOption(value = {"-n", "--name"}, defaultValue = "inst0", help = "Instrument name") String name) {

        return null;
    }

}