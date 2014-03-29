package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class WardVisitPercentageViewModel {

	private List<VisitPercentageViewModel> visits = newArrayList();

	private OrganizationViewModel organization;

	public WardVisitPercentageViewModel(List<VisitPercentageViewModel> visits, OrganizationViewModel organization) {
		this.visits = visits;
		this.organization = organization;
	}

	public List<VisitPercentageViewModel> getVisits() {
		return visits;
	}

	public OrganizationViewModel getOrganization() {
		return organization;
	}

}
