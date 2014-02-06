package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.Visit;

public class VisitHistoryModel {
	private FamilyViewModel familyViewModel;

	private List<VisitViewModel> visits = newArrayList();

	public VisitHistoryModel(List<Visit> visits, Family family) {
		for (Visit visit : visits) {
			this.visits.add(new VisitViewModel(visit));
		}

		this.familyViewModel = new FamilyViewModel(family, true, true, true);
	}

	public FamilyViewModel getFamilyViewModel() {
		return familyViewModel;
	}

	public List<VisitViewModel> getVisits() {
		return visits;
	}
}
