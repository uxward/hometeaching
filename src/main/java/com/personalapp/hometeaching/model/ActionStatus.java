package com.personalapp.hometeaching.model;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

public enum ActionStatus {
	SUCCESS(1L, "Success"), DUPLICATE(2L, "Duplicate"), ERROR(3L, "Error");

	private final static Map<Long, ActionStatus> ACTION_STATUS = newHashMap();
	static {
		for (ActionStatus status : ActionStatus.values()) {
			ACTION_STATUS.put(status.getId(), status);
		}
	}

	private Long id;
	private String status;

	private ActionStatus(Long id, String status) {
		this.id = id;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public static ActionStatus fromId(Long actionStatusId) {
		return ACTION_STATUS.get(actionStatusId);
	}

}
