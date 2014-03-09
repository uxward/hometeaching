package com.personalapp.hometeaching.model;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

public enum Role {
	ADMIN("Admin", "admin"), HOMETEACHER("Home teacher", "hometeacher"), LEADER("Leader", "leader"), MEMBERSHIP("Membership", "membership");

	private final static Map<String, Role> ROLE = newHashMap();
	static {
		for (Role status : Role.values()) {
			ROLE.put(status.getRole(), status);
		}
	}

	private String role;
	private String display;

	private Role(String display, String role) {
		this.display = display;
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public String getDisplay() {
		return display;
	}

	public static Role fromRole(String role) {
		return ROLE.get(role);
	}

}
