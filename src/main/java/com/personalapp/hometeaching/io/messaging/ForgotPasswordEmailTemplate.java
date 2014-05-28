package com.personalapp.hometeaching.io.messaging;

import static com.personalapp.hometeaching.model.Organization.isVisitingTeaching;

import com.personalapp.hometeaching.model.HometeachingUser;
import com.personalapp.hometeaching.model.Organization;

public class ForgotPasswordEmailTemplate extends BaseEmailTemplate {

	private HometeachingUser user;

	private boolean visitingTeaching;

	private String token;

	public ForgotPasswordEmailTemplate(HometeachingUser user, Organization organization, String token) {
		this.user = user;
		this.visitingTeaching = isVisitingTeaching(organization);
		this.token = token;
	}

	public HometeachingUser getUser() {
		return user;
	}

	public boolean getVisitingTeaching() {
		return visitingTeaching;
	}

	public String getToken() {
		return token;
	}
}
