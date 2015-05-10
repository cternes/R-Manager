package de.slackspace.rmanager.exception;

import org.springframework.http.HttpStatus;

public class UnknownObjectException extends GeneralWebException {

	public UnknownObjectException(HttpStatus status, String name, String message) {
		super(status, name, message);
	}

}
