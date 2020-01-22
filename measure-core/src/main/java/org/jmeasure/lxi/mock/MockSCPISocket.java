package org.jmeasure.lxi.mock;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPISocket;

import lombok.extern.slf4j.Slf4j;

/**
 * MockSCPIDevice
 */
@Slf4j
public abstract class MockSCPISocket implements SCPISocket {

	private boolean connected = false;

	private BlockingQueue<SCPICommand> rxStream = new LinkedBlockingQueue<>(10);

	private DeviceIdentifier idn;

	public MockSCPISocket(DeviceIdentifier idn) {
		this.idn = idn;
	}

	@Override
	public final void connect() throws IOException {
		this.connected = true;
	}

	@Override
	public final void disconnect() {
		this.connected = false;
	}

	@Override
	public final boolean isConnected() {
		return this.connected;
	}

	/**
	 * Matches the given command path to the incoming command
	 * @param path
	 * @param command
	 * @return
	 */
	private Matcher matcher(String path, String command) {
		Pattern regex = Pattern.compile(path);
		return regex.matcher(command);
	}

	/**
	 * Resolves method parameters using the given SCPI command and matcher
	 * 
	 * 
	 * 
	 * @param matcher Matcher object, has to match before
	 * @param method
	 * @param command SCPICommand
	 * @return
	 */
	private Object[] resolve(Matcher matcher, Method method, SCPICommand command) {
		Object[] arguments = new Object[method.getParameterCount()];
		for(int i=0;i<arguments.length;i++) {
			Parameter param = method.getParameters()[i];
			if(param.getType().equals(SCPICommand.class)) {
				arguments[i] = command;
			}
			else if(param.isNamePresent()) {
				String value = matcher.group(param.getName());
				arguments[i] = resolve(value, param.getType());
			}
		}
		return arguments;
	}

	private Object resolve(String value, Class<?> type) {
		if(type.equals(String.class)) {
			return value;
		}
		else if(type.equals(Integer.class) || type.equals(int.class)) {
			return Integer.parseInt(value);
		}
		throw new IllegalArgumentException("Couldn't convert '" + value + "' to " + type.getSimpleName());
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	private SCPICommand resolveReturnType(Object object) {
		if(object instanceof SCPICommand) {
			return (SCPICommand) object;
		}
		else if(object instanceof String) {
			return new SCPICommand((String) object);
		}
		return null;
	}

	/**
	 * Called when the socket has received a command
	 * 
	 * @param in Received SCPI command
	 * @throws Exception
	 */
	protected void onReceive(SCPICommand in) throws Exception {
		for(Method method: this.getClass().getMethods()) {
			if(method.isAnnotationPresent(OnCommand.class)) {
				OnCommand path = method.getAnnotation(OnCommand.class);
				Matcher matcher = matcher(path.value(), in.getCommand());
				if(matcher.find()) {
					Object[] arguments = resolve(matcher, method, in);
					Object ret = method.invoke(this, arguments);

					SCPICommand response = resolveReturnType(ret);
					if(response != null) {
						this.pushResponse(response);
					}
					return;
				}
			}
		}
		this.onNotMapped(in);
	}

	/**
	 * Pushes the SCPI command to the response stream
	 * 
	 * @param response SCPI response
	 */
	protected final void pushResponse(SCPICommand response) {
		rxStream.offer(response);
	}

	/**
	 * Called on identification query commands
	 * 
	 * @return Response including the device's identifier
	 */
	@OnCommand("\\*IDN\\?")
	public final SCPICommand onIDNQuery() {
		return new SCPICommand(idn.value());
	}

	@OnCommand("\\*OPC\\?")
	public final SCPICommand onOPCQuery() {
		return new SCPICommand("1");
	}

	/**
	 * Called on reset command
	 */
	@OnCommand("\\*RST")
	public abstract void onReset();

	/**
	 * Called when no mapping was found for the inbound command
	 * 
	 * @param command Inbound SCPI command
	 */
	public abstract void onNotMapped(SCPICommand command);

	@Override
	public final void send(SCPICommand... commands) throws IOException {
		if(!isConnected()) {
			throw new IOException("Device not connected.");
		}
		for(SCPICommand command: commands) {
			try {
				this.onReceive(command);
			} catch(Exception e) {
				log.warn("Failed to invoke callback", e);
			}
		}
	}

	@Override
	public final Optional<SCPICommand> receive(long timeout) throws IOException {
		try {
			if(!isConnected()) {
				throw new IOException("Device not connected.");
			}
			SCPICommand response = timeout == 0 ? rxStream.take() : rxStream.poll(timeout, TimeUnit.MILLISECONDS);
			return Optional.ofNullable(response);
		} catch(InterruptedException e) {
			throw new IOException();
		}
	}
	
}