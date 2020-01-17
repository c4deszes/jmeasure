package org.jmeasure.lxi;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

/**
 * SCPICommand is a message understood by SCPI devices
 * 
 * In general the command format:
 * 	ROOT:SUB:CMD PARAM1,PARAM2,...
 * 
 * 
 */
public final class SCPICommand {

	@Getter
	private String raw;

	@Getter
	private String command;

	@Getter
	private List<String> parameters;

	public SCPICommand(String command, List<String> params) {
		if(command.contains(" ") || command.contains(",")) {
			throw new IllegalArgumentException("Invalid command");
		}
		this.command = command;
		this.parameters = Collections.unmodifiableList(params);
		this.raw = this.toString();
	}

	public SCPICommand(String command, String... params) {
		this(command, Arrays.asList(params));
	}

	public SCPICommand(String raw) {
		this.raw = raw;

		int firstSpace = this.raw.indexOf(" ");
		if(firstSpace == -1 || firstSpace == raw.length()) {
			this.command = this.raw;
			this.parameters = Collections.emptyList();
		}
		else {
			this.command = raw.substring(0, firstSpace);
			String[] params = raw.substring(firstSpace+1, raw.length()).split(",");
			this.parameters = Collections.unmodifiableList(Arrays.asList(params));
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getCommand());
		if(this.getParameters().size() != 0) {
			builder.append(" ");
		}
		Iterator<String> it = this.getParameters().iterator();
		while(it.hasNext()) {
			String param = it.next();
			builder.append(param);
			if(it.hasNext()) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

	/**
	 * Returns the given parameter's value
	 * 
	 * E.g: for CMD PARAM1,VALUE1, getParameter(PARAM1) would return VALUE1
	 * 
	 * @param name Parameter's name
	 * @return The parameter's value if there's a string in the argument list after the named parameter
	 * 			null if there's no such parameter or there is no value associated to it
	 */
	public String getParameter(String name) {
		Iterator<String> it = parameters.iterator();
		while(it.hasNext()) {
			if(it.next().equals(name)) {
				if(it.hasNext()) {
					return it.next();
				}
				else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the given parameter's value as float
	 * @param name Parameter's name
	 * @return Parameter's value
	 */
	public float getFloat(String name) {
		return Float.parseFloat(this.getParameter(name));
	}

	/**
	 * Returns the given parameter's value as int
	 * @param name Parameter's name
	 * @return Parameter's value
	 */
	public int getInt(String name) {
		return Integer.parseInt(this.getParameter(name));
	}

	/**
	 * Returns whether or not the given parameter exists
	 * 
	 * @param param Parameter's name
	 * @return {@code true} if there's such parameter
	 */
	public boolean hasParameter(String param) {
		return this.parameters.contains(param);
	}

	public static Builder builder() {
		return new Builder();
	}

	public final static class Builder {

		private String command;

		private List<String> parameters = new LinkedList<>();

		public Builder command(String command) {
			this.command = command;
			return this;
		}

		public Builder command(String... cmds) {
			this.command = String.join(":", cmds);
			return this;
		}

		public Builder query(String command) {
			this.command = command + "?";
			return this;
		}

		public Builder with(String param, String value) {
			this.parameters.add(param);
			this.parameters.add(value);
			return this;
		}

		public Builder with(String param, int value) {
			return this.with(param, String.valueOf(value));
		}

		public Builder with(String param, float value) {
			return this.with(param, String.valueOf(value));
		}

		public Builder with(String param) {
			this.parameters.add(param);
			return this;
		}

		public SCPICommand build() {
			return new SCPICommand(command, parameters);
		}
	}
	
}