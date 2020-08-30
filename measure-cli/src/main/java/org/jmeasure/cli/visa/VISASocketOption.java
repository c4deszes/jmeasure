package org.jmeasure.cli.visa;

import org.jmeasure.core.device.ISocket;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

public class VISASocketOption {

	@ArgGroup(exclusive = false)
	RawSocketOption rawSocket;
	
	@ArgGroup(exclusive = false)
	SerialSocketOption serialSocket;

	@Option(names = {"--resource"})
	String resourceString;
	
}