package com.example.j2ee_frontend_test.exceptions;

import org.springframework.http.HttpStatus;

public class AppException {
	final String error;
	final Throwable t;
	final HttpStatus httpStatus;
	
	public AppException(String message, Throwable t, HttpStatus httpStatus) {
		this.error = message;
		this.t = t;
		this.httpStatus = httpStatus;
	}

	public String getError() {
		return error;
	}

	public Throwable getT() {
		return t;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
