package com.personalapp.hometeaching.view;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Assignment;

public class AssignmentViewModel extends ActionViewModel {
	private Long id;

	private FamilyViewModel family;

	public AssignmentViewModel(Assignment assignment, ActionStatus status) {
		this.id = assignment.getId();
		this.family = new FamilyViewModel(assignment.getFamily(), true, false, false);
		setActionStatus(status);
	}

	public AssignmentViewModel(Assignment assignment) {
		this.id = assignment.getId();
		this.family = new FamilyViewModel(assignment.getFamily(), true, false, false);
	}

	public Long getId() {
		return id;
	}

	public FamilyViewModel getFamily() {
		return family;
	}
}
