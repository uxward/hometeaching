package com.personalapp.hometeaching.model;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

public enum Priority {
	CRITICAL(1L, "1 - Critical"), HIGH(2L, "2 - High"), MEDIUM(3L, "3 - Medium"), LOW(
			4L, "4 - Low"), RESOLVED(5L, "5 - Resolved");

	private final static Map<Long, Priority> PRIORITY = newHashMap();
	static {
		for (Priority priority : Priority.values()) {
			PRIORITY.put(priority.getId(), priority);
		}
	}

	private Long id;
	private String priority;

	private Priority(Long id, String priority) {
		this.id = id;
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public String getPriority() {
		return priority;
	}

	public static Priority fromId(Long priorityId) {
		return PRIORITY.get(priorityId);
	}

}
