package org.jmeasure.lxi.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPISocket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * RawSCPISocket
 */
@Slf4j
public class RawSCPISocket implements SCPISocket {

	@Getter
	private String host;

	@Getter
	private int port;

	private Socket socket;

	private Thread receiver;

	private BlockingQueue<SCPICommand> rxStream = new LinkedBlockingDeque<>(32);

	private Thread transmitter;

	private BlockingQueue<String> txStream = new LinkedBlockingDeque<>(32);

	public RawSCPISocket(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void send(SCPICommand... commands) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<commands.length;i++) {
			builder.append(commands[i].toString());
			if(i != commands.length - 1) {
				builder.append(";");
			}
		}
		builder.append("\n");
		txStream.offer(builder.toString());
	}

	private void receive() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
			while (isConnected()) {
				String input = reader.readLine();
				rxStream.offer(new SCPICommand(input));

				log.debug("Received " + truncate(input, 10));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void transmit() {
		try {
			while(isConnected()) {
				String command = txStream.take();
				socket.getOutputStream().write(command.getBytes(StandardCharsets.ISO_8859_1));
				socket.getOutputStream().flush();

				log.debug("Transmitted " + truncate(command, 10));
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String truncate(String in, int length) {
		return (in.length() > length ? in.substring(0, length) + "..." : in.substring(0, in.length() - 1));
	}

	@Override
	public SCPICommand receive(long timeout) throws IOException {
		try {
			if(timeout == 0) {
				return rxStream.take();
			}
			return rxStream.poll(timeout, TimeUnit.MILLISECONDS);
		} catch(InterruptedException e) {
			throw new IOException();
		}
	}

	@Override
	public void connect() throws IOException {
		socket = new Socket(getHost(), getPort());

		receiver = new Thread(this::receive);
		receiver.setName("RX-" + this.getHost());
		receiver.start();

		transmitter = new Thread(this::transmit);
		transmitter.setName("TX-" + this.getHost());
		transmitter.start();
	}

	@Override
	protected void finalize() throws Throwable {
		this.disconnect();
	}

	@Override
	public void disconnect() {
		try {
			socket.close();
		} catch (Exception e) {}
	}

	@Override
	public boolean isConnected() {
		return this.socket != null && !this.socket.isClosed();
	}

}