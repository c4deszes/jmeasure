package org.jmeasure.cli.visa;

import java.io.IOException;

import org.jmeasure.core.serial.SerialSocket;
import org.openmuc.jrxtx.DataBits;
import org.openmuc.jrxtx.FlowControl;
import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.StopBits;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;

public class SerialSocketOption {
	@Option(names = { "--serial-port" })
	String port;

	@ArgGroup(exclusive = false)
	SerialSocketDetailedConfiguration socket;

	public SerialSocket getSocket() throws IOException {
		return new SerialSocket(port, socket.baudrate, socket.databits, socket.parity, socket.stopbits, FlowControl.NONE);
	}

	public static class SerialSocketDetailedConfiguration {
		@Option(names = {"--serial-baudrate"}, defaultValue = "9600")
		int baudrate;

		@Option(names = {"--serial-databits"}, defaultValue = "8", converter = DataBitsConverter.class)
		DataBits databits;

		@Option(names = {"--serial-parity"}, defaultValue = "NONE")
		Parity parity;

		@Option(names = {"--serial-stopbits"}, defaultValue = "1")
		StopBits stopbits;
		
	}

	private static class DataBitsConverter implements ITypeConverter<DataBits> {

		@Override
		public DataBits convert(String value) throws Exception {
			return SerialSocket.toDataBits(Integer.parseInt(value));
		}
		
	}
}