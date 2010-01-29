package com.narphorium.freebase.services.exceptions;

public class FreebaseServiceTimeoutException extends FreebaseServiceException {

	public static String ERROR_CODE = "/api/status/error/mql/timeout";
	
	public FreebaseServiceTimeoutException(String description, String host, int port, double timeout) {
		super(ERROR_CODE, description, host, port, timeout);
	}

}
