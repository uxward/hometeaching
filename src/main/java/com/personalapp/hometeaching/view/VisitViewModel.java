package com.personalapp.hometeaching.view;

import java.util.Date;

import com.personalapp.hometeaching.model.ActionStatus;
import com.personalapp.hometeaching.model.Visit;

public class VisitViewModel extends ActionViewModel {
	private Long id;

	private Long assignmentId;

	private Long familyId;

	private String notes;

	private Boolean visited;

	private Date monthYear;

	private Integer month;

	private Integer year;

	private Boolean saveVisit;

	private Long companionId;

	public VisitViewModel() {

	}

	public VisitViewModel(Visit visit) {
		setupVisitViewModel(visit);
	}

	public VisitViewModel(Visit visit, ActionStatus status) {
		setupVisitViewModel(visit);
		super.setActionStatus(status);
	}

	private void setupVisitViewModel(Visit visit) {
		this.id = visit.getId();
		this.assignmentId = visit.getAssignmentId();
		this.familyId = visit.getFamilyId();
		this.notes = visit.getNotes();
		this.visited = visit.getVisited();
		this.monthYear = visit.getVisitDate();
		this.month = visit.getMonth();
		this.year = visit.getYear();
	}

	public Long getId() {
		return id;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public String getNotes() {
		return notes;
	}

	public Boolean getVisited() {
		return visited;
	}

	public Date getMonthYear() {
		return monthYear;
	}

	public Integer getMonth() {
		return month;
	}

	public Integer getYear() {
		return year;
	}

	public Boolean getSaveVisit() {
		return saveVisit;
	}

	public Long getCompanionId() {
		return companionId;
	}
}
