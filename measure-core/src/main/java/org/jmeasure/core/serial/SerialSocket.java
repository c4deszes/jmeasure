package org.jmeasure.core.serial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SystemUtils;
import org.jmeasure.core.device.ISocket;
import org.openmuc.jrxtx.DataBits;
import org.openmuc.jrxtx.FlowControl;
import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.SerialPort;
import org.openmuc.jrxtx.SerialPortBuilder;
import org.openmuc.jrxtx.SerialPortTimeoutException;
import org.openmuc.jrxtx.StopBits;

/**
 * SerialSocket
 */
public class SerialSocket implements ISocket {

	private SerialPort socket;

	public SerialSocket(String port, int baudRate) throws IOException {
		this(port, baudRate, DataBits.DATABITS_8, Parity.NONE, StopBits.STOPBITS_1, FlowControl.NONE);
	}

	public SerialSocket(String port, int baudRate, DataBits dataBits, Parity parity, StopBits stopBits, FlowControl flowControl)
			throws IOException {
		this.socket = SerialPortBuilder.newBuilder(port)
										.setBaudRate(baudRate)
										.setDataBits(dataBits)
										.setParity(parity)
										.setStopBits(stopBits)
										.setFlowControl(flowControl)
										.build();
	}

	private final static Pattern CONFIG_PATTERN = Pattern.compile("(?<baudrate>[0-9]+)/(?<databits>[0-9])/(?<parity>[NOEMS])/(?<stopbits>[12])");

	public static SerialSocket create(String port, String params) throws IOException {
		Matcher matcher = CONFIG_PATTERN.matcher(params);
		if(!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		int baudrate = Integer.parseInt(matcher.group("baudrate"));
		int databits = Integer.parseInt(matcher.group("databits"));
		String parity = matcher.group("parity");
		int stopbits = Integer.parseInt(matcher.group("stopbits"));

		return new SerialSocket(port, baudrate, toDataBits(databits), toParity(parity), toStopBits(stopbits), FlowControl.NONE);
	}

	public static DataBits toDataBits(int count) {
		switch(count) {
			case 5: return DataBits.DATABITS_5;
			case 6: return DataBits.DATABITS_6;
			case 7: return DataBits.DATABITS_7;
			case 8: return DataBits.DATABITS_8;
			default: throw new IllegalArgumentException("Databits must be 5-8, got " + count);
		}
	}

	private static Parity toParity(String parity) {
		switch(parity) {
			case "N":
			default: throw new IllegalArgumentException("Parity must be None, Odd, Even, Mark, Space, but was: " + parity);
		}
	}

	private static StopBits toStopBits(int stopbits) {
		switch(stopbits) {
			case 1: return StopBits.STOPBITS_1;
			case 2: return StopBits.STOPBITS_2;
			default: throw new IllegalArgumentException("Stop bit must be 1 or 2, but was: " + stopbits);
		}
	}

	/*
	public SerialSocket(String port, String params) {
		this.port = port;
		//TODO: extract
		//new SerialSocket("COM3", "9600/8/N/1");
	}
	*/

	@Override
	public void close() {
		try {
			socket.close();
		} catch (NullPointerException | IOException e) {}
	}

	@Override
	public boolean isConnected() {
		return this.socket != null && !socket.isClosed();
	}

	@Override
	public String getResourceString() {
		return "ASRL" + SerialSocketFactory.getPortNumber(this.socket.getPortName());
	}

	@Override
	public void send(ByteBuffer message) throws IOException {
		if(socket == null) {
			throw new IOException("Socket is closed.");
		}
		if(!message.hasArray()) {
			throw new IOException("Message empty.");
		}
		socket.getOutputStream().write(message.array());
	}

	@Override
	public ByteBuffer receive(int count, long timeout) throws IOException, TimeoutException {
		int previousTimeout = socket.getSerialPortTimeout();
		socket.setSerialPortTimeout((int)timeout);

		byte[] input = new byte[count];
		socket.getInputStream().read(input);

		socket.setSerialPortTimeout(previousTimeout);

		return ByteBuffer.wrap(input);
	}

	@Override
	public ByteBuffer receive(char delimiter, long timeout) throws IOException, TimeoutException {
		try {
			int previousTimeout = socket.getSerialPortTimeout();
			socket.setSerialPortTimeout((int)timeout);
			
			ArrayList<Byte> input = new ArrayList<>();
			byte in = (byte) socket.getInputStream().read();
			while(in != delimiter) {
				input.add(in);
				in = (byte) socket.getInputStream().read();
			}
			socket.setSerialPortTimeout(previousTimeout);
			ByteBuffer bytes = ByteBuffer.allocate(input.size());
			for(byte b : input) {
				bytes.put(b);
			}
			return bytes;
		} catch(SerialPortTimeoutException e) {
			throw new TimeoutException(e.getMessage());
		}
	}

}