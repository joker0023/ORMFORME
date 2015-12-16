package com.joker23.orm.executor;

import com.joker23.orm.exception.PersistenceException;

public class ExecutorException extends PersistenceException {

	private static final long serialVersionUID = 1272434500440498787L;

	public ExecutorException() {
		super();
	}

	public ExecutorException(String message) {
		super(message);
	}

	public ExecutorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutorException(Throwable cause) {
		super(cause);
	}

}
