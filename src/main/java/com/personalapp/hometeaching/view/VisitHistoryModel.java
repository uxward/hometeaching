package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.personalapp.hometeaching.model.Family;
import com.personalapp.hometeaching.model.Visit;

public class VisitHistoryModel {
	private FamilyViewModel familyViewModel;

	private List<FamilyVisitViewModel> familyVisits = newArrayList();

	public VisitHistoryModel(List<Visit> homeTeachingVisits, List<Visit> visitingTeachingVisits, Family family) {
		for (int i = 0; i < homeTeachingVisits.size(); i++) {
			this.familyVisits.add(new FamilyVisitViewModel(homeTeachingVisits.get(i), visitingTeachingVisits.get(i)));
		}

		this.familyViewModel = new FamilyViewModel(family, true, true, true);
	}

	public FamilyViewModel getFamilyViewModel() {
		return familyViewModel;
	}

	public List<FamilyVisitViewModel> getFamilyVisits() {
		return familyVisits;
	}
}
