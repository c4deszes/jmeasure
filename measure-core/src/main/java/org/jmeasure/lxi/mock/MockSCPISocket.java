package org.jmeasure.lxi.mock;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
public class MockSCPISocket implements SCPISocket {

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

	public Matcher matcher(String path, String command) {
		Pattern regex = Pattern.compile(path);
		return regex.matcher(command);
	}

	public Object[] resolve(Matcher matcher, Method method) {
		Object[] arguments = new Object[method.getParameterCount()];
		for(int i=0;i<arguments.length;i++) {
			Parameter param = method.getParameters()[i];
			if(param.isNamePresent()) {
				String value = matcher.group(param.getName());
				arguments[i] = resolve(value, param.getType());
			}
		}
		return arguments;
	}

	public Object resolve(String value, Class<?> type) {
		if(type.equals(String.class)) {
			return value;
		}
		else if(type.equals(Integer.class)) {
			return Integer.parseInt(value);
		}
		throw new IllegalArgumentException();
	}

	protected void onReceive(SCPICommand in) throws Exception {
		for(Method method: this.getClass().getMethods()) {
			if(method.isAnnotationPresent(OnCommand.class)) {
				OnCommand path = method.getAnnotation(OnCommand.class);
				Matcher matcher = matcher(path.value(), in.getCommand());
				if(matcher.find()) {
					Object[] arguments = resolve(matcher, method);
					Object ret = method.invoke(this, arguments);

					if(ret instanceof SCPICommand) {
						this.pushResponse((SCPICommand) ret);
					}
					return;
				}
			}
		}
		throw new NoSuchMethodException("No mapping found for '" + in.getCommand() + "'");
	}

	protected final void pushResponse(SCPICommand out) {
		rxStream.offer(out);
	}

	@OnCommand("*IDN?")
	private final SCPICommand onIDNQuery() {
		return new SCPICommand(idn.value());
	}

	@Override
	public final void send(SCPICommand... commands) throws IOException {
		if(!isConnected()) {
			throw new IOException("Device not connected.");
		}
		for(SCPICommand command: commands) {
			try {
				this.onReceive(command);
			} catch(Exception e) {
				log.warn("Failed to invoke callback, reason: " + e.getMessage());
			}
		}
	}

	@Override
	public final SCPICommand receive(long timeout) throws IOException {
		try {
			if(!isConnected()) {
				throw new IOException("Device not connected.");
			}
			if(timeout == 0) {
				return rxStream.poll();
			}
			return rxStream.poll(timeout, TimeUnit.MILLISECONDS);
		} catch(InterruptedException e) {
			throw new IOException("");
		}
	}
	
}