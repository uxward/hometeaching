package com.personalapp.hometeaching.io.messaging;

import com.personalapp.hometeaching.model.Companion;

public class AssignmentEmailTemplate extends BaseEmailTemplate {

	private Companion companion;

	public AssignmentEmailTemplate(BaseEmailTemplate baseEmailTemplate, Companion companion) {
		super.setFromName(baseEmailTemplate.getFromName());
		super.setFromNumber(baseEmailTemplate.getFromNumber());
		this.companion = companion;
	}

	public Companion getCompanion() {
		return companion;
	}
}
