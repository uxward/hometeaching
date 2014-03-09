package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

public class WardFamilyStatusViewModel {

	private List<FamilyStatusViewModel> familyStatus = newArrayList();

	private List<OrganizationViewModel> organizations = newArrayList();

	public WardFamilyStatusViewModel(List<FamilyStatusViewModel> familyStatus, List<OrganizationViewModel> organizations) {
		this.familyStatus = familyStatus;
		this.organizations = organizations;
	}

	public List<FamilyStatusViewModel> getFamilyStatus() {
		return familyStatus;
	}

	public List<OrganizationViewModel> getOrganizations() {
		return organizations;
	}

}
