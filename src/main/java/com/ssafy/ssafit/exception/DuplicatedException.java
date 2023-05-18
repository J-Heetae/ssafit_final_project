package com.ssafy.ssafit.exception;

public class DuplicatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicatedException() {

	}

	public DuplicatedException(String message) {
		super(message);
	}
  
}
