package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.personalapp.hometeaching.model.Visit;

public class FamilyVisitViewModel {

	private List<VisitViewModel> visits = newArrayList();

	public FamilyVisitViewModel(Visit homeTeachingVisit, Visit visitingTeachingVisit) {
		this.visits = newArrayList(new VisitViewModel(homeTeachingVisit), new VisitViewModel(visitingTeachingVisit));
	}

	public List<VisitViewModel> getVisits() {
		return visits;
	}
}
