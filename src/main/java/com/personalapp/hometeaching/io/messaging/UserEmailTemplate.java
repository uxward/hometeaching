package com.personalapp.hometeaching.io.messaging;

import com.personalapp.hometeaching.model.HometeachingUser;

public class UserEmailTemplate extends BaseEmailTemplate {

	private HometeachingUser user;

	public UserEmailTemplate(BaseEmailTemplate baseEmailTemplate, HometeachingUser user) {
		super.setFromName(baseEmailTemplate.getFromName());
		super.setFromNumber(baseEmailTemplate.getFromNumber());
		this.user = user;
	}

	public HometeachingUser getUser() {
		return user;
	}

	public void setUser(HometeachingUser user) {
		this.user = user;
	}
}
