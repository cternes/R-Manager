package de.slackspace.rmanager.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.slackspace.rmanager.domain.ApplicationError;

@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
        ApplicationError error;
        
		if(e instanceof GeneralWebException) {
			//TODO: Log exception
			GeneralWebException exception = (GeneralWebException) e;
			error = new ApplicationError(exception.getStatus(), exception.getName(), exception.getMessage());
		}
		else {
			//TODO: Log exception
			error = new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An internal error occurred");
		}
		
		return handleExceptionInternal(e, error, headers, error.getStatus(), request);
    }
}
