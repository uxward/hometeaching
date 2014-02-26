package com.personalapp.hometeaching.view;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.personalapp.hometeaching.model.Organization;

public class WardFamilyStatusViewModel {

	private List<FamilyStatusViewModel> familyStatus = newArrayList();

	private List<OrganizationViewModel> organizations = newArrayList();

	public WardFamilyStatusViewModel(List<FamilyStatusViewModel> familyStatus, List<Organization> organizations) {
		this.familyStatus = familyStatus;
		for (Organization organization : organizations) {
			this.organizations.add(new OrganizationViewModel(organization));
		}
	}

	public List<FamilyStatusViewModel> getFamilyStatus() {
		return familyStatus;
	}

	public List<OrganizationViewModel> getOrganizations() {
		return organizations;
	}

}
