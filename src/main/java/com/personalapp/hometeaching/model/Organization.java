package com.personalapp.hometeaching.model;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

public enum Organization {
	ELDERS(1L, "Elders quorum"), HIGH_PRIEST(2L, "High priests"), RELIEF_SOCIETY(3L, "Relief society"), AGGREGATE(4L, "Aggregate");

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

}
