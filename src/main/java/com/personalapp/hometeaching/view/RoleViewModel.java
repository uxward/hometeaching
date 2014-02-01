package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Role;

public class RoleViewModel {

	private String role;

	private String display;

	public RoleViewModel(Role role) {
		this.role = role.getRole();
		this.display = role.getDisplay();
	}

	public String getRole() {
		return role;
	}

	public String getDisplay() {
		return display;
	}

}
