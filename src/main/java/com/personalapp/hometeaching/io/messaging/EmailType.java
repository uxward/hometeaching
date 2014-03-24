package com.personalapp.hometeaching.io.messaging;

public enum EmailType {
	NEW_USER("New User"), UPDATED_ASSIGNMENT("Relief society");

	private String type;

	private EmailType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
