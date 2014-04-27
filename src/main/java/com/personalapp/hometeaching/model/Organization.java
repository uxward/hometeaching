package com.personalapp.hometeaching.model;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public enum Organization {
	WARD(4L, "Ward", "D4"), RELIEF_SOCIETY(3L, "Relief Society", "RS"), HIGH_PRIEST(2L, "High priests", "HP"), ELDERS(1L, "Elders quorum", "EQ");

	private final static Map<Long, Organization> ORGANIZATION = newHashMap();
	static {
		for (Organization status : Organization.values()) {
			ORGANIZATION.put(status.getId(), status);
		}
	}

	private Long id;
	private String name;
	private String abbreviation;

	private Organization(Long id, String name, String abbreviation) {
		this.id = id;
		this.name = name;
		this.abbreviation = abbreviation;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getAbbreviation(){
		return abbreviation;
	}

	public static Organization fromId(Long id) {
		return id != null ? ORGANIZATION.get(id) : null;
	}

	public static List<Organization> forDisplay() {
		return Lists.newArrayList(RELIEF_SOCIETY, HIGH_PRIEST, ELDERS);
	}

	public static boolean isVisitingTeaching(Organization organization) {
		return RELIEF_SOCIETY.equals(organization);
	}

}
