package com.personalapp.hometeaching.io.messaging;

import static com.personalapp.hometeaching.security.SecurityUtils.getCurrentUser;

import com.personalapp.hometeaching.model.HometeachingUser;

public class UserEmailTemplate extends BaseEmailTemplate {

	private HometeachingUser user;

	public UserEmailTemplate(HometeachingUser user) {
		super.setFromName(getCurrentUser().getPerson().getFullName());
		super.setFromNumber(getCurrentUser().getPerson().getPhoneNumber());
		this.user = user;
	}

	public HometeachingUser getUser() {
		return user;
	}

	public void setUser(HometeachingUser user) {
		this.user = user;
	}
}
