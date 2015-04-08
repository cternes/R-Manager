package de.slackspace.rmanager.domain;

import org.springframework.http.HttpStatus;

public class ApplicationError {

	private String name;
	private int code;
	private HttpStatus status;
	private String message;
	
	public ApplicationError(HttpStatus code, String name, String message) {
		setStatus(code);
		setCode(code.value());
		setName(name);
		setMessage(message);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}
