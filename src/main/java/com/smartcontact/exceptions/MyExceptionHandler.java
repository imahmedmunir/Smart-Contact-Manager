
package com.smartcontact.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

	@ExceptionHandler(value = ConstraintViolationException.class)
	public void constraintExc(ConstraintViolationException ex) throws Exception {
		throw new Exception("Duplication of  data");
	}

	@ExceptionHandler(value = IllegalStateException.class)
	public void getnull() throws Exception {
			throw new Exception("Illegal ");
	}
	
	@ExceptionHandler(NullPointerException.class)
	public String nullex(NullPointerException ex) {
		return ex.getMessage();
		
	}
}
