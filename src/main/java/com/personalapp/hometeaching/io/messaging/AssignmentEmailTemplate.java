package com.personalapp.hometeaching.io.messaging;

import static com.personalapp.hometeaching.model.Organization.isVisitingTeaching;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;

import com.personalapp.hometeaching.model.Companion;

public class AssignmentEmailTemplate extends BaseEmailTemplate {

	private Companion companion;

	private boolean visitingTeaching;

	public AssignmentEmailTemplate(Companion companion) {
		super.setFromName(getCurrentUser().getPerson().getFullName());
		super.setFromNumber(getCurrentUser().getPerson().getPhoneNumber());
		this.companion = companion;
		this.visitingTeaching = isVisitingTeaching(companion.getOrganization());
	}

	public Companion getCompanion() {
		return companion;
	}

	public boolean getVisitingTeaching() {
		return visitingTeaching;
	}
}
