package com.personalapp.hometeaching.io.messaging;

import static com.personalapp.hometeaching.model.Organization.isVisitingTeaching;
import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;

public class UserEmailTemplate extends BaseEmailTemplate {

	private HometeachingUser user;

	private boolean visitingTeaching;

	public UserEmailTemplate(HometeachingUser user, Organization organization) {
		super.setFromName(getCurrentUser().getPerson().getFullName());
		super.setFromNumber(getCurrentUser().getPerson().getPhoneNumber());
		this.user = user;
		this.visitingTeaching = isVisitingTeaching(organization);
	}

	public HometeachingUser getUser() {
		return user;
	}
	
	public boolean getVisitingTeaching(){
		return visitingTeaching;
	}
}
