package org.jbpm.integration.ws;

public class WSExecutionException extends RuntimeException {

	public WSExecutionException(String message) {
		super(message);
	}

	public WSExecutionException(String message, Throwable t) {
		super(message, t);
	}

}
