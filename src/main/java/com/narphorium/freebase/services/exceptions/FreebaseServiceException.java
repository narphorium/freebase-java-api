package com.narphorium.freebase.services.exceptions;

public class FreebaseServiceException extends Exception {

	private String code;
	private String host;
	private int port;
	private double timeout;
	
	public FreebaseServiceException(String code, String message, String host, int port, double timeout) {
		super(message);
		this.code = code;
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}

	public String getCode() {
		return code;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public double getTimeout() {
		return timeout;
	}

}
