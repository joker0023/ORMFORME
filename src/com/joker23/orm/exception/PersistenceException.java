package com.joker23.orm.exception;

public class PersistenceException extends RuntimeException {
	
	private static final long serialVersionUID = 2619929030397389076L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(Throwable cause) {
		super(cause);
	}
}
