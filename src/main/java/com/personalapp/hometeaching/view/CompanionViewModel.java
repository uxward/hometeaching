package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.personalapp.hometeaching.model.Assignment;
import com.personalapp.hometeaching.model.Companion;
import com.personalapp.hometeaching.model.PersonCompanion;

public class CompanionViewModel {

	private Long id;

	private List<PersonViewModel> hometeachers = newArrayList();

	private List<FamilyViewModel> assignments = newArrayList();

	public CompanionViewModel() {

	}

	public CompanionViewModel(Companion companion, Boolean populateAssignments) {
		if (companion != null) {
			this.id = companion.getId();

			for (PersonCompanion personCompanion : companion.getCompanions()) {
				hometeachers.add(new PersonViewModel(personCompanion.getPerson(), true, false));
			}

			if (populateAssignments) {
				for (Assignment assignment : companion.getAssignments()) {
					if (assignment.getActive()) {
						FamilyViewModel family = new FamilyViewModel(assignment.getFamily(), true, false, true);
						family.setAssignmentId(assignment.getId());
						assignments.add(family);
					}
				}
			}
		}
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setHometeachers(List<PersonViewModel> hometeachers) {
		this.hometeachers = hometeachers;
	}

	public List<PersonViewModel> getHometeachers() {
		return hometeachers;
	}

	public void setAssignments(List<FamilyViewModel> assignments) {
		this.assignments = assignments;
	}

	public List<FamilyViewModel> getAssignments() {
		return assignments;
	}

	public String getAllHometeachers() {
		String hometeachers = "";
		for (PersonViewModel person : this.hometeachers) {
			if (!hometeachers.equals("")) {
				hometeachers += ", ";
			}
			hometeachers += person.getFirstName();
			if (person.getFamily() != null) {
				hometeachers += " " + person.getFamily().getFamilyName();
			}
		}
		return hometeachers;
	}

	public String getAllAssignments() {
		String assignments = "";
		for (FamilyViewModel family : this.assignments) {
			if (assignments.equals("")) {
				assignments += family.getFamilyName();
			} else {
				assignments += " and " + family.getFamilyName();
			}
		}
		return assignments;
	}
}
