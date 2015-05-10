package de.slackspace.rmanager.exception;

import org.springframework.http.HttpStatus;

public class GeneralWebException extends RuntimeException {

	private String name;
	private HttpStatus status;
	
	public GeneralWebException(HttpStatus status, String name, String message) {
		super(message);
		setStatus(status);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
