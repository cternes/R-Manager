package de.slackspace.rmanager.exception;

import org.springframework.http.HttpStatus;

public class InvalidOperationException extends GeneralWebException {

	public InvalidOperationException(HttpStatus status, String name, String message) {
		super(status, name, message);
	}
}
