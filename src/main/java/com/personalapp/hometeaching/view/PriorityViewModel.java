package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.Priority;

public class PriorityViewModel {

	private Long id;

	private String priority;

	public PriorityViewModel(Priority priority) {
		this.id = priority.getId();
		this.priority = priority.getPriority();
	}

	public Long getId() {
		return id;
	}

	public String getPriority() {
		return priority;
	}
}
