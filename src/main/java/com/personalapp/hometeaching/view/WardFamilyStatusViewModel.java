package com.personalapp.hometeaching.view;

import java.util.List;

import com.personalapp.hometeaching.model.Organization;

public class WardFamilyStatusViewModel {

	private List<FamilyStatusViewModel> familyStatus;

	private String organization;

	public WardFamilyStatusViewModel(List<FamilyStatusViewModel> familyStatus, Organization organization) {
		this.familyStatus = familyStatus;
		this.organization = organization.getOrganization();
	}

	public List<FamilyStatusViewModel> getFamilyStatus() {
		return familyStatus;
	}

	public String getOrganization() {
		return organization;
	}

}
