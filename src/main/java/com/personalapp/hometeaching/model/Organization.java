package com.personalapp.hometeaching.model;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public enum Organization {
	WARD(4L, "Ward"), RELIEF_SOCIETY(3L, "Relief society"), HIGH_PRIEST(2L, "High priests"), ELDERS(1L, "Elders quorum");

	private final static Map<Long, Organization> ORGANIZATION = newHashMap();
	static {
		for (Organization status : Organization.values()) {
			ORGANIZATION.put(status.getId(), status);
		}
	}

	private Long id;
	private String organization;

	private Organization(Long id, String organization) {
		this.id = id;
		this.organization = organization;
	}

	public Long getId() {
		return id;
	}

	public String getOrganization() {
		return organization;
	}

	public static Organization fromId(Long id) {
		return ORGANIZATION.get(id);
	}

	public static List<Organization> forDisplay() {
		return Lists.newArrayList(RELIEF_SOCIETY, HIGH_PRIEST, ELDERS);
	}

}
