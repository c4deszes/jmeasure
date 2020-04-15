package org.jmeasure.core.visa;

import java.io.IOException;

/**
 * UnsupportedSocketException
 */
public class UnsupportedSocketException extends Exception {

	private static final long serialVersionUID = -7900104889511905613L;

	public UnsupportedSocketException() {
	}

	public UnsupportedSocketException(String message) {
		super(message);
	}

	public UnsupportedSocketException(Throwable cause) {
		super(cause);
	}

	public UnsupportedSocketException(String message, Throwable cause) {
		super(message, cause);
	}

}