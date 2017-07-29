package com.game.utils.common.exception;

public class PayException extends BaseException {
	private static final long serialVersionUID = -7086855509188011754L;

	public PayException() {
	}

	public PayException(String message) {
		super(message);
	}

	public PayException(String message, Throwable e) {
		super(message, e);
	}
}
