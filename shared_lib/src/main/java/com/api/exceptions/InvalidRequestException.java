package com.api.exceptions;

public class InvalidRequestException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public InvalidRequestException() { super(); }
	public InvalidRequestException(String message) { super(message); }
	public InvalidRequestException(String message, Throwable cause) { super(message, cause); }
	public InvalidRequestException(Throwable cause) { super(cause); }
}
