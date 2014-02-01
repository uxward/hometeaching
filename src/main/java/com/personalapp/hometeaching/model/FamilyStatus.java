package com.personalapp.hometeaching.model;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

public enum FamilyStatus {
	ACTIVE(1L, "Active"), RECENT_CONVERT(2L, "Recent Convert"), INACTIVE(3L,
			"Inactive"), UNKNOWN(4L, "Unknown"), MOVED(5L, "Moved"), DO_NOT_CONTACT(6L, "Do Not Contact");

	private final static Map<Long, FamilyStatus> FAMILY_STATUS = newHashMap();
	static {
		for (FamilyStatus status : FamilyStatus.values()) {
			FAMILY_STATUS.put(status.getId(), status);
		}
	}

	private Long id;
	private String status;

	private FamilyStatus(Long id, String status) {
		this.id = id;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public static FamilyStatus fromId(Long familyStatusId) {
		return FAMILY_STATUS.get(familyStatusId);
	}

}
