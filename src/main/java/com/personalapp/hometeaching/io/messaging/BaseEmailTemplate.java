package com.personalapp.hometeaching.io.messaging;

public class BaseEmailTemplate {

	private String fromName;

	private String fromNumber;

	public BaseEmailTemplate() {
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromNumber() {
		return fromNumber;
	}

	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}
}
