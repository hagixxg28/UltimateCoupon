package com.hagi.couponsystem.beans;

public class ErrorBean {
	private int internalErrorCode;
	private String internalMessage;
	private String externalMessage;

	public ErrorBean(int internalErrorCode, String internalMessage, String externalMessage) {
		super();
		this.internalErrorCode = internalErrorCode;
		this.internalMessage = internalMessage;
		this.externalMessage = externalMessage;
	}

	public int getInternalErrorCode() {
		return internalErrorCode;
	}

	public void setInternalErrorCode(int internalErrorCode) {
		this.internalErrorCode = internalErrorCode;
	}

	public String getInternalMessage() {
		return internalMessage;
	}

	public void setInternalMessage(String internalMessage) {
		this.internalMessage = internalMessage;
	}

	public String getExternalMessage() {
		return externalMessage;
	}

	public void setExternalMessage(String externalMessage) {
		this.externalMessage = externalMessage;
	}

}
