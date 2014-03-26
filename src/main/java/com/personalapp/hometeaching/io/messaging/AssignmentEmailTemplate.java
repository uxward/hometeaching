package com.personalapp.hometeaching.io.messaging;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;

import com.personalapp.hometeaching.model.Companion;

public class AssignmentEmailTemplate extends BaseEmailTemplate {

	private Companion companion;

	public AssignmentEmailTemplate(Companion companion) {
		super.setFromName(getCurrentUser().getPerson().getFullName());
		super.setFromNumber(getCurrentUser().getPerson().getPhoneNumber());
		this.companion = companion;
	}

	public Companion getCompanion() {
		return companion;
	}
}
